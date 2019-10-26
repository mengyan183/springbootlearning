package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitMqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
@Slf4j
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件值（站点id）
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /*    //新增页面
        public CmsPageResult add(CmsPage cmsPage){
            //校验页面名称、站点Id、页面webpath的唯一性
            //根据页面名称、站点Id、页面webpath去cms_page集合，如果查到说明此页面已经存在，如果查询不到再继续添加
            CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
            if(cmsPage1==null){
                //调用dao新增页面
                cmsPage.setPageId(null);
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            }
            //添加失败
            return new CmsPageResult(CommonCode.FAIL,null);

        }*/
    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //抛出异常，非法参数异常..指定异常信息的内容
            return new CmsPageResult(CommonCode.INVALID_PARAM, cmsPage);
        }
        //校验页面名称、站点Id、页面webpath的唯一性
        //根据页面名称、站点Id、页面webpath去cms_page集合，如果查到说明此页面已经存在，如果查询不到再继续添加
        CmsPage existCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (existCmsPage != null) {
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //调用dao新增页面 (使用自动生成主键)
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);

    }

    //根据页面id查询页面
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        return optional.orElse(null);
    }

    //修改页面
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id从数据库查询页面信息
        CmsPage one = this.getById(id);
        if (one != null) {
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            // 更新dataURL
            one.setDataUrl(cmsPage.getDataUrl());
            //提交修改
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS, one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);

    }

    //根据id删除页面
    public ResponseResult delete(String id) {
        //先查询一下
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 静态化程序获取页面的DataUrl
     * 静态化程序远程请求DataUrl获取数据模型。
     * 静态化程序获取页面的模板信息
     * 执行页面静态化
     *
     * @author guoxing
     * @date 2019-08-30 2:42 PM
     * @since 2.0.0
     **/
    public String getPageHtmlByPageId(String pageId) throws Exception {
        if (StringUtils.isBlank(pageId)) {
            return null;
        }
        // 获取 cmspage
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            return null;
        }
        // 获取 cmspage 中的 地址
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            return null;
        }
        // 获取 模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        if (body == null) {
            return null;
        }
        // 获取 静态资源 模板
        String templateId = cmsPage.getTemplateId();
        String content = templateService.getTemplateContentById(templateId);
        if (StringUtils.isBlank(content)) {
            return null;
        }
        // 根据 模型数据 和 模板 转换为 静态资源 (html)
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //加载模板
        //模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", content);
        configuration.setTemplateLoader(stringTemplateLoader);
        // 将模板语言转换为模板文件
        Template template = configuration.getTemplate("template", "utf-8");
        //静态化
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, body);
    }

    //页面发布
    public ResponseResult postPage(String pageId) throws Exception {
        //执行静态化
        String pageHtml = this.getPageHtmlByPageId(pageId);
        if (StringUtils.isEmpty(pageHtml)) {
            log.error("页面静态化失败");
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //保存静态化文件
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        //发送消息
        sendPostPage(cmsPage.getPageId());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 发送mq 消息
     *
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("pageId", pageId);
        //消息内容
        String msg = JSON.toJSONString(msgMap);
        //获取站点id作为routingKey
        String siteId = cmsPage.getSiteId();
        //发布消息
        rabbitTemplate.convertAndSend(RabbitMqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, msg);
    }

    //保存静态页面内容
    private CmsPage saveHtml(String pageId, String content) {
        //查询页面
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //存储之前先删除
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotBlank(htmlFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        //保存html文件到GridFS
        InputStream inputStream = IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        //文件id
        String fileId = objectId.toString();
        //将文件id存储到cmspage中
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
     * 保存cms page
     *
     * @author guoxing
     * @date 2019-10-18 11:45 AM
     * @since 2.0.0
     **/
    public CmsPageResult save(CmsPage cmsPage) {
        if (cmsPage == null) {
            return new CmsPageResult(CommonCode.INVALID_PARAM, cmsPage);
        }
        // 根据唯一数据查询 数据 是否存在
        CmsPage byId = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (byId != null) {
            return this.update(byId.getPageId(), cmsPage);
        } else {
            return this.add(cmsPage);
        }
    }

    /**
     * 快速发布课程接口
     *
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPageResult save = this.save(cmsPage);
        if (save == null || !save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        cmsPage = save.getCmsPage();
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String pageId = cmsPage.getPageId();
        // 执行页面发布
        try {
            ResponseResult responseResult = this.postPage(pageId);
            if (responseResult != null && responseResult.isSuccess()) {
                String siteId = cmsPage.getSiteId();
                if(StringUtils.isBlank(siteId)){
                    ExceptionCast.cast(CommonCode.FAIL);
                }
                CmsSite cmsSite = cmsSiteRepository.findById(siteId).orElse(null);
                if(cmsSite == null){
                    ExceptionCast.cast(CommonCode.FAIL);
                }
                //站点域名
                String siteDomain = cmsSite.getSiteDomain();
                 //站点web路径
                String siteWebPath = cmsSite.getSiteWebPath();
                //页面web路径
                String pageWebPath = cmsPage.getPageWebPath();
                //页面名称
                String pageName = cmsPage.getPageName();
                if(StringUtils.isBlank(siteDomain)||StringUtils.isBlank(siteWebPath)||StringUtils.isBlank(pageWebPath)||StringUtils.isBlank(pageName)){
                    ExceptionCast.cast(CommonCode.FAIL);
                }
                //页面的web访问地址
                String pageUrl = siteDomain+siteWebPath+pageWebPath+pageName;
                return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
            } else {
                ExceptionCast.cast(CommonCode.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return null;
    }
}

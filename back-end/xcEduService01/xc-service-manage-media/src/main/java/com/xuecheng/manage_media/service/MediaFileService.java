package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * MediaFileService
 *
 * @author guoxing
 * @date 10/21/2019 1:43 PM
 * @since 2.0.0
 **/
@Service
public class MediaFileService {
    @Autowired
    private MediaFileRepository mediaFileRepository;

    /**
     * 媒体文件 列表 分页查询
     *
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    public QueryResponseResult findList(Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 20;
        }
        if (queryMediaFileRequest == null) {
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        // 条件值对象
        MediaFile mediaFile = new MediaFile();
        // 查询条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        String tag = queryMediaFileRequest.getTag();
        if (StringUtils.isNotBlank(tag)) {
            exampleMatcher.withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains());
            mediaFile.setTag(tag);
        }
        String fileOriginalName = queryMediaFileRequest.getFileOriginalName();
        if (StringUtils.isNotBlank(fileOriginalName)) {
            exampleMatcher.withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains());
            mediaFile.setFileOriginalName(fileOriginalName);
        }
        String processStatus = queryMediaFileRequest.getProcessStatus();
        if (StringUtils.isNotBlank(processStatus)) {
            // 精确匹配(默认,可以不设置 )
            exampleMatcher.withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact());
            mediaFile.setProcessStatus(processStatus);
        }
        // 组装查询条件
        Example<MediaFile> mediaFileExample = Example.of(mediaFile, exampleMatcher);
        // 分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(mediaFileExample, PageRequest.of(page - 1, size));
        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<>();
        mediaFileQueryResult.setTotal(all.getTotalElements());
        mediaFileQueryResult.setList(all.getContent());
        return new QueryResponseResult<>(CommonCode.SUCCESS, mediaFileQueryResult);
    }
}

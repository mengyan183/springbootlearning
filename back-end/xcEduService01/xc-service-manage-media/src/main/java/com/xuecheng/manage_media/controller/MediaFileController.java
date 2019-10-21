package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaFileControllerApi;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MediaFileController
 *
 * @author guoxing
 * @date 10/21/2019 11:09 AM
 * @since 2.0.0
 **/
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {
    @Autowired
    private MediaFileService mediaFileService;

    /**
     * 媒体文件 列表 分页查询
     *
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    @Override
    @GetMapping("/findList/{page}/{size}/")
    public QueryResponseResult findList(@PathVariable("page") Integer page, @PathVariable("size") Integer size, QueryMediaFileRequest queryMediaFileRequest) {
        //媒资文件查询
        return mediaFileService.findList(page, size, queryMediaFileRequest);
    }
}

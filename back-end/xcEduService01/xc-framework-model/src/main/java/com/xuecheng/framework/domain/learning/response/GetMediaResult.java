package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * GetMediaResult
 * 获取媒资文件播放地址 返回值 定义
 *
 * @author guoxing
 * @date 10/22/2019 9:54 AM
 * @since 2.0.0
 **/
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {
    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }

    //媒资文件播放地址
    private String fileUrl;
}
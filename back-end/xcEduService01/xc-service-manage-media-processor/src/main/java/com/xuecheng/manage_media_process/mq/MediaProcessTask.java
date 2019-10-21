package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.config.SystemConfig;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class MediaProcessTask {

    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private SystemConfig systemConfig;
    private static final String SYSTEM = System.getProperty("os.name");

    /**
     * mq 监听 视频处理队列
     *
     * @param msg
     */
    // queues 注入指定的监听队列, containerFactory 设置 自定义监听工厂
    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}", containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) throws FileNotFoundException {
        File file;
        if (SYSTEM.contains("Windows")) {
            file = ResourceUtils.getFile("classpath:ffmpeg.exe");
        } else if (SYSTEM.contains("Mac")) {
            file = ResourceUtils.getFile("classpath:ffmpeg");
        } else {
            // TODO linux
            file = ResourceUtils.getFile("classpath:ffmpeg");
        }
        String ffmpeg_path =file.getAbsolutePath();
        String serverPath = systemConfig.getUploadVideoLocation();
        Map msgMap = JSON.parseObject(msg, Map.class);
        log.info("receive media process task msg :{} ", msgMap);

        //媒资文件id
        String mediaId = (String) msgMap.get("mediaId");
        //获取媒资文件信息
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            return;
        }
        MediaFile mediaFile = optional.get();
        //媒资文件类型
        String fileType = mediaFile.getFileType();
        if (fileType == null || !fileType.equals("avi")) {//目前只处理avi文件
            mediaFile.setProcessStatus("303004");            //处理状态为无需处理
            mediaFileRepository.save(mediaFile);
            return;
        } else {
            mediaFile.setProcessStatus("303001");            //处理状态为未处理
            mediaFileRepository.save(mediaFile);
        }
        //生成mp4
        String video_path = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        String mp4_name = mediaFile.getFileId() + ".mp4";
        String mp4folder_path = serverPath + mediaFile.getFilePath();
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        String result = videoUtil.generateMp4();
        if (StringUtils.isBlank(result) || !result.equals("success")) {
            //操作失败写入处理日志
            mediaFile.setProcessStatus("303003");
            //处理状态为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //生成m3u8
        video_path = serverPath + mediaFile.getFilePath() + mp4_name;//此地址为mp4的地址
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        String m3u8folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path, video_path, m3u8_name, m3u8folder_path);
        result = hlsVideoUtil.generateM3u8();
        if (StringUtils.isBlank(result) || !result.equals("success")) {
            //操作失败写入处理日志
            mediaFile.setProcessStatus("303003");//处理状态为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //获取m3u8列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        //更新处理状态为成功
        mediaFile.setProcessStatus("303002");//处理状态为处理成功
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //m3u8文件url
        mediaFile.setFileUrl(mediaFile.getFilePath() + "hls/" + m3u8_name);
        mediaFileRepository.save(mediaFile);
    }

}

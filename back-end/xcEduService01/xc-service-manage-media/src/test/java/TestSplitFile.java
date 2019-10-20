import com.xuecheng.manage_media.ManageMediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

@SpringBootTest(classes = {ManageMediaApplication.class})
@RunWith(SpringRunner.class)
public class TestSplitFile {


    /**
     * 分割文件
     *
     * @throws IOException
     */
    @Test
    public void split() throws IOException {
        File sourceFile = new File("/Users/xingguo/Downloads/阶段一-微服务课程/学成在线/day13 在线学习 HLS/资料/lucene.avi");
        String chunkPath = "/Users/xingguo/Downloads/阶段一-微服务课程/学成在线/day13 在线学习 HLS/资料/chunk/";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        //分块大小
        long chunkSize = 1024 * 1024 * 1;
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if (chunkNum <= 0) {
            chunkNum = 1;
        }
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r"); //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件中写数据
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                    if (file.length() > chunkSize) {
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }

    /**
     * 合并文件
     */
    @Test
    public void merge() throws IOException {
        File mergeFile = new File("/Users/xingguo/Downloads/阶段一-微服务课程/学成在线/day13 在线学习 HLS/资料/lucene-merge.avi");
        String chunkPath = "/Users/xingguo/Downloads/阶段一-微服务课程/学成在线/day13 在线学习 HLS/资料/chunk/";
        File chunkFolder = new File(chunkPath);
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
//创建新的合并文件
        mergeFile.createNewFile();
//用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw"); //指针指向文件顶端
        raf_write.seek(0);
//缓冲区
        byte[] b = new byte[1024];
//分块列表
        File[] fileArray = chunkFolder.listFiles();
// 转成集合，便于排序
        List<File> fileList = new ArrayList<>(Arrays.asList(fileArray)); // 从小到大排序
        fileList.sort((o1, o2) -> {
            if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                return -1;
            }
            return 1;
        });
//合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "rw");
            int len;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}

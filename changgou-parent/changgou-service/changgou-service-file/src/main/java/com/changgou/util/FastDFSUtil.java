package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 实现FastDFS文件管理
 *    文件上传
 *    文件删除
 *    文件下载
 *    文件信息获取
 *    Storage信息获取
 *    Tracker信息获取
 */
public class FastDFSUtil {
    /**
     * 加载Tracker链接信息
     */
    static {
        try {
            //查找classpath下的文件路径
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            //加载Tracker链接信息
            ClientGlobal.init(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param fastDFSFile :上传的文件信息封装
     * @return
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception {

        //附加参数
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0]=new NameValuePair("author",fastDFSFile.getAuthor());

        //创建一个Tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务，获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer的链接信息可以获取Storage的链接信息，创建StorageClient对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer,null);

        /**
         * 通过StorageClient访问Storage实现文件上传，并且获取文件上传后的存储信息
         * 1.上传文件的字节数组
         * 2.文件的扩展名
         * 3.附加参数
         */
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);

        return uploads;

    }
}

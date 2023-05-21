package com.wansir.media.utils;


import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.wansir.base.exception.MyException;
import com.wansir.base.model.RestResponse;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/21 12:34
 */
@Component
@Slf4j
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    //获取文件默认存储目录路径 年/月/日
    public String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/") + "/";
        return folder;
    }

    //获取文件的md5
    public String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //得到分块文件的目录，类似于： 9 / 7 / 9732315c4805e042b445cac7594bb50f / chunk / 1
    public String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    //构建合并后文件的路径，类似于：
    public String getFilePathByMd5(String fileMd5,String fileName){

        String extention = fileName.substring(fileName.lastIndexOf("."));
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 + extention;
    }


    //获取文件类型
    public String getMimeType(String extension) {
        if (extension == null)
            extension = "";
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    public boolean existFile(String bucket, String filePath) {
        GetObjectArgs build = GetObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build();
        //文件流
        InputStream stream = null;
        try {
            //查找
            stream = minioClient.getObject(build);
        } catch (Exception e) {
            MyException.cast("minio查找文件错误！");
        }
        return stream == null ? false : true;

    }

    /**
     * 查找minio，判断分块文件是否存在
     * @param chunkFilePath 分块文件的地址
     * @param bucket_videos bucket
     * @return
     */
    public boolean existChunk(String chunkFilePath, String bucket_videos) {
        //文件流
        InputStream fileInputStream = null;
        try {
            fileInputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket_videos)
                            .object(chunkFilePath)
                            .build());
        } catch (Exception e) {
            log.error("分块文件不存在：" + chunkFilePath);
        }
        return fileInputStream == null ? false : true;
    }


    /**
     * 上传文件
     * @param localFilePath 本地文件的位置
     * @param mimeType 文件类型
     * @param bucket bucket
     * @param objectName 上传文件的地址
     * @return
     */
    public boolean uploadFiles(String localFilePath, String mimeType
                                , String bucket, String objectName) {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(localFilePath)
                    .contentType(mimeType)
                    .build();
            minioClient.uploadObject(testbucket);
            log.debug("上传文件到minio成功,bucket:{},objectName:{}", bucket, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}", bucket, objectName, e.getMessage(), e);
            MyException.cast("上传文件到文件系统失败");
        }
        return false;

    }

    /**
     * 合并分块文件
     * @param mergeFilePath 合并后文件地址
     * @param chunkTotal 分块总数
     * @param bucket bucket
     * @param chunkFileFolderPath 分块文件的目录
     * @return
     */

    public boolean mergeChunks(String mergeFilePath, int chunkTotal, String bucket, String chunkFileFolderPath){
        //构建ComposeSource
        List<ComposeSource> sourceObjectList = Stream.iterate(0, i -> ++i)
                .limit(chunkTotal)
                .map(i -> ComposeSource.builder()
                        .bucket(bucket)
                        .object(chunkFileFolderPath.concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

        //合并文件
        ObjectWriteResponse response = null;
        try {
            response = minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucket)
                            .object(mergeFilePath)
                            .sources(sourceObjectList)
                            .build());
            log.debug("合并文件成功:{}",mergeFilePath);
            return true;
        } catch (Exception e) {
            log.debug("合并文件失败,fileMd5:{},异常:{}",mergeFilePath,e.getMessage(),e);
        }
        return false;
    }


    public File downloadFile(String bucket,String objectName){
        //临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try{
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            //创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream,outputStream);
            log.debug("下载合并后文件成功,mergeFilePath:{}",objectName);
            return minioFile;
        } catch (Exception e) {
            log.debug("下载合并后文件失败,mergeFilePath:{}",objectName);
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 清除分块文件
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal 分块文件总数
     */
    public void clearChunkFiles(String chunkFileFolderPath, int chunkTotal, String bucket){

        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i))))
                    .collect(Collectors.toList());

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucket).objects(deleteObjects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            results.forEach(r->{
                DeleteError deleteError = null;
                try {
                    deleteError = r.get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("清楚分块文件失败,objectname:{}",deleteError.objectName(),e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清楚分块文件失败,chunkFileFolderPath:{}",chunkFileFolderPath,e);
        }
    }






}

package com.kjz.www.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.InputStream;

public class OSSUtils {

    /**
     * 创建客户端
     * @return
     */
    public OSSClient createCilent(){
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 临时账号accessKeyId/accessKeySecret/securityToken
        String accessKeyId = "LTAI2DpQkd7qSzmW";
        String accessKeySecret = "TopGf3mZW9uiiVgnTBbHdjkyTgQ7Te";
        // 创建OSSClient实例
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        return client;
    }

    public void closeCilent(OSSClient ossClient){
        // 关闭ossClient
        ossClient.shutdown();
    }

    /**
     * 上传文件
     * @return
     */
    public String upload(String fileName,InputStream inputStream, OSSClient ossClient){
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // 指定要上传到 COS 上的路径
        String url="";
        try {
            String key = "ArticlePhoto/"+fileName;
            // 上传文件流
            ossClient.putObject("kjz-article-photo", key, inputStream);
            url="http://kjz-article-photo.oss-cn-beijing.aliyuncs.com/"+key;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}

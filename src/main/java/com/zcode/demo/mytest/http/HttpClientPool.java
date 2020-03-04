package com.zcode.demo.mytest.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

/**
 * 连接池的方式使用HttpClient
 */
public class HttpClientPool {

    public static void main(String[] args) {
        HttpClientPool clientPool = new HttpClientPool();
        clientPool.getImage();
    }

    /**
     * 获取网络图片
     */
    private void getImage() {

        // 连接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(5);

        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(5000)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        List<HttpGet> httpList = Arrays.asList(
                new HttpGet("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583346468501&di=1518eff0735a9dfe90c3c05956f8f6d3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201407%2F13%2F20140713194220_J4Fus.thumb.700_0.jpeg"),
                new HttpGet("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583346652394&di=fccf78d856311197ab01b578489beb87&imgtype=0&src=http%3A%2F%2Fimg.show160.com%2FUpload%2Fimages%2F201811%2F636785997541829552.jpg")
        );

        /**
         * 遍历，同步执行请求
         */

        for (int i = 1; i <= 20; i++) {
            int urlIndex = i % 2;
            final int no = i;
            try {
                // 执行请求
                Long contentLength = httpClient.execute(httpList.get(urlIndex), new ResponseHandler<Long>() {
                    @Override
                    public Long handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        // 网络图片写入到本地
                        writeImage(httpEntity.getContent(), no);
                        return httpEntity.getContentLength();
                    }
                });
                System.out.println("第" + i + "个文件长度：" + contentLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入本地
     * @param inStream
     * @param fileNo
     */
    private void writeImage(InputStream inStream, int fileNo) {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    new StringBuffer().append("d:/MyTestData/hcp-").append(fileNo).append(".jpg").toString());

            FileChannel fileChannel = fileOutputStream.getChannel();

            byte[] bytes = new byte[1024];
            int length;
            while ((length = inStream.read(bytes)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, length);
                fileChannel.write(byteBuffer);
            }

            //关闭输入流
            inStream.close();

            //关闭文件
            fileChannel.close();
            fileOutputStream.close();

            System.out.println("------------文件写入完成：" + fileNo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

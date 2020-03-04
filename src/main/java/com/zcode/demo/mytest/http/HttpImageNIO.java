package com.zcode.demo.mytest.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * NIO 异步下载图片
 * @author zhouwb
 * @since 2020/2/20
 */
public class HttpImageNIO {

    public static void main(String[] args) {

        HttpImageNIO httpImageNIO = new HttpImageNIO();
        httpImageNIO.getImage();

    }

    /**
     * 从互联网下载图片
     */
    public void getImage() {

        // IO线程配置
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();

        // 设置连接池
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        // 连接池管理器
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connectionManager.setMaxTotal(20);   // 最大连接数
        connectionManager.setDefaultMaxPerRoute(20);

        // http请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build();

        // http请求
        HttpGet httpGet = new HttpGet(
                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1583246731&di=6e536b2ec856c1bcae78463586fee117&src=http://n.sinaimg.cn/sinacn16/740/w1440h900/20180416/c681-fzcyxmv4458667.jpg");

        // 创建httpClient
        final CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        httpClient.start();

        CountDownLatch countDownLatch = new CountDownLatch(20);

        // 发送多个请求
        for (int i = 0; i < 20; i++) {
            httpClient.execute(httpGet, new CallBack(i, countDownLatch));
        }

        // 等待完成
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("------------------下载完成");

    }

    static class CallBack implements FutureCallback<HttpResponse> {

        private Integer no;
        private CountDownLatch countDownLatch;

        public CallBack(final int no, final CountDownLatch countDownLatch) {
            this.no = no;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void completed(HttpResponse httpResponse) {

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            System.out.println("---------------------------------【得到一张】 " + " 图片编号: " + no  + " threadId:" + Thread.currentThread().getId());
            try {
                writeImage(entity.getContent());
                System.out.println("---------------------------------【存完一张】 图片编号: " + no + " threadId:" + Thread.currentThread().getId());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.countDownLatch.countDown();
            }
        }

        @Override
        public void failed(Exception e) {
            e.printStackTrace();
            this.countDownLatch.countDown();
        }

        @Override
        public void cancelled() {
            this.countDownLatch.countDown();
        }

        /**
         * 下载的图片写入到本地
         *
         * @param inStream
         */
        private void writeImage(InputStream inStream) {

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(
                        new StringBuffer().append("d:/MyTestData/").append(no).append(".jpg").toString());
                byte[] bytes = new byte[1024];
                int length = -1;
                while ((length = inStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // 故意阻塞5秒
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}

package com.zcode.demo.mytest.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhouwb
 * @since 2020/2/20
 */
public class HttpImage {

    public static void main(String[] args) {

        HttpImage httpImage = new HttpImage();
        httpImage.getImage();

    }

    public void getImage() {

        HttpClient httpClient = HttpClientBuilder.create()
                .build();
        HttpGet httpGet = new HttpGet(
                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1583246731&di=6e536b2ec856c1bcae78463586fee117&src=http://n.sinaimg.cn/sinacn16/740/w1440h900/20180416/c681-fzcyxmv4458667.jpg");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            System.out.println("length:" + entity.getContentLength());

            writeImage(entity.getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeImage(InputStream inStream) {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    "d:/1.png");
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

    }

}

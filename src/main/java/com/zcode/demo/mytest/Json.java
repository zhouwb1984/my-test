package com.zcode.demo.mytest;

import com.alibaba.fastjson.JSON;

/**
 * @author zhouwb
 * @since 2019/1/26
 */
public class Json {

    public static void main(String[] args) {

        UserModel user = new UserModel();
        user.setKey("123");

        String s = JSON.toJSONString(user);

        System.out.println(s);

    }

}

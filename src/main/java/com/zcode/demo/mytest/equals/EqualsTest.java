package com.zcode.demo.mytest.equals;

/**
 * @author zhouwb
 * @since 2019/11/28
 */
public class EqualsTest {

    public static void main(String[] args) {

        EqualsTest test = new EqualsTest();
        test.test1();

    }

    /**
     * ==，用来比较两个引用指向的地址是否相同
     * 知识点：常量池
     */
    private void test1() {

        String s1 = "123";
        String s2 = "123";

        //true
        System.out.println("s1==s2:" + (s1 == s2));

        String s3 = new String("123");
        //false
        System.out.println("s1==s3:" + (s1 == s3));

        Integer i1 = 123;
        int i2 = 123;
        // true
        System.out.println("i1==i2:" + (i1 == i2));

        Integer i3 = new Integer(123);
        // true
        System.out.println("i2==i3:" + (i2 == i3));

        Integer i4 = new Integer(123);
        //false
        System.out.println("i3 == i4:" + (i3 == i4));

        Integer ii1 = 128;
        int ii2 = 128;
        Integer ii3 = new Integer(128);
        int ii4 = 128;
        Integer ii5 = new Integer(128);
        Integer ii6 = 128;

        // true
        System.out.println("ii1 == ii2:" + (ii1 == ii2));
        // true
        System.out.println("ii2 == ii4:" + (ii2 == ii4));
        // true
        System.out.println("ii2 == ii3:" + (ii2 == ii3));
        // false Integer的常量池范围：-128~127
        System.out.println("ii3 == ii5:" + (ii3 == ii5));
        // false
        System.out.println("ii1 == ii6:" + (ii1 == ii6));
    }


}

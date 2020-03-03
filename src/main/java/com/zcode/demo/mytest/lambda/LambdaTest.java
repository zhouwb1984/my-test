package com.zcode.demo.mytest.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongToDoubleFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author zhouwb
 * @since 2019/11/26
 */
public class LambdaTest {

    private Integer sumSoldier = 0;

    public static void main(String[] args) {
        LambdaTest test = new LambdaTest();
//        test.testPredicate();
//        test.testPredicate2();
//        test.testConsumeForInstanceVariable();
//        test.testConsumeForMethodVariable();
//        test.testFunction();
//        test.testFunction2();
        test.testFunctionChain();
    }

    /**
     * 断言
     */
    private void testPredicate() {
        List<Integer> filtedList = filter(
                Arrays.asList(10, 20, 5, 4, 3, 2, 1),
                (item) -> item % 2 == 0);
        filtedList.forEach(i -> System.out.println(i));
    }

    /**
     * 断言
     */
    private void testPredicate2() {

        Predicate<Integer> predicate = (i) -> i % 2 == 0;
        predicate = predicate
                .or((i) -> i == 1)
                .and((i) -> i == 5)
                .negate();

        List<Integer> list = filter(
                Arrays.asList(10, 20, 5, 4, 3, 2, 1),
                predicate);
        list.forEach(i -> System.out.println(i));
    }

    /**
     * 断言
     * @param list
     * @param predicate
     * @return
     */
    private List<Integer> filter(
            List<Integer> list, Predicate<Integer> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    private void testConsumeForMethodVariable() {
        AtomicInteger sum = new AtomicInteger(0);
        consume(
                Arrays.asList(10, 10, 20, 30),
                i -> {
                    sum.addAndGet(i);
                });
        System.out.println("sum = " + sum.get());
    }

    private void testConsumeForInstanceVariable() {
        consume(
                Arrays.asList(10, 10, 20, 30),
                i -> sumSoldier += i);
        System.out.println("sum = " + sumSoldier);
    }

    private void testConsume3() {

        Arrays.asList(10, 10, 20, 30).forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });

        Arrays.asList(10, 10, 20, 30).forEach(item -> System.out.println(item));

    }

    private void consume(List<Integer> list, Consumer<Integer> consumer) {
        list.forEach(consumer);
    }

    /**
     * Supplier 用于创建对象，对象提供者
     */
    private void testSupplier() {
        supplier(
                Arrays.asList(10, 10, 20, 30),
                () -> 10);
    }

    private void supplier(List<Integer> list, Supplier<Integer> supplier) {
        list.forEach(i -> {
            Integer r = i * supplier.get();
            System.out.println(r);
        });
    }

    private void testSupplier2() {
        //构造函数引用
        Supplier<String> ss = String::new;
        List<String> list = new ArrayList<>();
        for (int i=0; i<10; i++) {
            list.add(ss.get());
        }
    }

    /**
     * Function -> 传入一个参数，返回一个参数
     */
    private void testFunction() {

        Function<Integer, String> function = String::valueOf;

        List<String> list = new ArrayList<>();
        for (int i=0; i<10; i++) {
            list.add(function.apply(i));
        }

        Function<Double, String> function1 = (d) -> String.valueOf(d);

        for (int i=0; i<10; i++) {
            list.add(function1.apply(i+0.2d));
        }

        list.forEach(s -> {
            System.out.println(s);
        });
    }

    /**
     * 基本类型转换
     * 不用new Integer(1)转化，高效
     */
    private void testFunction2() {

        LongToDoubleFunction ldFunction = (l) -> l * 0.2d;

        Arrays.asList(10L,20L,30L).forEach(i -> {
            double dv = ldFunction.applyAsDouble(i);
            System.out.println(dv);
        });

    }

    /**
     * function
     */
    private void testFunctionChain() {

        Function<Integer, String> function1 = String::valueOf;
        Function<Double, Integer> function2 = (d) -> d.intValue();
        Function<String, Double> function3 = Double::new;

        // double -> integer -> string
        String s = function1.compose(function2).apply(5d);
        System.out.println(s);

        // integer -> string -> double
        double d = function1.andThen(function3).apply(5);
        System.out.println(d);
    }

    private void testCompare() {

        Arrays.asList(10L,20L,30L).stream().sorted(
                Comparator.comparingLong(Long::longValue).reversed());

    }

    private void testCompare2() {

        Arrays.asList(10L,20L,30L).stream()
                .sorted((v1, v2) -> v1.compareTo(v2));

    }

    private void testCompare3() {
        Arrays.asList(10L,20L,30L).stream().sorted(
                new Comparator<Long>() {
                    @Override
                    public int compare(Long o1, Long o2) {
                        return o1.compareTo(o2);
                    }
                }
        );
    }

}

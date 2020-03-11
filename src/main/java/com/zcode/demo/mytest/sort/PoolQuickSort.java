package com.zcode.demo.mytest.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 快速排序，多线程
 */
public class PoolQuickSort {

    private List<Integer> members = Collections.synchronizedList(Arrays.asList(
            49, 38, 60, 97, 76, 13, 27, 49
    ));

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    /**
     * 启动排序
     */
    public void start() {
        sort(0, members.size()-1);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        executor.shutdown();
    }

    /**
     * 快速排序
     * @param low 低位元素下标
     * @param high 高位元素下标
     */
    private void sort(int low, int high) {

        System.out.println("thread-id:" + Thread.currentThread().getId());

        if (low < high) {

            // 对数组进行划分，划分出基准位置
            int pivotPosition = partition(low, high);

            executor.execute(() -> {
                // 对基准位置左边的数组进行排序
                sort(low, pivotPosition - 1);
            });

            executor.execute(() -> {
                // 对基准位置右边的数组进行排序
                sort(pivotPosition + 1, high);
            });
        }
    }

    /**
     * 对数组进行划分，划分出基准位置
     * @param low 数组的低位元素下标（位置索引）
     * @param high 数组的高位元素下标（位置索引）
     * @return 分隔基准位置
     */
    private int partition(int low, int high) {

        // 把第1个元素作为划分的基准位置
        int pivotMember = members.get(low);

        while (low < high) {

            while (low < high && members.get(high) >= pivotMember) {
                // 从高位（右侧）向低位遍历，找到比基准元素小的元素
                high--;
            }
            if (low < high) {
                // 把右侧比基准元素小的元素放到左侧,low下标向右侧移动一位
                members.set(low++, members.get(high));
            }

            while (low < high && members.get(low) <= pivotMember) {
                // 从低位（左侧）向高位遍历，找到比基准元素大的元素
                low++;
            }
            if (low < high) {
                // 把左侧比基准元素大的元素放到右侧，high下标向左移动一位
                members.set(high--, members.get(low));
            }

        }

        // 基准元素移到分隔位置
        members.set(low, pivotMember);

        // 返回分隔基准位置
        return low;
    }

    public void print() {
        for (int m : members) {
            System.out.print(m + " ");
        }
    }

    public static void main(String[] args) {
        PoolQuickSort quickSort = new PoolQuickSort();
        quickSort.start();
        quickSort.print();
    }
}

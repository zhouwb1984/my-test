package com.zcode.demo.mytest.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 快速排序，fork/join
 */
public class ForkJoinQuickSort {

    private List<Integer> members = Collections.synchronizedList(Arrays.asList(
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49
    ));

    /**
     * 快速排序任务
     */
    private class QuickSortTask extends RecursiveAction {

        private int low;
        private int high;

        /**
         * 快速排序任务
         * @param low 数组的低位元素下标（位置索引）
         * @param high 数组的高位元素下标（位置索引）
         */
        public QuickSortTask(int low, int high) {
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {

            System.out.println("thread-id:" + Thread.currentThread().getId());

            if (low < high) {

                // 对数组进行划分，划分出基准位置
                int pivotPosition = partition(low, high);

                // 对基准位置左边的数组进行排序
                QuickSortTask leftSort = new QuickSortTask(low, pivotPosition - 1);

                // 对基准位置右边的数组进行排序
                QuickSortTask rightSort = new QuickSortTask(pivotPosition + 1, high);

                /*
                    执行任务。
                    看源码，当前线程执行一个任务，另一个任务加入到队列等待执行
                 */
                invokeAll(leftSort, rightSort);
            }
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

    /**
     * 启动排序
     */
    public void start() {

        // 线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 创建排序任务
        QuickSortTask sortTask = new QuickSortTask(0, members.size()-1);

        /*
            执行排序任务。
            内部调用了task.join()方法，同步等待任务执行完成，返回结果。
            这是与submit(task)方法的区别，submit不会同步等待执行完成，将task放到任务队列就OK了，要手动调用join方法等待返回结果。
         */
        forkJoinPool.invoke(sortTask);

        // 关闭线程池
        forkJoinPool.shutdown();

        print();
    }

    /**
     * print
     */
    public void print() {
        for (int m : members) {
            System.out.print(m + " ");
        }
    }

    public static void main(String[] args) {
        ForkJoinQuickSort quickSort = new ForkJoinQuickSort();
        quickSort.start();
    }

}

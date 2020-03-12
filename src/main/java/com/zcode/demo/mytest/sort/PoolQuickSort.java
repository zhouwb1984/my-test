package com.zcode.demo.mytest.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 快速排序，多线程
 */
public class PoolQuickSort {

    private List<Integer> members = Collections.synchronizedList(Arrays.asList(
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49,
            49, 38, 60, 97, 76, 13, 27, 49, 78, 19, 20, 43, 90, 100, 41, 4, 3, 3, 31, 46, 109, 95, 98, 87, 54,43, 49
    ));

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /**
     * 快速排序任务
     */
    private class SortTask implements Callable<Boolean> {

        // 数组低位、高位索引
        private int low, high;

        public SortTask(int low, int high) {
            this.low = low;
            this.high = high;
        }

        @Override
        public Boolean call() {

            System.out.printf("thread-id:%d, isDaemon:%d, low:%d, high:%d \n",
                    Thread.currentThread().getId(),
                    Thread.currentThread().isDaemon() ? 1 : 0,
                    low, high);

            if (low < high) {

                // 对数组进行划分，划分出基准位置
                int pivotPosition = partition(low, high);

                // 对基准位置左边的数组进行排序
                SortTask leftSortTask = new SortTask(low, pivotPosition - 1);

                // 对基准位置右边的数组进行排序
                SortTask rightSortTask = new SortTask(pivotPosition + 1, high);

                try {
                    executor.invokeAll(Arrays.asList(leftSortTask, rightSortTask));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return true;
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

        // 创建排序任务，执行
        SortTask task = new SortTask(0, members.size()-1);
        Future future = executor.submit(task);

        try {
            // 等待任务执行完毕
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.printf("pool size: %d \n", executor.getPoolSize());

        // 关闭线程池
        executor.shutdown();

        print();
    }

    public void print() {
        for (int m : members) {
            System.out.print(m + " ");
        }
    }

    public static void main(String[] args) {
        PoolQuickSort quickSort = new PoolQuickSort();
        quickSort.start();
    }
}

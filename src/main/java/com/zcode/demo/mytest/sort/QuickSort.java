package com.zcode.demo.mytest.sort;

/**
 * 快速排序
 */
public class QuickSort {

    private int[] members = new int[] {
            49, 38, 60, 97, 76, 13, 27, 49
    };

    /**
     * 启动排序
     */
    public void start() {
        sort(0, members.length-1);
    }

    /**
     * 快速排序
     * @param low 低位元素下标
     * @param high 高位元素下标
     */
    private void sort(int low, int high) {
        if (low < high) {
            // 对数组进行划分，划分出基准位置
            int pivotPosition = partition(low, high);
            // 对基准位置左边的数组进行排序
            sort(low, pivotPosition - 1);
            // 对基准位置右边的数组进行排序
            sort(pivotPosition + 1, high);
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
        int pivotMember = members[low];

        while (low < high) {

            while (low < high && members[high] >= pivotMember) {
                // 从高位（右侧）向低位遍历，找到比基准元素小的元素
                high--;
            }
            if (low < high) {
                // 把右侧比基准元素小的元素放到左侧,low下标向右侧移动一位
                members[low++] = members[high];
            }

            while (low < high && members[low] <= pivotMember) {
                // 从低位（左侧）向高位遍历，找到比基准元素大的元素
                low++;
            }
            if (low < high) {
                // 把左侧比基准元素大的元素放到右侧，high下标向左移动一位
                members[high--] = members[low];
            }

        }

        // 基准元素移到分隔位置
        members[low] = pivotMember;

        // 返回分隔基准位置
        return low;
    }

    public void print() {
        for (int m : members) {
            System.out.print(m + " ");
        }
    }

    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        quickSort.start();
        quickSort.print();
    }

}

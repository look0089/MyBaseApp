package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClass {

    public static void main(String[] args) {
//        int[] intArray = {13, 2, 33, 42, 35, 1, 31, 7, 9, 3};
//        initMap();
//        createXData();
        System.out.println("a");
        asycFunc();
        System.out.println("c");
    }

    private static void asycFunc() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        System.out.println("b");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createXData() {
        List<EleEntity> mEleList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 12; j++) {
                EleEntity eleEntity = new EleEntity();
                String createTime = "";
                if (i < 10) {
                    createTime = "0" + i + ":";
                } else {
                    createTime = i + ":";
                }
                if (j < 2) {
                    eleEntity.create_time = createTime + "0" + j * 5;
                } else {
                    eleEntity.create_time = createTime + j * 5;
                }
                mEleList.add(eleEntity);
            }
        }
        System.out.println(mEleList.toString());
    }

    private static void choose() {
        int random = (int) (Math.random() * 100);
        if (10 > random && random > 0) {
            System.out.println(random + "");
        }
        if (10 > random && random > 0) {
            System.out.println(random + "");
        }
        if (10 > random && random > 0) {
            System.out.println(random + "");
        }
        if (10 > random && random > 0) {
            System.out.println(random + "");
        }
        if (10 > random && random > 0) {
            System.out.println(random + "");
        }
    }

    public static void initMap() {
        List<HourDataEntity> xhour = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 6; j++) {
                HourDataEntity hourDataEntity = new HourDataEntity();
                if (i < 11) {
                    hourDataEntity.HourNum = "0" + i + ":" + j + 0;
                }
                hourDataEntity.HourNum = i + ":" + j + 0;
                xhour.add(hourDataEntity);
            }
        }
        System.out.println(xhour.toString());

        for (int i = 0; i < xhour.size() / 6; i++) {


        }
    }

    public static class EleEntity {
        public String Pac;
        public String create_time;

        public String toString() {
            return create_time;
        }
    }

    public static class HourDataEntity {
        /**
         * HourNum : 0:0
         * HourPower : 0
         */

        public String HourNum;
        public String HourPower;

        public String hourCount;
        public String hourPowerCount;

        @Override
        public String toString() {
            return HourNum;
        }
    }


    public static void getLeft() {
        float screenWidth = 1080;
        float left = 400;
        System.out.println("" + left / screenWidth);


    }

    /*
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    System.out.println("比较数字j:" + numbers[j] + ",j+1:" + numbers[j + 1]);
                    System.out.println("原数组:" + Arrays.toString(numbers));
                    //交换两数位置
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    System.out.println("交换后:" + Arrays.toString(numbers));
                }
            }
        }
    }


    /*
     *快速排序
     */

    /**
     * 查找出中轴（默认是最低位low）的在numbers数组排序后所在位置
     *
     * @param numbers 带查找数组
     * @param low     开始位置
     * @param high    结束位置
     * @return 中轴所在位置
     */
    public static int getMiddle(int[] numbers, int low, int high) {
//        int[] intArray = {13, 2, 33, 42, 35, 1, 31, 7, 9, 3};
        int temp = numbers[low]; //数组的第一个作为中轴
        while (low < high) {
            while (low < high && numbers[high] > temp) {
                high--;
            }
            numbers[low] = numbers[high];//比中轴小的记录移到低端
            while (low < high && numbers[low] < temp) {
                low++;
            }
            numbers[high] = numbers[low]; //比中轴大的记录移到高端
        }
        numbers[low] = temp; //中轴记录到尾
        return low; // 返回中轴的位置
    }

    /**
     * @param numbers 带排序数组
     * @param low     开始位置
     * @param high    结束位置
     */
    public static void quickSort(int[] numbers, int low, int high) {
        if (low < high) {
            int middle = getMiddle(numbers, low, high); //将numbers数组进行一分为二
            System.out.println("中位数：" + middle);
            quickSort(numbers, low, middle - 1);   //对低字段表进行递归排序
            quickSort(numbers, middle + 1, high); //对高字段表进行递归排序
        }

    }

    /**
     * 快速排序
     *
     * @param numbers 带排序数组
     */
    public static void quick(int[] numbers) {
        if (numbers.length > 0)   //查看数组是否为空
        {
            quickSort(numbers, 0, numbers.length - 1);
        }
    }

    public static void countDown(int i) {
        System.out.println("" + i);
        if (i < 0) {
            return;
        } else {
            countDown(i - 1);
        }
    }
}

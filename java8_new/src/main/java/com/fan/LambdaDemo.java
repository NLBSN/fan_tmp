package com.fan;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description: fan_tmp    lambda表达式示例
 * @author: fan
 * @Date: Created in 2019/2/25 11:22
 * @Modified By:
 */
public class LambdaDemo {
    /**
     * @description 原来的java写法    创建线程
     */
    @Test
    public void test1_0() {
        Thread td = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello,,,");
            }
        });
        td.start();
    }

    /**
     * @description lambda写法
     */
    @Test
    public void test1_1() {
        Thread thread = new Thread(() -> System.out.println("hello,lambda!"));
        thread.start();
    }

    /**
     * @description 排序
     */
    @Test
    public void test2_0() {
        List<String> list = Arrays.asList(new String[]{"b", "c", "a"});
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });
    }

    /**
     * @description 排序 lambda写法
     */
    @Test
    public void test2_1() {
        List<String> list = Arrays.asList(new String[]{"b", "c", "a"});
        Collections.sort(list, (str1, str2) -> str1.compareTo(str2));
        // Collections.sort(list, String::compareTo);           //或者可以这样
        // 下面的代码都是进行将集合中的元素进行遍历的几种方法
        // 方法1：最原始的代码
        for (String str : list) {
            System.out.println(str);
        }
        // 方法2：最原始的lambda表达式
        list.forEach((a) -> { System.out.println(a); });

        System.out.println("----------------分隔符1----------------");
        // 方法3：进化一步的lambda表达式
        list.forEach(LambdaDemo::printValur);
        System.out.println("----------------分隔符2----------------");
        //下面的方法和上面等价的
        // 方法4：java8新的function函数
        Consumer<String> methodParam = LambdaDemo::printValur; //方法参数
        list.forEach(x -> methodParam.accept(x));//方法执行accept
        list.forEach(methodParam::accept);

    }

    public static void printValur(String str) {
        System.out.println("print value : " + str);
    }
}

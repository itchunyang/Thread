package com.itchunyang.basic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luchunyang on 2016/11/3.
 *
 * AtomicInteger，一个提供原子操作的Integer的类。在Java语言中，++i和i++操作并不是线程安全的，
 * 在使用的时候，不可避免的会用到synchronized关键字。而AtomicInteger则通过一种线程安全的加减操作接口
 */
public class AtomicIntegerDemo {

    public static void main(String[] args) {
        AtomicInteger integer = new AtomicInteger(1);
        System.out.println("当前值:"+integer.get());//1

        //取当前的值，并设置新的值
        System.out.println("当前值:"+integer.getAndSet(11));//1

        //获取当前的值，并自增
        System.out.println("当前值:"+integer.getAndIncrement());//11
        System.out.println("当前值:"+integer.getAndDecrement());//12
        System.out.println(integer.get());//11

        integer.getAndAdd(20);
        System.out.println(integer.get());

    }
}

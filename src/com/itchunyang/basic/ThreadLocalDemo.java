package com.itchunyang.basic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luchunyang on 2016/11/3.
 *
 * ThreadLocal是为了使每个线程保存一份属于自己的数据。
 * ThreadLocal是一个线程内部的数据存储类。通过使用ThreadLocal，能够让同一个数据对象在不同的线程中存在多个副本，而这些副本互不影响
 */
public class ThreadLocalDemo {

    static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {

        //三个线程使用的是threadLocal的副本（克隆品）
//        new MyThread().start();
//        new MyThread().start();
//        new MyThread().start();

        //这个山寨版的ThreadLocal也同样运行很好，实现了JavaAPI中ThreadLocal的功能。
        testMyThreadLocal();
    }

    private static void testMyThreadLocal() {

        MyThreadLocal<Integer> local = new MyThreadLocal<Integer>(){
            @Override
            public Integer initialValue() {
                return 0;
            }
        };

        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        local.set(local.get() + 1);
                        System.out.println(Thread.currentThread().getName()+"\t"+local.get());
                    }
                }
            };
            t.start();
        }
    }

    static class MyThread extends Thread{

        @Override
        public void run() {

            for (int i = 0; i < 5; i++) {
                threadLocal.set(threadLocal.get() + 1);
                System.out.println(Thread.currentThread().getName()+"\t"+threadLocal.get());
            }
        }
    }

    static class MyThreadLocal<T>{
        private Map<Thread,T> map = Collections.synchronizedMap(new HashMap<Thread, T>());

        public T initialValue(){
            return null;
        }

        public T get(){
            Thread t = Thread.currentThread();
            T obj = map.get(t);
            if(obj == null && !map.containsKey(t)){
                obj = initialValue();
                map.put(t,obj);
            }
            return obj;
        }

        public void set(T value){
            map.put(Thread.currentThread(),value);
        }

        public void remove(){
            map.remove(Thread.currentThread());
        }
    }
}

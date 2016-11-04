package com.itchunyang.sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luchunyang on 2016/11/3.
 *
 * Lock对应Synchronized，使用之前都要先获取锁
 * 				 Object       Condition
 * 休眠           wait         await
 * 唤醒个线程      notify       signal
 * 唤醒所有线程    notifyAll    signalAll
 */
public class ConditionDemo {

    public static void main(String[] args) {
        final IBuffer buffer = new IBuffer(10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 40; i++) {
                    buffer.put(""+i);
                    System.out.println("[put] "+i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Object o = buffer.take();
                    System.out.println("<take> "+o.toString());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    //ArrayBlockingQueue 的内部实现
    static class IBuffer{
        final Lock lock = new ReentrantLock(false);
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();
        final Object[] items ;//缓存队列
        int putPtr/*写索引*/,takePtr/*读索引*/,count/*缓存个数*/;

        public IBuffer(int size) {
            items = new Object[size];
        }

        public void put(Object o){
            lock.lock();

            try {
                while(count == items.length)
                    notFull.await(); //阻塞写线程

                items[putPtr] = o;

                if(++putPtr == items.length)
                    putPtr = 0;

                ++count;

                notEmpty.signalAll();//唤醒读线程

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }

        public Object take(){
            lock.lock();

            try {
                while(count == 0)
                    notEmpty.await();

                Object o = items[takePtr];
                if(++takePtr == items.length)//如果读索引读到队列的最后一个位置了，那么置为0
                    takePtr = 0;

                --count;

                notFull.signalAll();
                return o;
            }catch (Exception e){
                System.out.println("-------->");
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

            return "";
        }
    }
}

package com.itchunyang.sync;

/**
 * Created by luchunyang on 2016/11/2.
 */
public class SyncThreadDemo {

    public static void main(String[] args) {

        TickSell tickSell = new TickSell();
        new Thread(tickSell).start();
        new Thread(tickSell).start();
        new Thread(tickSell).start();


//        SyncObject syncObject = new SyncObject();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("getDelayedMessage="+syncObject.getDelayedMessage());
//            }
//        }).start();
//
//        //阻塞
//        System.out.println("getMessage="+syncObject.getMessage());


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("getDelyedDetail="+SyncObject.getDelayedDetail());
//            }
//        }).start();
//
//        //阻塞
//        System.out.println("getDetail="+SyncObject.getDetail());

    }

    static class TickSell implements Runnable {
        private int tick = 100;

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    if (tick > 0) {
                        System.out.println(Thread.currentThread().getName() + " sail -> " + (tick--));
                        try {
                            Thread.sleep(100);//保持对象锁，让出CPU
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("卖完票了");
                        break;
                    }
                }
            }
        }
    }
}

class SyncObject {

    public synchronized String getDelayedMessage(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "message";
    }

    public synchronized String getMessage(){
        return "message";
    }

    public synchronized static String getDelayedDetail(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "getDelayedDetail";
    }

    public synchronized static String getDetail(){
        return "getDetail";
    }
}

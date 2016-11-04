package com.itchunyang.sync;

/**
 * Created by luchunyang on 2016/11/2.
 */
public class ErrorSellDemo {

    public static void main(String[] args) {
        errorSell();
    }

    private static void errorSell() {
        //这种方法可能会造成同一时间卖出相同的票
        TickSell tickSell = new TickSell();
        new Thread(tickSell).start();
        new Thread(tickSell).start();
        new Thread(tickSell).start();

    }

    static class TickSell implements Runnable{

        private int tick = 100;

        @Override
        public void run() {
            while( tick > 0){
                System.out.println(Thread.currentThread().getName()+" sail -> " + (tick--));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

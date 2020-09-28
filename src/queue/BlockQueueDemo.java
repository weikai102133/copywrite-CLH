package queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        BlockQueue blockQueue = new BlockQueue(20);
        ExecutorService works = Executors.newFixedThreadPool(10);
        AtomicInteger data = new AtomicInteger(0);
        CountDownLatch latch  = new CountDownLatch(1);
        for(int i = 0;i < 5;i++){
            works.submit(()->{
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"write begin"+System.currentTimeMillis());
                for(int j = 0;j < 2000000;j++) {
                    blockQueue.add(data.getAndIncrement());
                }
                System.out.println(Thread.currentThread().getName()+"write end"+System.currentTimeMillis());
            });
        }
        for (int i = 0;i < 5;i++){
            works.submit(()->{
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"read begin"+System.currentTimeMillis());
                try {
                    for(int j = 0;j < 2000000 ;j++) {
                        blockQueue.get();
                        //System.out.println(blockQueue.get());
                    }
                    System.out.println(Thread.currentThread().getName()+"read end"+System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        latch.countDown();
        TimeUnit.MILLISECONDS.sleep(10000);

        for(int i = 0;i < blockQueue.getQueue().length;i++){
            //  System.out.println("element of "+i+"is "+blockQueue.getQueue()[i]);
        }
        works.shutdown();
    }
}

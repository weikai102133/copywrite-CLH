package queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CopyWriteQueueDemo {
    public static void main(String[] args) {
        CopyWriteQueue copyWriteQueue = new CopyWriteQueue(20);
        ExecutorService works = Executors.newFixedThreadPool(10);
        AtomicInteger data = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        Object[] array = new Object[100];

        for (int i=0;i < array.length;i++){
            array[i] = data.getAndIncrement();
        }
        //System.out.println(array[9]);

        works.submit(()->{
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("write begin"+System.currentTimeMillis());
            copyWriteQueue.addArray(array);
           /* for(int i = 0;i < 100;i++){
                copyWriteQueue.add(data.getAndIncrement());
            }*/
            //System.out.println("write end"+System.currentTimeMillis());
        });

        for(int i = 0;i< 5;i++){
            works.submit(()->{
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(Thread.currentThread().getName()+"read begin"+System.currentTimeMillis());
                for (int j = 0;j < 2;j++){
                    try {
                        copyWriteQueue.getWithFlag();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // System.out.println(Thread.currentThread().getName()+"read end"+System.currentTimeMillis());
            });
        }

        latch.countDown();
        works.shutdown();
    }
}

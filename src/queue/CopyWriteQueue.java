package queue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CopyWriteQueue {
    private volatile Object[] queue;
    private int capacity;
    private volatile AtomicInteger head = new AtomicInteger(0);
    private volatile AtomicInteger flag = new AtomicInteger(0);
    private volatile int tail = 0;
    private Object getLock = new Object();
    private Object addLock = new Object();
    private CLH clh = new CLH();

    public CopyWriteQueue(int capacity){
        this.capacity = capacity;
        queue = new Object[this.capacity];
    }

    //单值写入
    public boolean add(Object o){
        if (tail >= (int) (queue.length * 0.8)) {
            reSize(queue);
        }
        Object[] copy = new Object[queue.length];
        System.arraycopy(queue, 0, copy, 0, queue.length);
        copy[tail++] = o;
        queue = copy;
        flag.getAndIncrement();
        return true;
    }

    //数组形式写入
    public boolean addArray(Object[] o){
        int size = o.length;
        while(queue.length-tail < size){
            reSize(queue);
        }
        Object[] copy = new Object[queue.length];
        System.arraycopy(queue,0,copy,0,tail);
        System.arraycopy(o,0,copy,tail,o.length);
        tail += o.length;
        flag.set(flag.get()+o.length);
        queue = copy;
        //System.out.println("flag is "+flag.get());
        //System.out.println(tail+"is"+queue[tail-1]);
        return true;
    }

    //分段锁
    public Object get() throws InterruptedException {
        while(true){
            if(queue[head.get()] == null) {
                TimeUnit.MILLISECONDS.sleep(10);
                continue;
            }
            synchronized (getLock) {
                if(queue[head.get()] != null) {
                    //System.out.println(queue[head]);
                    return queue[head.getAndIncrement()];
                }else {
                    continue;
                }
            }
        }
    }

    //无锁，写完后读
    public Object getWithoutLock() throws InterruptedException {
        while (true){
            if(queue[head.get()] == null){
                TimeUnit.MILLISECONDS.sleep(10);
                continue;
            }else {
                //System.out.println(queue[head.get()]);
                return queue[head.getAndIncrement()];
            }
        }
    }

    //有标志位读，不符合标志位条件线程返回null
    public Object getWithFlag() throws InterruptedException {
        while (true){
            if(queue[head.get()] == null){
                TimeUnit.MILLISECONDS.sleep(10);
                continue;
            }
            clh.lock();
            // synchronized (getLock){
            if (flag.get() < 0) return null;
            flag.getAndDecrement();
            System.out.println(queue[head.get()]);
            System.out.println("flag is " + flag.get());
            // }
            clh.unlock();
            return queue[head.getAndIncrement()];
        }
    }

    public void reSize(Object[] o){
        int size = o.length*2;
        Object[] doubleQueue = new Object[size];
        System.arraycopy(o,0,doubleQueue,0,o.length);
        queue = doubleQueue;
    }
}

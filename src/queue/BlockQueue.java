package queue;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.TimeUnit;

public class BlockQueue {
    private Object[] queue;
    private int capacity;
    private volatile int tail = 0;
    private volatile int head = 0;
    private Object lock = new Object();
    private Object addLock = new Object();
    private Object getLock = new Object();
    private Object element = new Object();

    public BlockQueue(int capacity){
        this.capacity = capacity;
        queue = new Object[capacity];
    }

    public Object[] getQueue(){
        return this.queue;
    }

    public boolean add(Object o) {
        synchronized(addLock) {
            if (tail >= (int) (queue.length * 0.8)) {
                reSize(queue);
            }
            queue[tail++] = o;
            //System.out.println(Thread.currentThread().getName() + "this is " + (int) o);
            return true;
        }
    }

    public  Object get() throws InterruptedException {
        while (true) {
            if (tail == head) {
                TimeUnit.MILLISECONDS.sleep(10);
                continue;
            }
            synchronized (getLock) {
                if (head < tail) {
                    element = queue[head];
                    queue[head] = null;
                    head++;
                    //System.out.println(element);
                    return element;
                }else {
                    continue;
                }
            }
        }

    }

    private void reSize(@NotNull Object[] o){
        int size = o.length*2;
        Object[] doubleQueue = new Object[size];
        System.arraycopy(o,0,doubleQueue,0,o.length);
        /*for(int i = 0;i < size/2;i++){
            doubleQueue[i] = o[i];
        }*/
        queue = doubleQueue;
    }

}

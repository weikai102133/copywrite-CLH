package queue;

import java.util.concurrent.TimeUnit;

public class NotifyQueue {
    static class Node {
        Node prev;
        Node next;
        Thread thread;
        Node nextWaiter;
        static Node EXCLUSIVE = null;

        Node predecessor() throws NullPointerException {
             Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {
        }

        Node(Thread thread, Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }
    }

    private volatile  Node head;
    private volatile  Node tail;
    private volatile int state = 0;

    private Object lock = new Object();
    private Object unlock = new Object();


    private  Node enq( Node node){
        for (;;){
             Node t = tail;
            if(t == null){
                head = new Node();
                tail = head;
            }else {
                node.prev = head;
                head.next = node;
                tail = node;
                return t;
            }
        }
    }

    //添加至队尾
    private Node addWaiter(Node mode){
         Node node = new Node(Thread.currentThread(),mode);
         Node pred = tail;
        if(pred != null){
            node.prev = tail;
            tail.next = node;
            tail = node;
            return node;
        }
        enq(node);
        return node;
    }

    //设置队头
    private void setHead(Node node){
        head = node;
        //线程没意义了，因为该线程已经获取到锁
        node.thread = null;
        //前一个节点已经没有意义了
        node.prev = null;
    }

    public void lock(){
        synchronized (lock) {
            Node node = addWaiter(Node.EXCLUSIVE);
            if (node.predecessor() == head) {
                System.out.println("head pass");
                return;
            }
            while(true) {
                try {
                    System.out.println(Thread.currentThread().getName() + " wait");
                    Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    if (node.predecessor() == head) {
                        System.out.println("break");
                        break;
                    } else {
                        System.out.println("no head");
                    }
                } finally {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }
    }

    public void unlock() {
        synchronized (lock) {
            if (head.next != null) {
                System.out.println("wakeup");
                head.next.thread.interrupt();
                head = head.next;
            }
        }
    }
}

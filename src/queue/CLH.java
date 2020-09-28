package queue;

public class CLH {
      static class Node{
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
        Node(){}

        Node(Thread thread, Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }

    }

    private volatile Node head;
    private volatile Node tail;
    private volatile int state = 0;

    private Object lock = new Object();


    private Node enq(Node node){
        for (;;){
            Node t = tail;
            if(t == null){
                head = new Node();
                tail = head;
            }else {
                node.prev = t;
                t.next = node;
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

    //前驱为head且state为0时将当前节点设置为头结点
    final boolean acquireQueue(final Node node){
        for (; ; ) {
            final Node p = node.predecessor();
            if (p == head && state == 0) {
                state = 1;
                setHead(node);
                p.next = null;
                return true;
            }
        }
    }


    public void lock(){
        synchronized (lock) {
            if (state == 0) { //当前无线程使用资源，尝试独占锁
                state = 1;
                return;
            }
            //调用addWaiter方法为当前线程创建一个节点node，并插入队列中
            Node node = addWaiter(Node.EXCLUSIVE);
            acquireQueue(node);
        }
    }

    //释放锁
    public void unlock(){
        state = 0;
    }

}

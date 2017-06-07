import com.tiger.uitl.MemoryUtils;
import org.junit.Test;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 17-5-17.
 */
public class TestThread {

    @Test
    public void test() throws InterruptedException {
        Num num = new Num();
        Thread threadA = new Thread(new RunAdd(num));
        Thread threadB = new Thread(new RunAdd(num));
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        num.print();
    }

    @Test
    public void testRunBlockingPut() throws InterruptedException {
        Blocking b = new Blocking();
        Thread threadA = new Thread(new RunBlockingPut(b));
        Thread threadB = new Thread(new RunBlockingTake(b));
        threadA.start();
        TimeUnit.SECONDS.sleep(2);
        threadB.start();
        threadA.join();
        threadB.join();
    }

}

class RunAdd implements Runnable {
    private Num num;

    public RunAdd(Num num) {
        this.num = num;
    }

    @Override
    public void run() {
        for (int i = 0; i < 800000; i++) {
            num.incr();
//            try {
//                long count_offset = VM.current().fieldOffset(Num.class.getDeclaredField("count"));
//                MemoryUtils.UNSAFE.getAndAddInt(num,count_offset,1);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
        }
    }
}

class RunBlockingPut implements Runnable {
    private Blocking blocking;

    public RunBlockingPut(Blocking blocking) {
        this.blocking = blocking;
    }

    @Override
    public void run() {
        for (int i = 0; i < 800000; i++) {
            try {
                blocking.push(i);
                blocking.print();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class RunBlockingTake implements Runnable {
    private Blocking blocking;

    public RunBlockingTake(Blocking blocking) {
        this.blocking = blocking;
    }

    @Override
    public void run() {
        for (;;) {
            try {
                blocking.take();
                blocking.print();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Blocking{

    private int[] arr = new int[20];
    private int putIndex = 0;
    private int count = 0;
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmtpy = lock.newCondition();

    public void print(){

        for(int i:arr){
            System.out.printf("%d \t",i);
        }

        System.out.println();

    }

    public void push(int i) throws InterruptedException {
        lock.lock();
        try {
            while (count == 1){
                System.out.printf("%s wait to put \n",Thread.currentThread().getName());
                notFull.await();
            }
            arr[count] = i;
            count ++;
            notEmtpy.signal();
        }finally {
            lock.unlock();
        }

    }

    public int take() throws InterruptedException {
        lock.lock();
        int res;
        try {
            while (count == 0){
                System.out.printf("%s wait to take \n",Thread.currentThread().getName());
                notEmtpy.await();
            }
            res = arr[count];
            count --;
            notFull.signal();
        }finally {
            lock.unlock();
        }
        return res;
    }

}

class Num {
    public int count = 0;

    public Thread lockThread = null;

    private static long lock_offset;

    static {
        try {
            lock_offset = VM.current().fieldOffset(Num.class.getDeclaredField("lockThread"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void incr() {
        while (!MemoryUtils.UNSAFE.compareAndSwapObject(this, lock_offset, null, Thread.currentThread())) {
        }
        count++;
        MemoryUtils.UNSAFE.compareAndSwapObject(this, lock_offset, Thread.currentThread(), null);
    }

    public void print() {
        System.out.println(count);
    }

}
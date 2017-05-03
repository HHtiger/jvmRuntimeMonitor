import com.tiger.uitl.MemoryUnit;
import com.tiger.uitl.MemoryUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by root on 17-4-30.
 */
public class TestHeap {

    public static void main(String[] args) throws InterruptedException {

        int step_M = 20;

//        try {
//            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//            System.out.println("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize());
//            System.out.println("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize());
//            long a = MemoryUtils.UNSAFE.allocateMemory(osmb.getFreePhysicalMemorySize()/2);
//            MemoryUtils.UNSAFE.setMemory(a, osmb.getFreePhysicalMemorySize()/2, (byte) 0);
//            MemoryUtils.UNSAFE.freeMemory(a);
//
//        } catch (OutOfMemoryError x) {
//            throw x;
//        }
        long a = MemoryUtils.UNSAFE.allocateMemory(4 * 4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                MemoryUtils.UNSAFE.putByte(a + i * 4 + j, (byte) (i * 4 + j));
            }

        }

        for (int i = 0; i < 16; i++) {
            System.out.printf("%d \t",MemoryUtils.UNSAFE.getByte(a + i));
            if((i+1)%4==0) System.out.println();
        }

        System.out.println(MemoryUtils.UNSAFE.getByte(a+4*(4-1)+(2-1)));

        MemoryUtils.UNSAFE.freeMemory(a);

        System.out.printf("maxMemory %d M \n", Runtime.getRuntime().maxMemory() / MemoryUnit._1M);
        System.out.printf("totalMemory %d M\n", Runtime.getRuntime().totalMemory() / MemoryUnit._1M);
        System.out.printf("freeMemory %d M\n", Runtime.getRuntime().freeMemory() / MemoryUnit._1M);
        TimeUnit.SECONDS.sleep(1);


    }
}

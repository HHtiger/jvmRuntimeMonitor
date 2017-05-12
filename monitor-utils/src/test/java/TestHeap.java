import com.sun.management.OperatingSystemMXBean;
import com.tiger.uitl.MemoryUnit;
import com.tiger.uitl.MemoryUtils;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;

/**
 * Created by root on 17-4-30.
 */
public class TestHeap {

    @Test
    public void TestString() {
        String s = "as";
        System.out.printf("s addr : %d \n", VM.current().addressOf(s));

        String s1 = "a" + "s";
        System.out.printf("s1 addr : %d \n", VM.current().addressOf(s1));
    }

    @Test
    public void testOperatingSystem() {
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize());
        System.out.println("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize());
        long a = MemoryUtils.UNSAFE.allocateMemory(osmb.getFreePhysicalMemorySize() / 2);
        MemoryUtils.UNSAFE.setMemory(a, osmb.getFreePhysicalMemorySize() / 2, (byte) 0);
        MemoryUtils.UNSAFE.freeMemory(a);
    }

    @Test
    public void TestArray() {
        long a = MemoryUtils.UNSAFE.allocateMemory(4 * 4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                MemoryUtils.UNSAFE.putByte(a + i * 4 + j, (byte) (i * 4 + j));
            }
        }

        for (int i = 0; i < 16; i++) {
            System.out.printf("%d \t", MemoryUtils.UNSAFE.getByte(a + i));
            if ((i + 1) % 4 == 0) System.out.println();
        }

        System.out.println(MemoryUtils.UNSAFE.getByte(a + 4 * (4 - 1) + (2 - 1)));
        MemoryUtils.UNSAFE.freeMemory(a);
        System.out.println(MemoryUtils.UNSAFE.getByte(a + 4 * (4 - 1) + (2 - 1)));
    }

    @Test
    public void testJVMMem() {
        System.out.printf("maxMemory %d M \n", Runtime.getRuntime().maxMemory() / MemoryUnit._1M);
        System.out.printf("totalMemory %d M\n", Runtime.getRuntime().totalMemory() / MemoryUnit._1M);
        System.out.printf("freeMemory %d M\n", Runtime.getRuntime().freeMemory() / MemoryUnit._1M);
    }

    @Test
    public void testObject() throws NoSuchFieldException, UnsupportedEncodingException {

        A o = new A();
        o.a = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        System.out.println(ClassLayout.parseInstance(o.a).toPrintable());

        long lo = VM.current().addressOf(o);
        long lo_a = VM.current().addressOf(o.a);
        System.out.printf("%d \n", lo);
        System.out.printf("%d \n", lo_a);

        long string_value_offset = VM.current().fieldOffset(String.class.getDeclaredField("value"));

        System.out.println(string_value_offset);
        System.out.println(MemoryUtils.UNSAFE.getByte(lo_a + string_value_offset));

    }
}

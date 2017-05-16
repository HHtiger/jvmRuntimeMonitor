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
    public void testArray() {

        char c = 'c';
        System.out.println(VM.current().sizeOf(c));
        System.out.println(ClassLayout.parseInstance(c).toPrintable());

        char[] s = new char[]{'a', 'b', 'c', 'd'};
        long address_of_s = VM.current().addressOf(s);
        long array_offset = MemoryUtils.UNSAFE.arrayBaseOffset(s.getClass());

        System.out.println(ClassLayout.parseInstance(s).toPrintable());

        System.out.printf("address of s is %d \n", address_of_s);

        int step = MemoryUtils.UNSAFE.arrayIndexScale(char[].class);

        System.out.println(MemoryUtils.UNSAFE.getChar(address_of_s + array_offset + 0 * step));
        System.out.println(MemoryUtils.UNSAFE.getChar(address_of_s + array_offset + 1 * step));
        System.out.println(MemoryUtils.UNSAFE.getChar(address_of_s + array_offset + 2 * step));

    }


    @Test
    public void testString() {
        String s = "as";
        System.out.printf("s addr : %d \n", VM.current().addressOf(s));

        String s1 = "a" + "s";
        System.out.printf("s1 addr : %d \n", VM.current().addressOf(s1));
    }

    @Test
    public void testString2() throws NoSuchFieldException, UnsupportedEncodingException {

        A o = new A();

        long addressOfA = VM.current().addressOf(o);
        System.out.printf("address of o is %d \n", addressOfA);
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        long addressOfA_a = VM.current().addressOf(o.a);
        System.out.printf("address of o.a is %d \n", addressOfA_a);
        System.out.println(ClassLayout.parseInstance(o.a).toPrintable());

        long addressOfA_b = VM.current().addressOf(o.b);
        System.out.printf("address of o.b is %d \n", addressOfA_b);
        System.out.println(ClassLayout.parseInstance(o.b).toPrintable());

        long A_a_offset = VM.current().fieldOffset(A.class.getDeclaredField("a"));
        System.out.printf("A_a_offset offset is %d \n", A_a_offset);

        long A_b_offset = VM.current().fieldOffset(A.class.getDeclaredField("b"));
        System.out.printf("A_b_offset offset is %d \n", A_b_offset);

        System.out.println(MemoryUtils.UNSAFE.getObject(o,A_a_offset));

        String c = "ccc";

        MemoryUtils.UNSAFE.putObject(o,A_a_offset,c);

        System.out.println(o.a);


    }

}

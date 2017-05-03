package com.tiger;

import com.tiger.mem.TigerRuntime;
import com.tiger.uitl.MemoryUnit;
import com.tiger.uitl.RuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;


public class NIOClient {

    static Logger logger = LoggerFactory.getLogger(NIOClient.class);

    public static void main(String[] args) throws IOException {

        SocketChannel clntChan = SocketChannel.open();
        clntChan.configureBlocking(false);
        if (!clntChan.connect(new InetSocketAddress("localhost", 10083))) {
            while (!clntChan.finishConnect()) {
                logger.info(".");
            }
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    byte[] obj = new byte[100*MemoryUnit._1M];
                    logger.info("new 100M.");

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        t1.start();

        //分别实例化用来读写的缓冲区
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    TigerRuntime tr = new TigerRuntime(
                            RuntimeUtil.IP192(),
                            RuntimeUtil.pid(),
                            Runtime.getRuntime().maxMemory() / MemoryUnit._1M,
                            Runtime.getRuntime().freeMemory() / MemoryUnit._1M,
                            Runtime.getRuntime().totalMemory() / MemoryUnit._1M
                    );

                    try {

                        clntChan.write(ByteBufferUitl.getByteBuffer(tr));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        t2.start();

    }

}
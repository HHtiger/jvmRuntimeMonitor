package com.tiger;

import com.tiger.mem.TigerRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class EchoSelectorProtocol implements TCPProtocol {

    private static Logger logger = LoggerFactory.getLogger(EchoSelectorProtocol.class);


    private int bufSize; // 缓冲区的长度
    public EchoSelectorProtocol(int bufSize){
        this.bufSize = bufSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        logger.info("Accept");
        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));



    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {

        SocketChannel clientChannel=(SocketChannel)key.channel();

        // 得到并清空缓冲区
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();

        // 读取信息获得读取的字节数
        long bytesRead=clientChannel.read(buffer);

        if(bytesRead==-1){
            // 没有读取到内容的情况
            clientChannel.close();
        }
        else{
            // 将缓冲区准备为数据传出状态
            buffer.flip();
            try {
                TigerRuntime tr = (TigerRuntime) ByteBufferUitl.getObject(buffer);
                logger.info("{}",tr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // 控制台打印出来
//            logger.info("接收到来自"+clientChannel.socket().getRemoteSocketAddress()+"的信息:"+ByteBufferUitl.decode(buffer));
//
//            // 准备发送的文本
//            String sendString="你好,客户端. @"+new Date().toString()+"，已经收到你的信息"+ByteBufferUitl.decode(buffer);
//            clientChannel.write(ByteBufferUitl.encode(sendString));

            // 设置为下一次读取或是写入做准备
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub
        ByteBuffer buffer=(ByteBuffer) key.attachment();
        buffer.flip();
        SocketChannel clntChan = (SocketChannel) key.channel();
        //将数据写入到信道中
        clntChan.write(buffer);
        if (!buffer.hasRemaining()){

            //如果缓冲区中的数据已经全部写入了信道，则将该信道感兴趣的操作设置为可读
            key.interestOps(SelectionKey.OP_READ);
        }
        //为读入更多的数据腾出空间
        buffer.compact();

    }

}

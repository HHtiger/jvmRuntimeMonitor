package com.tiger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by root on 17-4-14.
 */
public class ByteBufferUitl {

    private static Charset charset = Charset.forName("UTF-16");

    //对buffer进行解码
    public static String decode(ByteBuffer buffer) {
        CharBuffer charset = ByteBufferUitl.charset.decode(buffer); //将buffer转为utf-8格式的charbuffer
        return charset.toString();
    }

    //对String进行编码
    public static ByteBuffer encode(String str){
        return ByteBufferUitl.charset.encode(str);
    }

    public static byte[] getBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }

    public static ByteBuffer getByteBuffer(Serializable obj) throws IOException {
        byte[] bytes = getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }

    public static Object getObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
        InputStream input = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream oi = new ObjectInputStream(input);
        Object obj = oi.readObject();
        input.close();
        oi.close();
        byteBuffer.clear();
        return obj;
    }

}

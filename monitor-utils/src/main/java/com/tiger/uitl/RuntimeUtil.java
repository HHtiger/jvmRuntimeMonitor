package com.tiger.uitl;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by root on 17-4-14.
 */
public class RuntimeUtil {

    public static String pid(){
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static InetAddress localHostInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String IP192(){
        return IPUtil.getLocalIPList().stream().filter(s->s.startsWith("192")).findFirst().orElse("");
    }

}

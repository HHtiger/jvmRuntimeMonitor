package com.tiger.jni;

/**
 * Created by jiax on 2016/12/30.
 */
public class HelloNative {
    static {
        System.loadLibrary( "HelloNative" );
    }

    public static native void greeting();

    /*
    * -Djava.library.path=/root/IdeaProjects/JvmRuntimeMonitor/monitor-utils/src/main/java/com/tiger/jni
    * */
    public static void main(String[] args) {
        greeting();
    }
}
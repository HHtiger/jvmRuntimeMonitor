package com.tiger.mem;

import java.io.Serializable;

/**
 * Created by root on 17-4-14.
 */
public class TigerRuntime implements Serializable{

    private static final long serialVersionUID = 1L;

    private String ip;

    private String pid ;

    private long maxMemory;

    private long freeMemory;

    private long totalMemory;

    public TigerRuntime(String ip,String pid, long maxMemory, long freeMemory, long totalMemory) {
        this.ip = ip;
        this.pid = pid;
        this.maxMemory = maxMemory;
        this.freeMemory = freeMemory;
        this.totalMemory = totalMemory;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    @Override
    public String toString() {
        return "TigerRuntime{" +
                "ip='" + ip + '\'' +
                "pid='" + pid + '\'' +
                ", maxMemory=" + maxMemory +
                ", freeMemory=" + freeMemory +
                ", totalMemory=" + totalMemory +
                '}';
    }
}

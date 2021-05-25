package com.wushi.scheduler.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * traceId生成器
 *
 * @author yulianghua
 * @date 2020/1/3 5:32 PM
 * @description
 */
class TraceIdGenerator {
    private static final int START = 1000;
    private static final int END = 9000;

    public static String create() throws UnknownHostException {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            long decimal = ipToLong(ip);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            int number = new Random().nextInt(END - START + 1) + START;
            return Long.toHexString(decimal) + ":" + date + ":" + number;
        } catch (UnknownHostException e) {
            throw e;
        }
    }

    private static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return result;
    }

    private static String longToIp2(long ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }
}

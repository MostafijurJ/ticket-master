package com.learn.ms.payment.core.service;

import org.springframework.stereotype.Component;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

@Component
public class UniqueIdGenerationService {
    private static final int NODE_ID_BITS = 9;
    private static final int SEQUENCE_BITS = 12;
    private static final int MAX_NODE_ID = (int) (Math.pow(2, NODE_ID_BITS) - 1);
    private static final int MAX_SEQUENCE = (int) (Math.pow(2, SEQUENCE_BITS) - 1);
    private static final long CUSTOM_EPOCH = 1420070400000L;
    private final int nodeId;
    private volatile long sequence = 0L;
    private volatile long lastTimestamp = -1L;

    public UniqueIdGenerationService() {
        this.nodeId = createNodeId();
    }

    public synchronized String generateUniqueIdForTransaction(String idPrefix) {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp)
            throw new IllegalStateException("Bad System Clock!");

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0)
                currentTimestamp = waitNextMillis(currentTimestamp);

        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        long id = currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS);
        id |= ((long) nodeId << SEQUENCE_BITS);
        id |= sequence;
        return idPrefix.concat(String.valueOf(id));
    }

    public synchronized String generateSequentialId() {
        long currentTimestamp = timestamp();
        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Bad System Clock!");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = currentTimestamp;
        return String.valueOf(lastTimestamp);
    }

    private int createNodeId() {
        int newNodeId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (byte b : mac) {
                        sb.append(String.format("%02X", b));
                    }
                }
            }
            newNodeId = sb.toString().hashCode();
        } catch (Exception ex) {
            newNodeId = (new SecureRandom().nextInt());
        }
        newNodeId = newNodeId & MAX_NODE_ID;
        return newNodeId;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }

}

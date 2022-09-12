package com.young.xjyq.util;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 11:00
 */
public class SnowFlakeUtils {

    // 默认服务器ID参数
    static {
        machineId = 12L;
        machineRoomId = 7L;
    }

    //因为二进制里第一个 bit 为如果是 1，那么都是负数，但是我们生成的 id 都是正数，所以第一个 bit 统一都是 0

    ////////////////////////////////////////////////////////////
    ////                      起始时间戳                      ////
    ////////////////////////////////////////////////////////////
    /**
     * 开始时间截 (2020-10-29)
     */
    private static final long START_TIME = 1603900800000L;

    /////////////////////////////////////////////////////////
    ////                      机器ID                      ////
    /////////////////////////////////////////////////////////
    /**
     * 机器ID  63个（有一个不能使用）
     */
    private static long machineId;

    /**
     * 5位的机器id
     */
    private static final long MACHINE_ID_BITS = 5L;


    /////////////////////////////////////////////////////////
    ////                      机房ID                      ////
    /////////////////////////////////////////////////////////
    /**
     * 机房ID  63个（一个不能使用）
     */
    private static long machineRoomId;

    /**
     * 5位的机房id
     */
    private static final long MACHINE_ROOM_ID_BITS = 5L;


    /////////////////////////////////////////////////////////
    ////                      时间ID                      ////
    /////////////////////////////////////////////////////////
    /**
     * 代表一毫秒内生成的多个id的最新序号  12位 4096 - 1 个
     */
    private static long sequence = 0L;

    /**
     * 每毫秒内产生的id数 2 的 12次方
     */
    private static final long SEQUENCE_BITS = 12L;


    ///////////////////////////////////////////////////////////
    ////                      服务器ID                      ////
    ///////////////////////////////////////////////////////////
    /**
     * 机器Id最大可用Id
     */
    private static final long MAX_USABLE_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);


    /**
     * 机房Id最大可用Id
     */
    private static final long MAX_USABLE_MACHINE_ROOM_ID = ~(-1L << MACHINE_ROOM_ID_BITS);


    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long MACHINE_ROOM_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long TIME_STAMP_LEFT_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS + MACHINE_ROOM_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);


    /////////////////////////////////////////////////////////////////
    ////                      最近一次生产时间                      ////
    /////////////////////////////////////////////////////////////////
    /**
     * 记录产生时间毫秒数，判断是否是同1毫秒
     */
    private static long lastTimestamp = -1L;


    public SnowFlakeUtils(long machineId, long machineRoomId) {
        // 检查机房id和机器id是否超过其最大值 不能小于0
        if (machineId > MAX_USABLE_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException(
                    String.format("机器ID 不能 > %d 或 < 0", MAX_USABLE_MACHINE_ID));
        }

        if (machineRoomId > MAX_USABLE_MACHINE_ROOM_ID || machineRoomId < 0) {
            throw new IllegalArgumentException(
                    String.format("机房ID 不能 > %d 或 < 0", MAX_USABLE_MACHINE_ROOM_ID));
        }
        SnowFlakeUtils.machineId = machineId;
        SnowFlakeUtils.machineRoomId = machineRoomId;
    }

    /**
     * 核心方法，使得当前这台机器上的snowflake算法程序生成一个全局唯一的id
     *
     * @return 主键ID
     */
    public static synchronized long getSnowFlakeId() {
        // 获取当前时间戳，单位毫秒
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis < lastTimestamp) {
            throw new RuntimeException(
                    String.format("时钟向后移动，直到 %d 为止，拒绝在 %d 毫秒内生成ID",
                            lastTimestamp, lastTimestamp - currentTimeMillis));
        }

        // 相同毫秒内发生两次或多次生产ID的操作
        if (lastTimestamp == currentTimeMillis) {

            // 在4096这个范围内，避免传递sequence超过了4096这个范围
            sequence = (sequence + 1) & SEQUENCE_MASK;

            // 当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
            if (sequence == 0) {
                long tempTime = System.currentTimeMillis();
                while (currentTimeMillis >= tempTime) {
                    tempTime = System.currentTimeMillis();
                }
                currentTimeMillis = tempTime;
            }
        } else {
            sequence = 0L;
        }

        // 记录最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = currentTimeMillis;


        // 这儿就是最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((currentTimeMillis - START_TIME) << TIME_STAMP_LEFT_SHIFT) |
                (machineRoomId << MACHINE_ROOM_ID_SHIFT) |
                (machineId << MACHINE_ID_SHIFT) | sequence;
    }

}


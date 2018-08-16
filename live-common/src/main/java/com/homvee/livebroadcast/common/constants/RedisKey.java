package com.homvee.livebroadcast.common.constants;


/**
 * @author ddyunf
 */
public class RedisKey {
    /**
     * 保存门店信息的前缀
     */
    public static final String STORE = "STORE";
    /**
     * 保存设备信息的前缀
     */
    public static final String DEVICE = "DEVICE";
    /**
     * 保存门店设备信息的前缀
     */
    public static final String STORE_DEVICE = "STORE_DEVICE";
    /**
     * 保存门店规则的前缀
     */
    public static final String STORE_RULE = "STORE_RULE";
    /**
     * 保存门店调用外部接口获取手机号码的的次数限制规则
     */
    public static final String STORE_RULE_ST_PH_LIMIT = "STORE_RULE_ST_PH_LIMIT";
    /**
     * 保存门店客户信息前缀
     */
    public static final String STORE_CUSTOMER = "STORE_CUSTOMER";
    /**
     * 声压授权TOKEN前缀
     */
    public static final String SOUND_TOOTH_SMS_TOKEN = "SOUND_TOOTH_SMS_TOKEN";

    /**
     * 客户门店统计分布式锁key
     */
    public static final String LOCK_CUSTOMER_STORE_STATS_TASK = "LOCK_CUSTOMER_STORE_STATS_TASK";
    /**
     * 每个店铺的客户数据统计分布式锁key
     */
    public static final String LOCK_STORE_CUSTOMER_STATS_TASK = "LOCK_STORE_CUSTOMER_STATS_TASK";
}

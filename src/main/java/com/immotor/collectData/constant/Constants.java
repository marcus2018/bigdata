package com.immotor.collectData.constant;



public class Constants {
    public static  final String MATURE_USER="成熟用户";
    public static  final String HALF_MATURE_USER="半成熟用户";
    public static final String REDIS_TOKEN_KEY = "PROMOTION:ACCESS_TOKEN";
    public static final String ACCESS_TOKEN_KEY = "AccessToken";
    public static final String APP_ACCESS_TOKEN_KEY = "Authorization";
    public static final String USER_INFO_KEY = "userInfo";
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static final String APP_USER_INFO_KEY = "uID";
    public static final Long ONE_DAY_MILLS = 24*60*60*1000L;  // 一天的毫秒数
    public  static final Integer SECONDSFOR30DAY=30*24*60*60;
    public  static final Integer SECONDSFOR200DAY=200*24*60*60;

    /*aliyun 日志服务*/
    public  static  final String ENDPOINT="https://cn-shenzhen.log.aliyuncs.com";
    public  static  final String ACCESSKEYID="HmTjtVwGWaEvbDw5";
    public  static  final String ACCESSKEYSECRET ="GEKUNH2wv7fen6LxQhH2655ZcgRvmd";


    /**
     * logHub项目名：数据采集
     */
    public static final String LOG_HUB_PROJECT_ELASTIC = "elastic";

    public static final String LOG_STORE_SCOOTER_EVERY_ROUTE = "for_app_scooter_everyroute_speedperkm";

    /**
     * logHub项目名：轨迹数据
     */
    public static final String LOG_HUB_PROJECT_EC_LOC = "ec-loc";
    /**
     * logHub数据库 ：轨迹数据
     */
    public static final String LOG_STORE_EC_LOC = "ec-loc";
    /**
     * logHub数据库 ：旧版轨迹数据
     */
    public static final String LOG_STORE_EC_LOC_OLD = "ec_loc";
    /**
     * logHub项目名：心跳数据
     */
    public static final String LOG_HUB_PROJECT_EC_HB = "ec-hb";
    /**
     * logHub项目名：心跳数据
     */
    public static final String LOG_HUB_PROJECT_EC_HB_OLD = "ec_hb";
    /**
     * logHub数据库 ：心跳数据
     */
    public static final String LOG_STORE_EC_HB = "ec-hb";
    /**
     * loghub ec_hb ,ec_loc 变更日期
     */
    public  static  final  String LOGHUB_EC_LOC_AND_EC_HB_CHAGING_TIME="2019-07-14 00:00:00";



}

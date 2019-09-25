package com.immotor.collectData.util;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.response.GetLogsResponse;
import com.immotor.collectData.constant.Constants;
import com.immotor.collectData.exception.ServiceException;


import java.util.ArrayList;
import java.util.Date;

/**
 * loghub 连接
 */
public class LoghubUtils {

    private  String project;
    private  String  logstore;
    private  String query;
    private  int  from = (int) (new Date().getTime() / 1000 - Constants.SECONDSFOR30DAY);
    private  int  to = (int) (new Date().getTime() / 1000);

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public LoghubUtils(String project, String logstore, String query){
        this.project=project;
        this.logstore=logstore;
        this.query=query;
    }
    public LoghubUtils(String project, String logstore, int from, int to, String query){
        this(project, logstore, query);
        this.from=from;
        this.to=to;
    }


    public ArrayList<QueriedLog> getQueriedLogs( ) {


        Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

        GetLogsResponse res4 = null;
        for (int retry_time = 0; retry_time < 3; retry_time++) {
            GetLogsRequest req4 = new GetLogsRequest(this.project, this.logstore, this.from, this.to, "", this.query);
            try {
                res4 = client.GetLogs(req4);
            } catch (LogException e) {
                throw new ServiceException(-1, "日志服务连接异常，请稍后再试");
            }
            if (res4 != null && res4.IsCompleted()) {
                break;
            }

        }
        return  res4.GetLogs();


    }
    /**
     * ec_track 连接
     * @param from  从什么时间开始  单位 s
     * @param to    到什么时间结束  单位 s
     * @param query 查询语句
     * @return
     */
    public static GetLogsResponse ec_track_Loghub(int from,int to,String query) {

        try {
            Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

            String project = "elastic";
            // 日志库 ec_track
            String logstore = "ec_track";

            GetLogsResponse res4 = null;
            // 连接三次
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", query);

                try {
                    res4 = client.GetLogs(req4);
                } catch (LogException e) {
                    throw new ServiceException(-1, "网络异常，连接LogHub失败，请稍后再试");
                }
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
            }

            return res4;
        } catch (Exception e) {
            throw new ServiceException(-1, "查询失败: " + e.getMessage());
        }
    }


    /**
     * open-for-interface 连接
     * @param from  从什么时间开始  单位 s
     * @param to    到什么时间结束  单位 s
     * @param query 查询语句
     * @return
     */
    public static GetLogsResponse open_for_interface_Loghub(int from,int to,String query) {

        try {
            Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

            String project = "open-for-interface";
            // 日志库 ec_track
            String logstore = "battery_damage";

            GetLogsResponse res4 = null;
            // 连接三次
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", query);

                try {
                    res4 = client.GetLogs(req4);
                } catch (LogException e) {
                    throw new ServiceException(-1, "网络异常，连接LogHub失败，请稍后再试");
                }
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
            }

            return res4;
        } catch (Exception e) {
            throw new ServiceException(-1, "open-for-interface 查询失败: " + e.getMessage());
        }


    }


    /**
     * ec_track 连接
     * @param from  从什么时间开始  单位 s
     * @param to    到什么时间结束  单位 s
     * @param query 查询语句
     * @return
     */
    public static GetLogsResponse collectdata_station_meterrecord(int from,int to,String query) {

        try {
            Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

            String project = "collectdata";
            // 日志库 ec_track
            String logstore = "station_meterrecord";

            GetLogsResponse res4 = null;
            // 连接三次
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", query);

                try {
                    res4 = client.GetLogs(req4);
                } catch (LogException e) {
                    throw new ServiceException(-1, "网络异常，连接LogHub失败，请稍后再试");
                }
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
            }

            return res4;
        } catch (Exception e) {
            throw new ServiceException(-1, "查询失败: " + e.getMessage());
        }
    }

    /**
     * open_for_interface
     *
     */
    public static GetLogsResponse scooter_active_Loghub(int from,int to,String query) {

        try {
            Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

            String project = "open-for-interface";
            // 日志库 ec_track
            String logstore = "scooter_active";

            GetLogsResponse res4 = null;
            // 连接三次
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", query);

                try {
                    res4 = client.GetLogs(req4);
                } catch (LogException e) {
                    throw new ServiceException(-2, "网络异常，连接LogHub失败，请稍后再试");
                }
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
            }

            return res4;
        } catch (Exception e) {
            throw new ServiceException(-2, "scooter_active 日志库查询失败: " + e.getMessage());
        }
    }


    /**
     * common_Loghub
     */
    public static GetLogsResponse common_Loghub(String project, String logstore, int from, int to, String query) {

        try {
            Client client = new Client(Constants.ENDPOINT, Constants.ACCESSKEYID, Constants.ACCESSKEYSECRET);

//            String project = "open-for-interface";
//            // 日志库 ec_track
//            String logstore = "scooter_active";

            GetLogsResponse res4 = null;
            // 连接三次
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", query);
                res4 = client.GetLogs(req4);
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
            }

            return res4;
        } catch (Exception e) {
            throw new ServiceException(-2, "scooter_active 日志库查询失败: " + e.getMessage());
        }
    }

}

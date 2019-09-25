package com.immotor.collectData.controller;



import com.alibaba.fastjson.JSONObject;
import com.aliyun.datahub.model.PutRecordsResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.immotor.collectData.model.*;
import com.immotor.collectData.service.Collect;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private Collect collect;
    private  static Logger logger= LoggerFactory.getLogger(CommonController.class);

    /**
     * @return  采集充电时间统计
     */
    @RequestMapping(value = "/collectChargeTimeLog", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object collectChargeTimeLog(	@RequestBody ChargeTimeLog contentLog){
        System.out.println(contentLog.getId());
        return  ResponseResult.Result(collect.collectLog(contentLog,"app_chargetime"));
    }


    /**
     * @return  采集电池版本上报
     */
    @RequestMapping(value = "/collectBatteryVersionLog", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object collectBatteryVersionLog(	@RequestBody BatteryVersionLog batteryVersionLog){

        return  ResponseResult.Result(collect.collectLog(batteryVersionLog,"app_battery_version"));
    }

//    /**
//     * @return  采集电池状态采样
//     */
//    @RequestMapping(value = "/collectBatteryStatusSample", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
//    public @ResponseBody Object collectBatteryStatusSample(	@RequestBody BatteryStatusLog batteryStatusLog){
//
//        return  ResponseResult.Result(collect.collectLog(batteryStatusLog,"datahub_android_batterystatus"));
//    }

    /**
     * @return  采集充电柜统计
     */
    @RequestMapping(value = "/collectCabinetRecord", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object collectCabinetRecord(	@RequestBody CabinetRecordLog cabinetRecordLog){

        return  ResponseResult.Result(collect.collectLog(cabinetRecordLog,"datahub_android_collectcabinetrecord"));
    }

    /**
     * @return  采集电池一次性数据
     */
    @RequestMapping(value = "/batteryOnceDataCollect", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object batteryOnceDataCollect(	@RequestBody BatteryOnceDataCollect batteryOnceDataCollect){

        return  ResponseResult.Result(collect.collectLog(batteryOnceDataCollect,"android_hard_batteryoncedatacollect"));
    }

    /**
     * @return  电池实时数据，实时采集
     */
    @RequestMapping(value = "/batteryRealTimeDataCollect", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object batteryRealTimeDataCollect(	@RequestBody BatteryRealTimeDataCollect batteryRealTimeDataCollect){

        return  ResponseResult.Result(collect.collectLog(batteryRealTimeDataCollect,"android_hard_batteryrealtimedatacollect"));
    }

    /**
     * @return  电池实时数据，实时采集
     */
    @RequestMapping(value = "/batteryLogDataCollect", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object batteryRealTimeDataCollect(	@RequestBody BatteryLogDataCollect batteryLogDataCollect){
        return  ResponseResult.Result(collect.collectLog(batteryLogDataCollect,"android_hard_batterylogdatacollect"));
    }
    //datahub_android_emptyTradeableRecord
//
//    /**
//     * @return  电池实时数据，实时采集
//     */
//    @RequestMapping(value = "/collectEmptyTradeableRecord", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
//    public @ResponseBody Object collectEmptyTradeableRecord(	@RequestBody EmptyTradeableRecord emptyTradeableRecord){
//        long start=System.currentTimeMillis();
//
//        long end=System.currentTimeMillis();
//        System.out.println(end-start);
//        return  1;
//       // return  ResponseResult.Result(collect.collectLog(emptyTradeableRecord,"datahub_android_emptyTradeableRecord"));
//    }


//    /**
//     * @return  电池实时数据，实时采集
//     */
//    @RequestMapping(value = "/collectLog", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
//    public @ResponseBody Object collectLog(	@RequestBody EmptyTradeableRecord emptyTradeableRecord){
//        return  ResponseResult.Result(collect.collectLog(emptyTradeableRecord,"datahub_android_emptyTradeableRecord"));
//    }

    /**
     * 单文件上传
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request,@RequestParam("pid")String pid) {
        System.out.println(file.isEmpty());
        if (!file.isEmpty()) {
            String saveFileName = file.getOriginalFilename();
//            System.out.println(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
//            File saveFile = new File("C:\\Users\\Marcus\\" + saveFileName);
//            if (!saveFile.getParentFile().exists()) {
//                saveFile.getParentFile().mkdirs();
//            }
            try {
             //   BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                String str=new String(file.getBytes());
                collect.collectLog(new LogStationMessage(str,pid),"datahub_android_LogStationMessage");

           //     out.write(file.getBytes());
          //      out.flush();
          //      out.close();
                return " 上传成功";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败,";
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," ;
            }
        } else {
            return "上传失败，因为文件为空.";
        }
    }

    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    @PostMapping("/uploadFiles")
    @ResponseBody
    public String uploadFiles(HttpServletRequest request) throws IOException {
        File savePath = new File("C:\\Users\\Marcus");
        System.out.println(request.getSession().getServletContext().getRealPath("/upload/"));
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        System.out.println(files.size());
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    File saveFile = new File(savePath, file.getOriginalFilename());
                    stream = new BufferedOutputStream(new FileOutputStream(saveFile));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    if (stream != null) {
                        stream.close();
                        stream = null;
                    }
                    return "第 " + i + " 个文件上传有错误" + e.getMessage();
                }
            } else {
                return "第 " + i + " 个文件为空";
            }
        }
        return "所有文件上传成功";
    }

    /**
     * @return  电池实时数据，实时采集
     */
    @RequestMapping(value = "/andriodBatteryInfo", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object andriodBatteryInfo(	@RequestBody AndriodBatteryInfo andriodBatteryInfo){

        return  ResponseResult.Result(collect.collectLog(andriodBatteryInfo,"datahub_batteries_mayorsure"));
    }
    /**
     * @return  电池实时数据，实时采集
     */
    @RequestMapping(value = "/batteryInStationDetailInfo", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object batteryInStationDetailInfo(	@RequestBody BatteryDetailInfo batteryDetailInfo){
        return  ResponseResult.Result(collect.collectLog(batteryDetailInfo,"ods_datahub_andriod_batteryDetailInfo"));
    }

    /**
     * @return  电柜数据
     */
    @RequestMapping(value = "/cabinetMonitoringInfo", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
    public @ResponseBody Object cabinetMonitoringInfo(	@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toJSONString());
      //  JsonObject obj = new JsonParser().parse(jsonObject.toJSONString()).getAsJsonObject();
        CabinetMonitoringInfo cabinetMonitoringInfo = new CabinetMonitoringInfo(jsonObject.toJSONString());
        return  ResponseResult.Result(collect.collectLog(cabinetMonitoringInfo,"origin_andriod_cabinetMonitoringInfo"));
    }
    /**
     * @return  通用接口
     */
    @RequestMapping(value = "/info", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
//    @RequestMapping(value = "/info", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)

    public @ResponseBody Object info(	@RequestBody JSONObject jsonObject ){
        logger.info(jsonObject.toJSONString());

        return  ResponseResult.Result(collect.collectLog(jsonObject,"loghub"));
      }
    /**
     * @return  通用接口
     */
    @RequestMapping(value = "/list", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)
//    @RequestMapping(value = "/info", produces = {"application/json;charset=UTF-8;charset=UTF-8"},method = RequestMethod.POST)

    public @ResponseBody Object list(	 @RequestBody JSONObject jsonObject ){
        logger.info(jsonObject.toJSONString());

        JsonObject obj = new JsonParser().parse(jsonObject.toJSONString()).getAsJsonObject();
        CabinetMonitoringInfo cabinetMonitoringInfo = new CabinetMonitoringInfo(jsonObject.toJSONString());
        StationBid stationBid = new Gson().fromJson(obj, StationBid.class);

       // return  null;
        Object obj1=collect.getBatteryLogIndex(stationBid,"batteryLogIndex");
        if(obj1 instanceof  Integer){
            return  ResponseResult.ResultError("没数据");
        }else{
            return  ResponseResult.Result(collect.getBatteryLogIndex(stationBid,"batteryLogIndex"));
        }
    }



}


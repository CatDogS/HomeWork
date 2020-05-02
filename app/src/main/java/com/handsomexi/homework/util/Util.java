package com.handsomexi.homework.util;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.HomeworkNet;
import com.handsomexi.homework.bean.dayBean;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {
    public static void mkDirs(){
        File file = getWrongFile();
        boolean a = false;
        if(!file.exists())
            a = file.mkdirs();
    }
    public static File getWrongFile(){
        return  new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/Wrong");
    }
    public static File getNewPicFile(){
        return new File(getWrongFile().getPath(),new Date().getTime()+".jpg");
    }



    public static  List<dayBean> handleDayResponse(List<HomeworkNet.DataBean> dataBeans){
        List<dayBean> dataList = new ArrayList<>();
        for (HomeworkNet.DataBean homeworkNet : dataBeans){
            dayBean dayBean = new dayBean(homeworkNet.getUid(),homeworkNet.getImg_path(),homeworkNet.getTime(),"0","无","0");
            dataList.add(dayBean);
        }
        return dataList;
    }

    /**
     * 处理a2fragment的json数据
     * @param dataList
     */
   public static List<HomeWorkBean> handlerHomeworkResponse(List<HomeworkNet.DataBean> dataList) {

        List<HomeWorkBean> homeWorkBeanList = new ArrayList<>();
        String month = getCurrentMonth();
        for (HomeworkNet.DataBean homeworkNet : dataList){
            // TODO: 2019/1/6 增加数据的检验，一检验是否为null，二检验数据类型是否正确
            HomeWorkBean homeWorkBean = new HomeWorkBean(homeworkNet.getImg_path(), homeworkNet.getType(), homeworkNet.getEdu_level(), homeworkNet.getGrade(),
                    Integer.parseInt(homeworkNet.getDifficult()), 0, Long.parseLong(homeworkNet.getTime()), month, homeworkNet.getType());
            Log.d("Seven","服务器上的imagePath为" + homeworkNet.getImg_path());
            homeWorkBeanList.add(homeWorkBean);
            Log.d("Seven","contentList里的部分数据" + homeWorkBeanList.get(0).getTime());
        }
        return homeWorkBeanList;
    }

    public static String getCurrentMonth() {
        String  currentMonth = "2018年9月";
        return currentMonth;
    }
    public static List<Integer> intArray2List(int[] a){
        List<Integer> integers = new ArrayList<>();
        for (int i :a){
            integers.add(i);
        }
        return integers;
    }
    public static String long2Date(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        String year = cal.get(Calendar.YEAR)+"年";
        String month = cal.get(Calendar.MONTH)+1 + "月";
        String day = cal.get(Calendar.DATE) +"日";
        return year + month +day;
    }

    public static String list2String(List<Object> list){
        JsonArray array = new JsonArray();
        if(list == null || list.size() ==0){
            return array.toString();
        }else {
            String type = list.get(0).getClass().getSimpleName();
            try {
                switch (type) {
                    case "Boolean":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Boolean) list.get(i));
                        }
                        break;
                    case "Long":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Long) list.get(i));
                        }
                        break;
                    case "Float":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Float) list.get(i));
                        }
                        break;
                    case "String":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((String) list.get(i));
                        }
                        break;
                    case "Integer":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Integer) list.get(i));
                        }
                        break;
                    default:
                        Gson gson = new Gson();
                        for (int i = 0; i < list.size(); i++) {
                            JsonElement obj = gson.toJsonTree(list.get(i));
                            array.add(obj);
                        }
                        break;
                }
                return array.toString();
            }catch (Exception e){
                return "";
            }
        }
    }
    public static <T> List<T> string2List(String json,Class<T> aClass) {
        List<T> list = new ArrayList<>();
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, aClass));
            }
        }
        return list;
    }
    public static Intent getHomeIntent(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;

    }
}

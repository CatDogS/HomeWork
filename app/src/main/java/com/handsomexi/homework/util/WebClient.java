package com.handsomexi.homework.util;

import android.util.Log;

import okhttp3.*;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class WebClient {
    private static OkHttpClient client;
    public static void init(){
        client=new OkHttpClient.Builder().build();
    }

    public static void post(String url, Map<String, String> map,Callback callback){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key:map.keySet()) builder.add(key,map.get(key));
        Request  request=new Request.Builder().url(url).post(builder.build()).build();
        client.newCall(request).enqueue(callback);
    }
    public static Response post(String url, Map<String, String> map) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key:map.keySet()) builder.add(key,map.get(key));
        Request  request=new Request.Builder().url(url).post(builder.build()).build();
        Call call=client.newCall(request);
        return  call.execute();
    }

    //异步的请求方法
    public static void get(String url,Callback callback)  {
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);

    }

    //同步的请求方法
    public static Response get(String url) throws IOException {
        Request request=new Request.Builder().url(url).build();
        Call call=client.newCall(request);
        return  call.execute();
    }
    public static void DownFile(String url) throws IOException {
        BufferedSource bufferedSource = WebClient.get(url).body().source();
        Sink sink = Okio.sink(new File("D:\\yzm.jpg"));
        bufferedSource.readAll(sink);
        sink.flush();
        bufferedSource.close();
        sink.close();
    }

    public static void uploadHead(String id,String filePath,Callback callback){
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",id)
                .addFormDataPart("img", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpg"), file));
        Request request = new Request.Builder()
                .url("http://122.204.82.130:8080/img")
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void upPic(String id,String title,String label,String type,String edu,String grade,int dif,File img,Callback callback){
        String urlStr = "?name=习大大&pawd=测试" +"&uid="+ id + "&title=" + title + "&label=" + label + "&type=" + type
                + "&edu_level=" + edu +"&grade=" + grade + "&dif=" + dif;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)  //设置type为"multipart/form-data，不然无法上传参数
                .addFormDataPart("uid",id)//用户id
                .addFormDataPart("title",title)//错题类型
                .addFormDataPart("label",label)//错题类型
                .addFormDataPart("type",type)//错题类型
                .addFormDataPart("edu_level",edu)//教育程度
                .addFormDataPart("grade",grade)//年级
                .addFormDataPart("dif", String.valueOf(dif))//难度等级
                .addFormDataPart("img",img.getName(),
                        RequestBody.create(MediaType.parse("image/jpg"), img));
        Request request = new Request.Builder()
                .url("http://122.204.82.130:8080/wwh/question"+ urlStr)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("WebClientSvllen","上传失败 ");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                Log.d("WebClientSvllen","返回的responseStr：" + responseStr);

            }
        });
    }

}

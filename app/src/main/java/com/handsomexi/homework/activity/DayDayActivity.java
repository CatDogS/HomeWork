package com.handsomexi.homework.activity;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsomexi.homework.R;
import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.HomeworkNet;
import com.handsomexi.homework.bean.dayBean;
import com.handsomexi.homework.util.NetWorkUtils;
import com.handsomexi.homework.util.SqlUtil;
import com.handsomexi.homework.util.Util;
import com.handsomexi.homework.util.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static anet.channel.util.Utils.context;

public class DayDayActivity extends AppCompatActivity  {

    private  List<dayBean> dayBeanList = new ArrayList<>();
    //private List<HomeWorkBean> mHomeWorkBeanList = new ArrayList<>();
    int totalPosition = 0;
    int mPosition = 0;
    int accuracy = 0;
    String[] toolbarType = new String[]{"易错斩","难点斩","重点斩","随机斩","收工斩"};
    @BindView(R.id.day_back)
    ImageButton btn_back;
    @BindView(R.id.day_toolbar_type)
    TextView toolbar_type;
    @BindView(R.id.day_answer_image)
    ImageView day_image;
    @BindView(R.id.day_import)
    EditText edit_import;
    @BindView(R.id.day_btn_hint)
    Button btn_hint;          //查看提示按钮
    @BindView(R.id.day_btn_commit)
    Button btn_commit;
    @BindView(R.id.day_text_commit)
    TextView text_commit;
    @BindView(R.id.day_answer_commit)
    EditText edit_answer_commit;
    @BindView(R.id.day_text_ture)
    TextView text_true;
    @BindView(R.id.day_answer_true)
    EditText edit_answer_true;
    @BindView(R.id.day_congratulation)
    Button btn_congratulation;
    @BindView(R.id.day_true_next)
    Button btn_true_next;

    @BindView(R.id.day_button_false)
    Button btn_false;
    @BindView(R.id.day_false_next)
    Button btn_false_next;

    @BindView(R.id.day_sun)
    ImageView image_sun;
    @BindView(R.id.day_accuracy)
    TextView text_accuracy;
    @BindView(R.id.day_achieve)
    Button btn_achieve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_day);
        ButterKnife.bind(this);
        iniData();
        UpdateUi();

    }

    private void UpdateUi() {
        //刷新界面
       // Glide.with(this).asBitmap().thumbnail(0.2f).load(Uri.parse("http://122.204.82.130:8080/wwh/file?path=" + )).into(day_image);
        toolbar_type.setText(toolbarType[mPosition]);

    }

    private void iniData() {

        boolean isWifi = NetWorkUtils.isWifiConnected(this);
        Log.d("Seven","当前wifi状态 isWifi  " + isWifi);

        String name = "习大大";
        String pswd = "测试";
        String uri = "http://122.204.82.130:8080/wwh/question?name="+name+"&pawd="+pswd+"&type=数学" ;
        if (isWifi){
            WebClient.init();
            WebClient.get(uri, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtils.showShort("返回数据失败");
                    Log.d("DayDaySeven","返回数据失败" );
                    //mHomeWorkBeanList = SqlUtil.query("全部","全部","全部");
                    Log.d("DayDaySeven","initData(0方法中 分割前mHomeworkBeanList的长度为  " + dayBeanList.size());
                    //totalPosition = mHomeWorkBeanList.size();
                    Log.d("DayDaySeven","initData()方法中 105行 count=  ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    if (response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("Seven", "返回数据成功 responseData" + responseData);
                        //解析Json数据
                        //NetBean netBean = Util
                        HomeworkNet homeworkNet = new Gson().fromJson(responseData, HomeworkNet.class);
                        List<HomeworkNet.DataBean> data = homeworkNet.getData();
                        //转换为本地bean
                        dayBeanList = Util.handleDayResponse(data);
                    }
                }
            });

        }
        //这里的查询应该是按照复习计划查询
        if (totalPosition > 5){
            //将5后面的都分割完
            dayBeanList =  dayBeanList.subList(0,6);
            Log.d("Seven","initData(0方法中 分割后mHomeworkBeanList的长度为  " + dayBeanList.size());
            totalPosition = 5;
        }
    }


    @OnClick({R.id.day_back,R.id.day_btn_commit,R.id.day_true_next,R.id.day_false_next})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.day_back:
                Intent intent = new Intent(DayDayActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.day_btn_commit:

                if (edit_import.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "未输入答案，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    String importAnswer = edit_import.getText().toString();
                    showAnswer(importAnswer);
                        //判断答案是否正确
                        boolean answer = check();
                        if (answer){
                            accuracy++;
                            replaceWidget(1);
                        } else {
                            replaceWidget(2);
                        }
                    }

                break;
            case R.id.day_true_next:
                //等于5时题目做完，显示正确率
                if (mPosition > 4){
                    replaceWidget(5);
                } else {
                    replaceWidget(3);
                }

                break;
            case R.id.day_false_next:
                if (mPosition > 4){
                    replaceWidget(5);
                } else {
                    replaceWidget(4);
                }
                break;
        }
    }

    private boolean check() {
        String trueAnswer = "12315";
        if (edit_import.getText().toString().equals(trueAnswer)){
            Log.d("DAySeven","输入的答案正确");
            return  true;
        } else {
            Log.d("DAySeven","输入的答案错误");
            return false;
        }
    }


    private void replaceWidget(int i) {
        switch(i){
            case 1:     //答案正确
                edit_import.setVisibility(View.GONE);
                btn_hint.setVisibility(View.GONE);
                btn_commit.setVisibility(View.GONE);
                text_true.setVisibility(View.VISIBLE);
                edit_answer_true.setVisibility(View.VISIBLE);
                text_commit.setVisibility(View.VISIBLE);
                edit_answer_commit.setVisibility(View.VISIBLE);
                btn_congratulation.setVisibility(View.VISIBLE);
                btn_true_next.setVisibility(View.VISIBLE);
//                String importAnswer = edit_import.getText().toString();
//                showAnswer(importAnswer);
                // TODO: 2018/12/16 我感觉应该加个返回选项，可以修改答案
                mPosition ++;
                totalPosition --;
                break;
            case 2:   //答案错误
                edit_import.setVisibility(View.GONE);
                btn_hint.setVisibility(View.GONE);
                btn_commit.setVisibility(View.GONE);
                text_true.setVisibility(View.VISIBLE);
                edit_answer_true.setVisibility(View.VISIBLE);
                text_commit.setVisibility(View.VISIBLE);
                edit_answer_commit.setVisibility(View.VISIBLE);
                btn_false.setVisibility(View.VISIBLE);
                btn_false_next.setVisibility(View.VISIBLE);
                mPosition ++;
                totalPosition --;
                break;
            case 3:    //答案正确时点击下一斩
                edit_import.setText("");
                edit_import.setVisibility(View.VISIBLE);
                btn_hint.setVisibility(View.VISIBLE);
                btn_commit.setVisibility(View.VISIBLE);
                btn_congratulation.setVisibility(View.GONE);
                btn_true_next.setVisibility(View.GONE);
                text_true.setVisibility(View.GONE);
                edit_answer_true.setVisibility(View.GONE);
                text_commit.setVisibility(View.GONE);
                edit_answer_commit.setVisibility(View.GONE);
                UpdateUi();
                break;
            case 4:   //答案错误时点击下一斩
                edit_import.setText("");
                btn_false.setVisibility(View.GONE);
                btn_false_next.setVisibility(View.GONE);
                text_true.setVisibility(View.GONE);
                edit_answer_true.setVisibility(View.GONE);
                text_commit.setVisibility(View.GONE);
                edit_answer_commit.setVisibility(View.GONE);
                edit_import.setVisibility(View.VISIBLE);
                btn_hint.setVisibility(View.VISIBLE);
                btn_commit.setVisibility(View.VISIBLE);
                UpdateUi();
                break;
            case 5:  //最后总结
                btn_congratulation.setVisibility(View.GONE);
                btn_true_next.setVisibility(View.GONE);
                day_image.setVisibility(View.GONE
                );
                btn_false.setVisibility(View.GONE);
                btn_false_next.setVisibility(View.GONE);
                text_true.setVisibility(View.GONE);
                edit_answer_true.setVisibility(View.GONE);
                text_commit.setVisibility(View.GONE);
                edit_answer_commit.setVisibility(View.GONE);
                image_sun.setVisibility(View.VISIBLE);
                text_accuracy.setVisibility(View.VISIBLE);
                text_accuracy.setText("正确率 " + accuracy +"/5");
                btn_achieve.setVisibility(View.VISIBLE);
                toolbar_type.setText("");
                break;
        }
    }

    private void showAnswer(String answer) {
        edit_answer_commit.setText(answer);
        edit_answer_true.setText("12315");
    }


}

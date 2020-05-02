package com.handsomexi.homework.activity.mine;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.bean.UserBean;
import com.handsomexi.homework.util.WebClient;

import okhttp3.Response;

public class MemberActivity extends BaseActivity {

    private TextView text1;
    private TextView text2;
    private int vip_level;
    private String vip_expiration_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = WebClient.get("http://122.204.82.130:8080/login?name=习大大&password=测试");
            String res = response.body().string();
            Gson gson = new Gson();
            UserBean userBean = gson.fromJson(res,UserBean.class);
            String string = gson.toJson(userBean);
            UserBean userBean1 = gson.fromJson(string,UserBean.class);
            vip_expiration_date = userBean1.getData().getVip_expiration_date();
            vip_level = userBean1.getData().getVip_level();
            setVip_expiration_date(vip_expiration_date);
            setVip_level(vip_level);
        }catch (Exception e){
            e.printStackTrace();
        }

        text1=(TextView)findViewById(R.id.level);

        text1.setText(String.valueOf(getVip_level()));
        text2=(TextView)findViewById(R.id.expiration_date);
        text2.setText(getVip_expiration_date());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void setVip_level(int vip_level) {
        this.vip_level = vip_level;
    }

    public int getVip_level() {
        return vip_level;
    }

    public String getVip_expiration_date() {
        return vip_expiration_date;
    }
    public void setVip_expiration_date(String vip_expiration_date) {
        this.vip_expiration_date = vip_expiration_date;
    }

}

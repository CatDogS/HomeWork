package com.handsomexi.homework.activity.mine;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;

import com.google.gson.Gson;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.bean.UserBean;
import com.handsomexi.homework.util.WebClient;

import okhttp3.Response;


public class NameActivity extends BaseActivity {
    private EditText edit;
    private String name123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        findViewById(R.id.back1).setOnClickListener(v -> finish());

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Response response = null;
        try {
            response = WebClient.get("http://122.204.82.130:8080/login?name=习大大&password=测试");
            String res = response.body().string();
            Gson gson = new Gson();
            UserBean userBean = gson.fromJson(res,UserBean.class);
            String string = gson.toJson(userBean);
            UserBean userBean1 = gson.fromJson(string,UserBean.class);
            name123 = userBean1.getData().getName();
            setName123(name123);
            }catch (Exception e){
            e.printStackTrace();
            }
        edit = (EditText) findViewById(R.id.edit);
        edit.setText(getName123());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public String getName123() {
        return name123;
    }

    public void setName123(String name123) {
        this.name123 = name123;
    }
}


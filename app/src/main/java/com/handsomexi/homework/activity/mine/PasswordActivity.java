package com.handsomexi.homework.activity.mine;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.bean.UserBean;
import com.handsomexi.homework.util.WebClient;

import okhttp3.Response;

public class PasswordActivity extends BaseActivity {

    private EditText edit;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Button button = (Button) findViewById(R.id.back1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Response response = null;
        try {
            response = WebClient.get("http://122.204.82.130:8890/login?name=习大大&password=测试");
            String res = response.body().string();
            Gson gson = new Gson();
            UserBean userBean = gson.fromJson(res,UserBean.class);
            String string = gson.toJson(userBean);
            UserBean userBean1 = gson.fromJson(string,UserBean.class);
            password = userBean1.getData().getPassword();
            setPassword(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        edit = (EditText) findViewById(R.id.password);
        edit.setText(getPassword());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

package com.handsomexi.homework.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.bean.UserBean;
import com.handsomexi.homework.util.WebClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class InformationActivity extends BaseActivity {

    private List<Infor> inforList = new ArrayList<>();
    private String name123;
    private String grade;
    private String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Response response;
        try {
            response = WebClient.get("http://122.204.82.130:8890/login?name=习大大&password=测试");
            String res = response.body().string();
            Gson gson = new Gson();
            UserBean userBean = gson.fromJson(res,UserBean.class);
            String string = gson.toJson(userBean);
            UserBean userBean1 = gson.fromJson(string,UserBean.class);
            name123 = userBean1.getData().getName();
            grade = userBean1.getData().getEducational_level()+userBean1.getData().getGrade();
            place = userBean1.getData().getSchool();
            setName123(name123);
            setGrade(grade);
            setPlace(place);
        }catch (Exception e){
            e.printStackTrace();
        }

        initInfor();
        InformationAdapter adapter = new InformationAdapter(InformationActivity.this, R.layout.pn_item,inforList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {Intent intent=new Intent(InformationActivity.this,GradeActivity.class);
                    startActivity(intent);}
                if(position==1)
                {Intent intent=new Intent(InformationActivity.this,NameActivity.class);
                    startActivity(intent);}
                if(position==2)
                {Intent intent=new Intent(InformationActivity.this,PlaceActivity.class);
                    startActivity(intent);}
                if(position==3)
                {Intent intent=new Intent(InformationActivity.this,PasswordActivity.class);
                    startActivity(intent);}
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public String getName123() {
        return name123;
    }
    public String getGrade() {
        return grade;
    }
    public String getPlace() {
        return place;
    }

    public void setName123(String name123) {
        this.name123 = name123;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    private void initInfor(){

        Infor grade = new Infor("修改年级",R.mipmap.p,getGrade());
        inforList.add(grade);
        Infor name = new Infor("修改姓名",R.mipmap.p,getName123());
        inforList.add(name);
        Infor place = new Infor("就读省份",R.mipmap.p,getPlace());
        inforList.add(place);
        Infor password = new Infor("修改密码",R.mipmap.p,"");
        inforList.add(password);
    }


}

class Infor{
    private String db;
    private String name;
    private int imageId;
    public Infor(String name, int imageId , String db){
        this.name = name;
        this.imageId = imageId;
        this.db = db;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getDb(){
        return db;
    }
}

class InformationAdapter extends ArrayAdapter<Infor> {
    private int resourceId;
    InformationAdapter(Context context, int textViewResourceId, List<Infor> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Infor infor = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView inforImage = view.findViewById(R.id.infor_image);
        TextView inforName = view.findViewById(R.id.infor_name);
        TextView inforDb = view.findViewById(R.id.infor_db);
        inforImage.setImageResource(infor.getImageId());
        inforName.setText(infor.getName());
        inforDb.setText(infor.getDb());
        return view;
    }
}


package com.handsomexi.homework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.util.FlexboxUtil;
import com.handsomexi.homework.util.SqlUtil;
import com.handsomexi.homework.util.Util;
import com.handsomexi.homework.util.WebClient;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SaveSubjectActivity extends BaseActivity {
    @BindView(R.id.savesub_img)
    ImageView img;
    @BindView(R.id.savesub_spinner)
    Spinner spinner;
    @BindView(R.id.savesub_edit)
    EditText editText;
    @BindView(R.id.savesub_flexbox1)
    FlexboxLayout flexbox;
    @BindView(R.id.savesub_flexbox2)
    FlexboxLayout flexbox2;
    @BindView(R.id.savesub_ratingBar)
    RatingBar ratingBar;


    private List<String> flowArray;
    private List<String> diffArray;
    String path;
    String currentMonth;
    FlexboxUtil flexboxUtil1;
    FlexboxUtil flexboxUtil2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savesub);
        ButterKnife.bind(this);
        initArray();
        initView();
    }

    void initArray() {
        flowArray = Arrays.asList(getResources().getStringArray(R.array.default_subject));
        diffArray = Arrays.asList(getResources().getStringArray(R.array.default_difficult));
    }
    void initView(){
        path = getIntent().getStringExtra("path");
        Glide.with(this).asBitmap().load(new File(path)).into(img);
        img.setOnClickListener(v -> startActivity(new Intent(SaveSubjectActivity.this,ImageViewActivity.class).putExtra("path",path)));
        flexboxUtil1 = new FlexboxUtil(this, flexbox, flowArray);
        flexboxUtil2 = new FlexboxUtil(this, flexbox2, diffArray);
    }

    public void OnClick(View view){
        switch (view.getId()){
            case R.id.savesub_cancel:{
                ToastUtils.showShort("取消");
                finish();
                new File(path).delete();
                break;
            }
            case R.id.savesub_sure:{
                ToastUtils.showShort("正在保存");
                String id = "1";   //默认为1
                String label = Util.list2String(null);
                String title = editText.getText().toString();
                String type = flexboxUtil1.getCurrentItem();
                String edu = spinner.getSelectedItem().toString();
                int dif = ratingBar.getNumStars();
                String grade = "上学期";
                WebClient.upPic(id, title,label,type, edu, grade, dif, new File(path), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> ToastUtils.showShort("保存失败"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        runOnUiThread(() -> {
                            ToastUtils.showShort("保存成功");
                            getCurrentMonth();
                            SqlUtil.save(path, flexboxUtil1.getCurrentItem(), spinner.getSelectedItem().toString(), "", ratingBar.getNumStars(), 0, currentMonth, flexboxUtil1.getCurrentItem());
                            finish();
                        });
                    }
                });

                break;
            }
        }
    }
    private void getCurrentMonth() {

        currentMonth = "2018年9月";

    }
}

package com.handsomexi.homework.activity.mine;

import android.os.Bundle;

import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

}

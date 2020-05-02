package com.handsomexi.homework.activity.mine;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;

public class GradeActivity extends BaseActivity {

    private String[] data = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        findViewById(R.id.back2).setOnClickListener(v -> finish());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        ListView listView = findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
    }

}

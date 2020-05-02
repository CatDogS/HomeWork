package com.handsomexi.homework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.adapter.ContentAdapter;
import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.subject;
import com.handsomexi.homework.util.DividerUtil;
import com.handsomexi.homework.util.SqlUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends BaseActivity {

    @BindView(R.id.return_button_review)
    ImageButton button_back;
    @BindView(R.id.next_review)
    ImageButton button_next;
    @BindView(R.id.re_last)
    ImageButton button_last;


    int totalPosition = 0;
    int mPosition = 0;
    int sign = 3;

    private ContentAdapter mContentAdapter;

    private List<HomeWorkBean> mHomeWorkBeanList = new ArrayList<>();  //保存所有数据
    private List<HomeWorkBean> mContentList = new ArrayList<>();       //保存点击的对应科目的数据
    private List<subject> mSubjectList = new ArrayList<>();
    private HomeWorkBean showContent;
    private HomeWorkBean nextContent;

    @BindView(R.id.review_myhomework_picture)
    ImageView homeworkPicture;
    @BindView(R.id.review_myhomework_subject)
    TextView subjectText;
    @BindView(R.id.review_myhomework_schoolyear)
    TextView schoolyearText;
    @BindView(R.id.review_myhomework_difficult)
    RatingBar difficultRatingbar;
    @BindView(R.id.review_myhomework_time)
    TextView timeText;
    @BindView(R.id.review_myhomework_review_rating)
    RatingBar reviewRatingbar;
    @BindView(R.id.review_myhomework_reviews)
    TextView reviewText;

    @BindView(R.id.review_recycler)
    RecyclerView contentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_content);
        ButterKnife.bind(this);
        iniData();
        showRecycler();
    }

    /**
     * 处理toolBar的点击事件
     *
     * @param view
     */
    @OnClick({R.id.return_button_review, R.id.next_review,R.id.re_last})
    public void onViewClike(View view) {
        switch (view.getId()) {
            case R.id.return_button_review:
                finish();
                break;
            case R.id.next_review:
                next(mPosition);
                break;
            case R.id.re_last:
                last(mPosition);

        }
    }

    private void last(int position) {
        position --;
        if(position >= 0){
            nextContent = mContentList.get(position);
            showData(nextContent);
            totalPosition ++;
            mPosition = position;
        } else{
            Log.d("Seven","数组越界");
            Toast.makeText(this, "没有上一题", Toast.LENGTH_SHORT).show();
            mPosition = 0;
        }
    }


    private void next(int Position) {
        if (totalPosition == 0){
            ToastUtils.showShort("当前科目试卷已呈现完毕");
        } else {
            Position ++;
            nextContent = mContentList.get(Position);
            showData(nextContent);
            totalPosition --;
            mPosition = Position;
        }
    }


    private void iniData() {
        Intent intent = getIntent();
        String subject = intent.getStringExtra("subject");
        String reviewBatch = intent.getStringExtra("reviewBatch");
        Log.d("Seven","subject" + subject);
        Log.d("Seven","reviewBatch" + reviewBatch);

//        mHomeWorkBeanList = SqlUtil.query("全部", "全部", "全部");
        //处理第一个模块的事件
        mContentList = SqlUtil.queryReviewBatch(subject,reviewBatch);
        if (mContentList.size() == 0){
            ToastUtils.showShort("还没有复习试卷，请查看你的错题");
        } else {
            totalPosition = mContentList.size();
            showContent = mContentList.get(0);
            showData(showContent);
        }

    }

    private void showData(HomeWorkBean showContent) {

        subjectText.setText("科目：" + showContent.getSubject());
        schoolyearText.setText("年级：" + showContent.getSchoolYear());
        difficultRatingbar.setRating(showContent.getDifficulty());
        timeText.setText(format(showContent.getTime()));
        reviewRatingbar.setRating(showContent.getReviews());
        reviewText.setText("已标记复习" + showContent.getReviews() + "次");
    }

    private void showRecycler() {
        //开始显示ContentRecyclerView
        if (mContentAdapter == null){
            mContentAdapter = new ContentAdapter(ReviewActivity.this,mContentList);
        } else {
            mContentAdapter.clear();
            mContentAdapter.addData(mContentList);
        }
        contentRecyclerView.setAdapter(mContentAdapter);
        contentRecyclerView.addItemDecoration(new DividerUtil());                      //添加分割线
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
    }

    public String format(long time) {
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }




}

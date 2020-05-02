package com.handsomexi.homework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.base.BaseActivity;
import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.subject;
import com.handsomexi.homework.util.SqlUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeworkContentActivity extends BaseActivity {


    private List<HomeWorkBean> mContentList = new ArrayList<>();       //保存点击的对应科目的数据
    private HomeWorkBean showContent;
    private HomeWorkBean nextContent;

    int totalPosition = 0;
    int mPosition = 0;
    int sign = 3;


    Info mInfo;

    @BindView(R.id.return_button_homework)
    ImageButton button_back;
    @BindView(R.id.next_homework)
    ImageButton button_next;
    @BindView(R.id.hw_last)
    ImageButton button_last;

    @BindView(R.id.myhomework_picture)
    ImageView homeworkPicture;
    @BindView(R.id.myhomework_subject)
    TextView subjectText;
    @BindView(R.id.myhomework_schoolyear)
    TextView schoolyearText;
    @BindView(R.id.myhomework_difficult)
    RatingBar difficultRatingbar;
    @BindView(R.id.myhomework_time)
    TextView timeText;
    @BindView(R.id.myhomework_review_rating)
    RatingBar reviewRatingbar;
    @BindView(R.id.myhomework_reviews)
    TextView reviewText;

    @BindView(R.id.hw_image)
    ImageView ansImage;
    @BindView(R.id.review_image)
    ImageView reviewImage;
    @BindView(R.id.review_paper)
    TextView reviewPaper;


    PhotoView mPhotoView;
    View mParent;
    View mBg;
    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_content);
        ButterKnife.bind(this);
        mPhotoView = findViewById(R.id.hw_photoview);
        mParent = findViewById(R.id.parent);
        mBg = findViewById(R.id.bg);

//        EventBus.getDefault().register(this);
        iniData();
    }

    private void iniData() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position",0);
        Log.d("Seven","初始化mPosition" );

        String subject = intent.getStringExtra("subject");
        //保存所有数据
        List<HomeWorkBean> mHomeWorkBeanList = SqlUtil.query("全部", "全部", "全部");
        //处理第一个模块的事件
        mContentList = SqlUtil.queryOnSubject(subject);
        totalPosition = mContentList.size();
        totalPosition = totalPosition - mPosition -1 ;
        showContent = mContentList.get(mPosition);
        showData(showContent);
        //设置缩放
        imageClike(homeworkPicture);
        imageClike(reviewImage);
        imageClike(ansImage);



        //处理第三个模块的事件。主要是遍历数据，查看复习这列有无数据，有的直接显示到第三模块


    }

    private void imageClike(ImageView imageView) {
        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoView p = new PhotoView(HomeworkContentActivity.this);
                mInfo = p.getInfo();
                mPhotoView.setImageResource(R.mipmap.background2);
                mBg.startAnimation(in);
                mBg.setVisibility(View.VISIBLE);
                mParent.setVisibility(View.VISIBLE);;
                mPhotoView.animaFrom(mInfo);
            }
        });
        mPhotoView.enable();//开启缩放
        
        mPhotoView.setOnClickListener(v -> {
            Log.d("Seven", "图片已显示");
            mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    mParent.setVisibility(View.GONE);
                }
            });

        });
    }


    @Override
    public void onBackPressed() {
        if (mParent.getVisibility() == View.VISIBLE) {
            mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, () -> mParent.setVisibility(View.GONE));
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 处理toolBar的点击事件
     * @param view
     */
    @OnClick({ R.id.return_button_homework, R.id.next_homework,R.id.hw_last})
    public void onViewClike(View view){
        switch (view.getId()){
            case R.id.return_button_homework:
                finish();
                break;
            case R.id.next_homework:
//                sign = 0;
                next(mPosition);
                break;
            case  R.id.hw_last:
                last(mPosition);
                break;

        }
    }



    private void last(int position) {
        //sign = 1;
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

    private void showData(HomeWorkBean showContent) {
        //缺少两个图片的显示
        subjectText.setText("科目："+ showContent.getSubject());
        schoolyearText.setText("年级："+ showContent.getSchoolYear());
        difficultRatingbar.setRating(showContent.getDifficulty());
        timeText.setText(format(showContent.getTime()));
        reviewRatingbar.setRating(showContent.getReviews());
        reviewText.setText("已标记复习" + showContent.getReviews() + "次");
        reviewPaper.setText(showContent.getReviewOf());

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




    public String format(long time){
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        return  sd.format(date);
    }


    //    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void onMessageEvent(MessageEvent messageEvent){
//        mPosition = messageEvent.getPosition();
//        //处理第一个模块的事件
//        mContentList = SqlUtil.queryOnSubject(messageEvent.getSubject());
//        showContent = mContentList.get(mPosition);
//        showData(showContent);
//
//
//        //处理第三个模块的事件。主要是遍历数据，查看复习这列有无数据，有的直接显示到第三模块
//    }



}

package com.handsomexi.homework.adapter;

import android.content.Context;
import android.widget.RatingBar;

import com.handsomexi.homework.R;
import com.handsomexi.homework.bean.HomeWorkBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class RecommendAdapter extends SimpleAdapter<HomeWorkBean>{
    @BindView(R.id.review_rating)
    RatingBar reViewRating;
    @BindView(R.id.degree_of_difficult)
    RatingBar difficultRating;



    public RecommendAdapter(Context context, List<HomeWorkBean> contentList){
        super(context, R.layout.item_recommend,contentList);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, HomeWorkBean item) {
        /*ButterKnife.bind(this,viewHoder.itemView);
        setRating(reViewRating,item.getReviews());
        setRating(difficultRating ,item.getDifficulty());
        // TODO: 2018/11/20 检验一下图片地址是否在本地 可以创建一个检验类
        //Glide.with(context).asBitmap().thumbnail(0.2f).load(new File(item.getImagePath())).into(viewHoder.getImageView(R.id.content_picture));
        viewHoder.getTextView(R.id.time).setText("时间："+ format(item.getTime()));
        viewHoder.getTextView(R.id.time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点击的是" + viewHoder.getTextView(R.id.time).toString() , Toast.LENGTH_SHORT).show();
            }
        });*/
        viewHoder.getTextView(R.id.recommend_grade).setText("年级：" + item.getSchoolYear());
        viewHoder.getTextView(R.id.recommend_subject).setText("科目：" + item.getSubject());
        //viewHoder.getTextView(R.id.reviews).setText("已经复习" + item.getReviews() + "次");

    }

    public String format(long time){
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sd.format(date);
    }
    //设置星星控件
    /*private void setRating(RatingBar rating, int number){
        rating.setRating(number);
    }*/
}

package com.handsomexi.homework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.HomeworkContentActivity;
import com.handsomexi.homework.adapter.BaseAdapter;
import com.handsomexi.homework.adapter.ContentAdapter;
import com.handsomexi.homework.adapter.HandpickAdapter;
import com.handsomexi.homework.adapter.RecommendAdapter;
import com.handsomexi.homework.adapter.TagAdapter;

import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.HomeworkNet;
import com.handsomexi.homework.bean.subject;
import com.handsomexi.homework.util.DividerUtil;
import com.handsomexi.homework.util.NetWorkUtils;
import com.handsomexi.homework.util.SqlUtil;
import com.handsomexi.homework.util.WebClient;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class bFragment extends Fragment {
    private List<String> subjectArray;
    private List<String> schoolyearArray;
    private List<String> subjectList=new ArrayList<>(),schoolyearList=new ArrayList<>();
    private boolean isNet = false;
    boolean subexpand,schexpand;
    TagAdapter subAdapter,schAdapter;
    HandpickAdapter mHandpickAdapter;
    RecommendAdapter mRecommendAdapter;


    @BindView(R.id.sub_rv123)
    RecyclerView subrecyclerView;
    @BindView(R.id.sch_rv)
    RecyclerView schrecyclerView;

    @BindView(R.id.handpick_recycler_list)
    RecyclerView handpickRecyclerView;
    @BindView(R.id.recommend_recycler_list)
    RecyclerView recommendRecyclerView;


    private Unbinder unbinder;



    @Override
    public boolean getAllowEnterTransitionOverlap() {
        return super.getAllowEnterTransitionOverlap();
    }


    private Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, null);
        unbinder = ButterKnife.bind(this,view);


        initArray();
        initbqview();
        return view;
    }



    void initArray() {

        subjectArray = Arrays.asList(getResources().getStringArray(R.array.default_subject));
        schoolyearArray =Arrays.asList( getResources().getStringArray(R.array.default_shcoolyear));

    }
    public  List<String> setsubList1() {
        subjectList.clear();
        for(int i=0;i<3;i++){
            subjectList.add(subjectArray.get(i));
        }
        return subjectList;
    }
    public  List<String> setsubList2() {
        subjectList.clear();
        for(int i=0;i<subjectArray.size();i++){
            subjectList.add(subjectArray.get(i));
        }
        return subjectList;
    }
    public List<String> setschList1(){
        schoolyearList.clear();
        for (int i=0;i<3;i++){
            schoolyearList.add(schoolyearArray.get(i));
        }
        return schoolyearList;
    }
    public List<String> setschList2(){
        schoolyearList.clear();
        for (int i=0;i<schoolyearArray.size();i++){
            schoolyearList.add(schoolyearArray.get(i));
        }
        return schoolyearList;
    }


    void initbqview(){

        subrecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3){
            @Override
            public  boolean canScrollVertically(){
                return  false;
            }
        });
        subAdapter= new TagAdapter(getContext(),setsubList1());
        subrecyclerView.setAdapter(subAdapter);
        schrecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3){
            @Override
            public  boolean canScrollVertically(){
                return  false;
            }
        });
        schAdapter=new TagAdapter(getContext(),setschList1());
        schrecyclerView.setAdapter(schAdapter);
        getselected();
        requestList(subjectArray.get(0));
        //String defaultsubject =subjectArray.get(0);
        //requestHandpickList(0);
        //requestRecommendList(0);



    }

    public void getselected(){

        subAdapter.setTextviewclick(new TagAdapter.Textviewclick() {

            @Override
            public void textviewclick(int i) {
                Toast.makeText(getContext(),""+i,Toast.LENGTH_SHORT).show();
                //tagAdapter.notifyDataSetChanged();
                String subject=subjectArray.get(i);
                requestList(subject);
                //requestRecommendList(i);

            }


        });
        schAdapter.setTextviewclick(new TagAdapter.Textviewclick() {
            @Override
            public void textviewclick(int i2) {
                Toast.makeText(getContext(),""+i2,Toast.LENGTH_SHORT).show();


            }
        });
        //Log.d("mhandpicklistlength","cahng123"+mHandpickList.size());
       /* gethomeworkUtil.geturl(i1);
        //mHandpickList.clear();
        mHandpickList=gethomeworkUtil.getHomeworklist();
        mRecommendList=gethomeworkUtil.getHomeworklist();
        Log.d("zhielishi","changdu"+mRecommendList.size()+mHandpickList.size());
        requestHandpickList(mHandpickList);
        requestRecommendList(mRecommendList);*/
    }



    @OnClick({R.id.imageview1})
    void button1Click(View view) {

        if (!subexpand) {
            subAdapter = new TagAdapter(getContext(), setsubList2());
            subrecyclerView.setAdapter(subAdapter);
            getselected();
            subAdapter.notifyDataSetChanged();
            subexpand = true;
        } else {
            subAdapter = new TagAdapter(getContext(), setsubList1());
            subrecyclerView.setAdapter(subAdapter);
            getselected();
            subAdapter.notifyDataSetChanged();
            subexpand=false;
        }
    }
    @OnClick({R.id.imageview2})
    void button2Click(View view) {

        if (schexpand == false) {
            schAdapter = new TagAdapter(getContext(), setschList2());
            schrecyclerView.setAdapter(schAdapter);
            getselected();
            schAdapter.notifyDataSetChanged();
            schexpand = true;
        } else {
            schAdapter = new TagAdapter(getContext(), setschList1());
            schrecyclerView.setAdapter(schAdapter);
            getselected();
            schAdapter.notifyDataSetChanged();
            schexpand=false;
        }

    }
    private void requestList(String mSubject) {
        int uid = 1;
        //从网上获取数据
        String uri = "http://122.204.82.130:5831/getpic?uid=" + uid + "&type=" + mSubject;
        Log.d("bFragment","要获取数据的科目为："  + mSubject);
        boolean isWifi = NetWorkUtils.isWifiConnected(getContext());
        Log.d("bFragment","当前wifi状态 isWifi  " + isWifi);
        //检测当前Wifi是否可用，不可用直接获取本地数据
       /* if (isWifi){

            WebClient.init();
            WebClient.get(uri, new Callback() {

                List<HomeWorkBean> contentList = new ArrayList<>();
                @Override
                public void onFailure(Call call, IOException e){
                    ToastUtils.showShort("返回的科目为" + mSubject);
                    Log.d("bFragment","迟到的科目为 " + mSubject);
                    ToastUtils.showShort("返回数据失败");
                    Log.d("bFragment","返回数据失败" );
                    //Log.d("bFragment","迟到contentList里的部分数据" + mContentList.get(0).getTime());
                    //Log.d("bFragment","迟到contentList里的部分数据" + mContentList.get(0).getSubject());

                    //失败的话调用数据库里的数据
                    contentList = SqlUtil.queryOnSubject(mSubject);
//                    Message msg = new Message();
//                    msg.what = COMPLETED;
//                    handler.sendMessage(msg);

                    // showContent(contentList,mSubject);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    String responseData = response.body().string();
                    Log.d("bFragment","返回数据成功 responseData" + responseData );
                    //解析Json数据

                    contentList = handlerHomeworkResponse(contentList,responseData);
                    //下面这些只是为了Log打印 和保存数据
                    HomeWorkBean[] contentHomeworkBeans = new HomeWorkBean[contentList.size()];
                    contentHomeworkBeans = contentList.toArray(new HomeWorkBean[contentList.size()]);
                    for (HomeWorkBean homeWorkBean :contentHomeworkBeans){
                        Log.d("bFragment","ImagePath" + homeWorkBean.getImagePath());
                        Log.d("bFragment","time" + homeWorkBean.getTime());
                        Log.d("bFragment","subject" + homeWorkBean.getSubject());
                    }
                    SqlUtil.saveAll(contentHomeworkBeans);
                    Log.d("bFragment","保存成功");

                    //展示数据
//                    Message msg = new Message();
//                    msg.what = COMPLETED;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("bFragment","成功进入run方法");
                            showHandpick(contentList,mSubject);
                            showRecommend(contentList,mSubject);
                        }
                    });
                    // handler.sendMessage(msg);
                    //showContent(contentList,mSubject);
                }
            });

        } else{
            List<HomeWorkBean> contentList = SqlUtil.queryOnSubject(mSubject);
            showHandpick(contentList,mSubject);
            showRecommend(contentList,mSubject);
        }*/

    }

    /**
     * 显示试卷数据，用传进来的值加载到RecyclerView中
     * @param contentList
     */
    private void showHandpick(List<HomeWorkBean> contentList,String subject) {

        Log.d("bFragment","成功进入showContent()");
        //ContentAdapter mContentAdapter;
        //开始显示ContentRecyclerView
        if (mHandpickAdapter == null){
            mHandpickAdapter= new HandpickAdapter(getContext(),contentList);
            Log.d("bFragment","contentAdapter为空");
        } else {
            mHandpickAdapter.clear();
            mHandpickAdapter.addData(contentList);
            Log.d("bFragment","clear");
        }
        Log.d("bFragment","contentRecyclerview "+handpickRecyclerView.toString());

        mHandpickAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //传第几项进去和这一组的错题进去
                Intent intent = new Intent(getActivity(), HomeworkContentActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("subject",subject);
                startActivity(intent);
            }
        });
        handpickRecyclerView.setAdapter(mHandpickAdapter);
        handpickRecyclerView.addItemDecoration(new DividerUtil());                      //添加分割线
        handpickRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void showRecommend(List<HomeWorkBean> contentList,String subject) {

        Log.d("bFragment","成功进入showContent()");
        //ContentAdapter mContentAdapter;
        //开始显示ContentRecyclerView
        if (mRecommendAdapter == null){
            mRecommendAdapter= new RecommendAdapter(getContext(),contentList);
            Log.d("bFragment","contentAdapter为空");
        } else {
            mRecommendAdapter.clear();
            mRecommendAdapter.addData(contentList);
            Log.d("bFragment","clear");
        }
        Log.d("bFragment","contentRecyclerview "+recommendRecyclerView.toString());

        mRecommendAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //传第几项进去和这一组的错题进去
                Intent intent = new Intent(getActivity(), HomeworkContentActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("subject",subject);
                startActivity(intent);
            }
        });
        recommendRecyclerView.setAdapter(mRecommendAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommendRecyclerView.setLayoutManager(linearLayoutManager);
    }


    /**
     * 处理json数据
     * @param responseData
     */
    private List<HomeWorkBean> handlerHomeworkResponse(List<HomeWorkBean> contentList,String responseData) {
        Gson gson  = new Gson();
        List<HomeworkNet> NetDataList = gson.fromJson(responseData,new TypeToken<List<HomeworkNet>>(){}.getType());
        String month = getCurrentMonth();
//        for (HomeworkNet homeworkNet : NetDataList ) {
//            HomeWorkBean homeWorkBean = new HomeWorkBean(homeworkNet.getImg_path(), homeworkNet.getType(), homeworkNet.getEdu_level(), homeworkNet.getGrade(),
//                    homeworkNet.getDifficult(), 0, homeworkNet.getTime(), month, homeworkNet.getType());
//            Log.d("Seven","服务器上的imagePath为" + homeworkNet.getImg_path());
//            contentList.add(homeWorkBean);
//            Log.d("Seven","contentList里的部分数据" + contentList.get(0).getTime());
//        }
        return contentList;
    }

    private String getCurrentMonth() {
        String  currentMonth = "2018年9月";
        return currentMonth;
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("bFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("bFragment");
    }
}

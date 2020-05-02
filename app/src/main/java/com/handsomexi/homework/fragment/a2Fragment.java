package com.handsomexi.homework.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsomexi.homework.R;
import com.handsomexi.homework.activity.HomeworkContentActivity;
import com.handsomexi.homework.adapter.BaseAdapter;
import com.handsomexi.homework.adapter.ContentAdapter;
import com.handsomexi.homework.adapter.SubjectAdapter;
import com.handsomexi.homework.bean.HomeWorkBean;
import com.handsomexi.homework.bean.HomeworkNet;

import com.handsomexi.homework.bean.NetBean;
import com.handsomexi.homework.bean.subject;
import com.handsomexi.homework.util.DividerUtil;
import com.handsomexi.homework.util.NetWorkUtils;
import com.handsomexi.homework.util.SqlUtil;
import com.handsomexi.homework.util.Util;
import com.handsomexi.homework.util.WebClient;
import com.handsomexi.homework.view.IndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class a2Fragment extends Fragment {

    private static final int COMPLETED = 0;

    private ContentAdapter mContentAdapter;
    private boolean isNet = false;
    private List<subject> mSubjectList = new ArrayList<>();



    @BindView(R.id.indicatorView)
    IndicatorView indicatorView;
    @BindView(R.id.subject_recycle__list)
    RecyclerView subjectRecyclerView;
    @BindView(R.id.content_recycler_list)
    RecyclerView contentRecyclerView;


    @Override
    public boolean getAllowEnterTransitionOverlap() {
        return super.getAllowEnterTransitionOverlap();
    }


    private   Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a2, null);
        Unbinder unbinder = ButterKnife.bind(this,view);
        initBean();
        showSubject(mSubjectList);
        //选择默认的科目进行显示
        if (mSubjectList != null && mSubjectList.size() != 0){
            subject Subject = mSubjectList.get(0);
            Log.d("Seven","OnCreateView的requestContentList()");
            requestContentList(Subject);
        }
        return view;
    }


    private void initBean() {
        //从数据库中提取出数据，分别初始化mSubjectList，homeWorkBeanList
        //homeWorkBeanList = SqlUtil.query("全部","全部","全部");
        mSubjectList = SqlUtil.getSub();
    }

    private void showSubject(List<subject> subjectList) {

        SubjectAdapter mSubjectAdpter = new SubjectAdapter(getContext(),subjectList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        //设置indicatorView初始位置
        mSubjectAdpter.setOnBinding(new SubjectAdapter.OnBinding() {
            @Override
            public void onBinding() {
                View child = manager.findViewByPosition(0);
                if (child != null){
                    indicatorView.openAnimator(child);
                }
            }
        });

        mSubjectAdpter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                indicatorView.openAnimator(view);
                //mContentList.clear();
                //Log.d("Seven","重新选择另一科目后contentList的长度" + mContentList.size());
                subject Subject = mSubjectAdpter.getItem(position);
                // requestContentList(mSubject);
                requestContentList(Subject);
            }
        });
        subjectRecyclerView.setAdapter(mSubjectAdpter);
        subjectRecyclerView.setLayoutManager(manager);

    }

    private void requestContentList(subject mSubject) {
        //int uid = 1;
        String name = "习大大";
        String pawd = "测试";
        //从网上获取数据
        String uri = "http://122.204.82.130:8080/wwh/question?name=" + name+"&pawd="+pawd+"&type=" +mSubject.getSubject()  ;
        Log.d("Seven","要获取数据的科目为："  + mSubject.getSubject());
        boolean isWifi = NetWorkUtils.isWifiConnected(getContext());
        Log.d("Seven","当前wifi状态 isWifi  " + isWifi);
        //检测当前Wifi是否可用，不可用直接获取本地数据
        if (isWifi){
            WebClient.init();
            WebClient.get(uri, new Callback() {

                List<HomeWorkBean> contentList = new ArrayList<>();
                @Override
                public void onFailure(Call call, IOException e){
                    ToastUtils.showShort("返回的科目为" + mSubject.getSubject());
                    Log.d("Seven","迟到的科目为 " + mSubject.getSubject());
                    ToastUtils.showShort("返回数据失败");
                    Log.d("Seven","返回数据失败" );
                    //失败的话调用数据库里的数据
                    contentList = SqlUtil.queryOnSubject(mSubject.getSubject());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    if (response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("Seven", "返回数据成功 responseData" + responseData);
                        //解析Json数据
                        // TODO: 2019/1/6 加一个判断，判断数据是否正确 
                        HomeworkNet homeworkNet = new Gson().fromJson(responseData, HomeworkNet.class);
                        List<HomeworkNet.DataBean> data = homeworkNet.getData();
                        //转换为本地bean
                        contentList = Util.handlerHomeworkResponse(data);
                    }
                    //打印数据
                    logData(contentList);
//                        //展示数据
                   Message msg = new Message();
                   msg.what = COMPLETED;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Seven","成功进入run方法");
                            showContent(contentList,mSubject);
                        }
                    });

                }
            });

        } else{
            List<HomeWorkBean> contentList = SqlUtil.queryOnSubject(mSubject.getSubject());
            showContent(contentList,mSubject);
        }

    }

    private void logData(List<HomeWorkBean> contentList) {
        //下面这些只是为了Log打印 和保存数据
        HomeWorkBean[] contentHomeworkBeans = new HomeWorkBean[contentList.size()];
        contentHomeworkBeans = contentList.toArray(new HomeWorkBean[contentList.size()]);
        for (HomeWorkBean homeWorkBean :contentHomeworkBeans){
            Log.d("Seven","ImagePath" + homeWorkBean.getImagePath());
            Log.d("Seven","time" + homeWorkBean.getTime());
            Log.d("Seven","subject" + homeWorkBean.getSubject());
        }
        SqlUtil.saveAll(contentHomeworkBeans);
        Log.d("Seven","保存成功");
    }

    /**
     * 显示试卷数据，用传进来的值加载到RecyclerView中
     * @param contentList
     */
    private void showContent(List<HomeWorkBean> contentList,subject subject) {

        Log.d("seven","成功进入showContent()");
        //开始显示ContentRecyclerView
        if (mContentAdapter == null){
            mContentAdapter = new ContentAdapter(getContext(),contentList);
            Log.d("Seven","contentAdapter为空");
        } else {
            mContentAdapter.clear();
            mContentAdapter.addData(contentList);
            Log.d("Seven","clear");
        }
        Log.d("Seven","contentRecyclerview "+ contentRecyclerView.toString());

        mContentAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //传第几项进去和这一组的错题进去
                Intent intent = new Intent(getActivity(), HomeworkContentActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("subject",subject.getSubject());
                startActivity(intent);
            }
        });

        contentRecyclerView.setAdapter(mContentAdapter);
        contentRecyclerView.addItemDecoration(new DividerUtil());                      //添加分割线
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        isFrist = true;
        Log.d("Seven","onCreate" );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Seven","onAttach" );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Seven","onStart");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Seven","onResume" );

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Seven","onPause" );

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Seven","onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Seven","onDestroy");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Seven","onDetach" );

    }


}

package com.handsomexi.homework.fragment;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public class cFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("cFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("cFragment");
    }
}

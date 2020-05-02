package com.handsomexi.homework.activity.base;

import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SwipBaseActivity extends SwipeBackActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

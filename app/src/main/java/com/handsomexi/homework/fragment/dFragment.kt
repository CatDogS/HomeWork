package com.handsomexi.homework.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handsomexi.homework.R
import com.handsomexi.homework.activity.mine.AboutActivity
import com.handsomexi.homework.activity.mine.FunctionActivity
import com.handsomexi.homework.activity.mine.InformationActivity
import com.handsomexi.homework.activity.mine.MemberActivity
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_d.*

class dFragment : Fragment(),View.OnClickListener{

    override fun onClick(view: View) {
        var intent: Intent? = null
        when (view.id) {
            R.id.member -> intent = Intent(activity, MemberActivity::class.java)
            R.id.function -> intent = Intent(activity, FunctionActivity::class.java)
            R.id.information -> intent = Intent(activity, InformationActivity::class.java)
            R.id.about -> intent = Intent(activity, AboutActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_d, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        member.setOnClickListener(this)
        function.setOnClickListener(this)
        information.setOnClickListener(this)
        about.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("dFragment")
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd("dFragment")
    }
}

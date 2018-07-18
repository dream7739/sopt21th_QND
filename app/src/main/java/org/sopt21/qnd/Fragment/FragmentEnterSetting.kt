package org.sopt21.qnd.Fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Switch
import org.sopt21.qnd.Activity.LoginActivity
import org.sopt21.qnd.R



/**
 * Created by taehyung on 2018-01-06.
 */

class FragmentEnterSetting : Fragment(), View.OnClickListener {

    internal lateinit var rl_profile_change: RelativeLayout
    internal lateinit var rl_passwd_change: RelativeLayout
    internal lateinit var rl_alarm_change: RelativeLayout
    internal lateinit var rl_logout: RelativeLayout
    internal lateinit var rl_withdrawal: RelativeLayout
    internal lateinit var rl_help: RelativeLayout
    internal lateinit var rl_report: RelativeLayout
    internal lateinit var btn_back: RelativeLayout

    internal lateinit var switch_lock: Switch

    internal lateinit var mFragment: Fragment
    internal lateinit var fm: FragmentManager
    internal lateinit var fragmentHelp: FragmentHelp
    internal lateinit var fragmentPasswdChange: FragmentPasswdChange
    internal lateinit var fragmentProfileChange: FragmentProfileChange
    internal lateinit var fragmentReport: FragmentReport

    var loginInfo: SharedPreferences? = null

    var switch_lock_checked: Boolean? = true
//    lateinit var lockInfo: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_setting, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
//        lockInfo = activity.getSharedPreferences("lockInfo", 0)

        rl_profile_change = view.findViewById(R.id.rl_profile_change) as RelativeLayout
        rl_passwd_change = view.findViewById(R.id.rl_passwd_change) as RelativeLayout
        rl_alarm_change = view.findViewById(R.id.rl_alarm_change) as RelativeLayout
        rl_logout = view.findViewById(R.id.rl_logout) as RelativeLayout
        rl_withdrawal = view.findViewById(R.id.rl_withdrawal) as RelativeLayout
        rl_help = view.findViewById(R.id.rl_help) as RelativeLayout
        rl_report = view.findViewById(R.id.rl_report) as RelativeLayout
        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout

        switch_lock = view.findViewById(R.id.switch_lock) as Switch

        /*lockInfo = activity.getSharedPreferences("lockInfo", 0)
        switch_lock_checked = lockInfo.getBoolean("check_lock", false)

        switch_lock.isChecked = switch_lock_checked as Boolean*/

        rl_profile_change.setOnClickListener(this)
        rl_passwd_change.setOnClickListener(this)
        rl_alarm_change.setOnClickListener(this)
        rl_logout.setOnClickListener(this)
        rl_withdrawal.setOnClickListener(this)
        rl_help.setOnClickListener(this)
        rl_report.setOnClickListener(this)
        btn_back.setOnClickListener(this)

        switch_lock.setOnClickListener(this)

        fragmentProfileChange = FragmentProfileChange()
        fragmentPasswdChange = FragmentPasswdChange()
        fragmentHelp = FragmentHelp()
        fragmentReport = FragmentReport()

        return view
    }

    override fun onClick(v: View) {
        fm = activity.supportFragmentManager

        when (v.id) {
            R.id.rl_profile_change -> {
                mFragment = Fragment.instantiate(context, fragmentProfileChange.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.rl_passwd_change -> {
                mFragment = Fragment.instantiate(context, fragmentPasswdChange.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.rl_alarm_change -> {
            }
            R.id.rl_logout -> {
                var editor: SharedPreferences.Editor = loginInfo!!.edit()
                editor.clear()
                editor.commit()

                var intent: Intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity.finish()

            }
            R.id.rl_withdrawal -> {
            }
            R.id.rl_help -> {
                mFragment = Fragment.instantiate(context, fragmentHelp.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.rl_report -> {
                mFragment = Fragment.instantiate(context, fragmentReport.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.btn_back -> {
                fm.beginTransaction().remove(this).commit()
                fm.popBackStack()
            }

        }
    }
}

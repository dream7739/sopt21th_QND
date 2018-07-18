package org.sopt21.qnd.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import de.hdodenhof.circleimageview.CircleImageView
import org.sopt21.qnd.Data.LoginResult
import org.sopt21.qnd.R

class Fragment_Profile : Fragment(), View.OnClickListener {
    internal lateinit var userData: LoginResult

    internal lateinit var tv_email: TextView
    internal lateinit var tv_age: TextView
    internal lateinit var tv_gender: TextView
    internal lateinit var tv_job: TextView
    internal lateinit var str_gender: String

    internal lateinit var rl_con_survey: RelativeLayout
    internal lateinit var rl_maked_survey: RelativeLayout
    internal lateinit var rl_purchased_survey: RelativeLayout
    internal lateinit var btn_setting: ImageButton

    internal lateinit var mFragment: Fragment
    internal lateinit var fragmentMySurveyList: FragmentMySurveyList
    internal lateinit var fragmentMakeSurveyList: FragmentMakedSurveyList
    internal lateinit var fragmentPurchaseSurveyList: FragmentPurchaseSurveyList
    internal lateinit var fragmentEnterSetting: FragmentEnterSetting

    internal lateinit var fm: FragmentManager
    internal lateinit var tv_total_coin: TextView
    internal lateinit var tv_available_coin: TextView
    internal lateinit var tv_used_coin: TextView

    internal lateinit var switch_lock: Switch

    internal lateinit var iv_profile: CircleImageView

    lateinit var loginInfo: SharedPreferences
    var user_id: String? = null

    var user_available_point: Int = 0
    var user_used_point: Int = 0
    var user_accumulate_point: Int = 0
    var user_img: String? = null
    var user_email: String? = null

    var switch_lock_checked: Boolean? = true
    lateinit var lockInfo: SharedPreferences

    private var requestManager : RequestManager? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo.getString("user_id", "")

        if(arguments != null) {
            user_available_point = arguments.getInt("user_available_point")
            user_used_point = arguments.getInt("user_used_point")
            user_accumulate_point = arguments.getInt("user_accumulate_point")
            user_img = arguments.getString("user_img")
            user_email = arguments.getString("user_email")
        }

        fragmentMySurveyList = FragmentMySurveyList()
        fragmentMakeSurveyList = FragmentMakedSurveyList()
        fragmentPurchaseSurveyList = FragmentPurchaseSurveyList()
        fragmentEnterSetting = FragmentEnterSetting()

        fm = activity.supportFragmentManager

        tv_email = view.findViewById(R.id.tv_email) as TextView
        tv_age = view.findViewById(R.id.tv_age) as TextView
        tv_gender = view.findViewById(R.id.tv_gender) as TextView
        tv_job = view.findViewById(R.id.tv_job) as TextView

        rl_con_survey = view.findViewById(R.id.rl_con_survey) as RelativeLayout
        rl_maked_survey = view.findViewById(R.id.rl_maked_survey) as RelativeLayout
        rl_purchased_survey = view.findViewById(R.id.rl_purchased_survey) as RelativeLayout
        btn_setting = view.findViewById(R.id.btn_setting) as ImageButton

        tv_total_coin = view.findViewById(R.id.total_coin) as TextView
        tv_available_coin = view.findViewById(R.id.available_coin) as TextView
        tv_used_coin = view.findViewById(R.id.used_coin) as TextView

        iv_profile = view.findViewById(R.id.iv_profile) as CircleImageView
        requestManager = Glide.with(activity)
        requestManager!!.load(user_img).into(iv_profile)

        tv_email.text = user_email
        tv_total_coin.text = user_accumulate_point.toString()
        tv_used_coin.text = user_used_point.toString()
        tv_available_coin.text = user_available_point.toString()

        rl_con_survey.setOnClickListener(this)
        rl_maked_survey.setOnClickListener(this)
        rl_purchased_survey.setOnClickListener(this)
        btn_setting.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_con_survey -> {
                mFragment = Fragment.instantiate(context, fragmentMySurveyList.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.rl_maked_survey -> {
                mFragment = Fragment.instantiate(context, fragmentMakeSurveyList.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.rl_purchased_survey -> {
                mFragment = Fragment.instantiate(context, fragmentPurchaseSurveyList.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }
            R.id.btn_setting -> {
                mFragment = Fragment.instantiate(context, fragmentEnterSetting.javaClass.name)
                fm.beginTransaction().add(R.id.make_container, mFragment).commit()
            }

        }
    }
}

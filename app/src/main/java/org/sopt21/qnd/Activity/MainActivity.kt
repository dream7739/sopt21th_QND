package org.sopt21.qnd.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Application.ApplicationController.Companion.context
import org.sopt21.qnd.Data.MyProfileInfo
import org.sopt21.qnd.Fragment.*
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var tabLayout: TabLayout
    private lateinit var fragment_make: FragmentMake
    private lateinit var fragment_survey: FragmentSurvey
    private lateinit var fragment_profile: Fragment_Profile
    private lateinit var fragment_stroe: FragmentStore
    private lateinit var fragment_search: FragmentSearch

    private lateinit var tabView1: View
    private lateinit var tabView2: View
    private lateinit var tabView3: View
    private lateinit var tabView4: View
    private lateinit var tabView5: View

    internal lateinit var intent: Intent

    private var mFragment: Fragment? = null
    //두번 클릭 여부 확인용
    private var backPressedTime: Long = 0
    private val FINSH_INTERVAL_TIME: Long = 2000

    lateinit var loginInfo: SharedPreferences
    var user_id: String? = null

    var bundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginInfo = this.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo.getString("user_id", "")

        Log.v("t_Main", user_id)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = this.resources.getColor(R.color.statusbar)
        }


        tabLayout = findViewById(R.id.tablayout) as TabLayout

        tabView1 = LayoutInflater.from(this).inflate(R.layout.tab_icon1, null)
        tabView2 = LayoutInflater.from(this).inflate(R.layout.tab_icon2, null)
        tabView3 = LayoutInflater.from(this).inflate(R.layout.tab_icon3, null)
        tabView4 = LayoutInflater.from(this).inflate(R.layout.tab_icon4, null)
        tabView5 = LayoutInflater.from(this).inflate(R.layout.tab_icon5, null)

        val tab_img1 = tabView1.findViewById(R.id.tab_icon1) as ImageView
        val tab_img2 = tabView2.findViewById(R.id.tab_icon2) as ImageView
        val tab_img3 = tabView3.findViewById(R.id.tab_icon3) as ImageView
        val tab_img4 = tabView4.findViewById(R.id.tab_icon4) as ImageView
        val tab_img5 = tabView5.findViewById(R.id.tab_icon5) as ImageView

        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView1))
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView2))
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView3))
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView4))
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView5))

        fragment_make = FragmentMake() //메이크
        fragment_profile = Fragment_Profile() //프로필
        fragment_survey = supportFragmentManager.findFragmentById(R.id.fragment) as FragmentSurvey
        fragment_stroe = FragmentStore()
        fragment_search = FragmentSearch()//검색

        getMyProfileInfoNetwork()

        val fm = supportFragmentManager


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position

                when (position) {
                    0 -> {
                        tab_img1.setImageResource(R.drawable.surveyfill)
                        tab_img2.setImageResource(R.drawable.make)
                        tab_img3.setImageResource(R.drawable.serch)
                        tab_img4.setImageResource(R.drawable.purchase)
                        tab_img5.setImageResource(R.drawable.profile)
                        mFragment = Fragment.instantiate(applicationContext, fragment_survey.javaClass.getName())
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()
                    }

                    1 -> {
                        tab_img1.setImageResource(R.drawable.survey)
                        tab_img2.setImageResource(R.drawable.makefill)
                        tab_img3.setImageResource(R.drawable.serch)
                        tab_img4.setImageResource(R.drawable.purchase)
                        tab_img5.setImageResource(R.drawable.profile)
                        mFragment = Fragment.instantiate(applicationContext, fragment_make.javaClass.getName())
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()
                    }
                    2 -> {
                        tab_img1.setImageResource(R.drawable.survey)
                        tab_img2.setImageResource(R.drawable.make)
                        tab_img3.setImageResource(R.drawable.serchfill)
                        tab_img4.setImageResource(R.drawable.purchase)
                        tab_img5.setImageResource(R.drawable.profile)
                        mFragment = Fragment.instantiate(applicationContext, fragment_search.javaClass.getName())
                        Log.v("taehyung_main", mFragment.toString())
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()
                    }
                    3 -> {
                        tab_img1.setImageResource(R.drawable.survey)
                        tab_img2.setImageResource(R.drawable.make)
                        tab_img3.setImageResource(R.drawable.serch)
                        tab_img4.setImageResource(R.drawable.purchasefill)
                        tab_img5.setImageResource(R.drawable.profile)
                        mFragment = Fragment.instantiate(applicationContext, fragment_stroe.javaClass.getName())
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()
                    }
                    4 -> {
                        tab_img1.setImageResource(R.drawable.survey)
                        tab_img2.setImageResource(R.drawable.make)
                        tab_img3.setImageResource(R.drawable.serch)
                        tab_img4.setImageResource(R.drawable.purchase)
                        tab_img5.setImageResource(R.drawable.profilefill)
                        mFragment = Fragment.instantiate(applicationContext, fragment_profile.javaClass.getName())
                        mFragment!!.arguments = bundle
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (mFragment != null) {
                    val fm = supportFragmentManager
                    fm.beginTransaction().detach(mFragment).commit()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        /**
         * Back키 두번 연속 클릭 시 앱 종료
         */
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMyProfileInfoNetwork() {
        val networkService = ApplicationController.instance!!.networkService

        val myProfileInfoCallback = networkService!!.getMyProfile(user_id)
        myProfileInfoCallback.enqueue(object : Callback<MyProfileInfo> {
            override fun onResponse(call: Call<MyProfileInfo>, response: Response<MyProfileInfo>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") { //로그인 성공시 리얼메인액티비티로
                        var user_available_point: Int = response.body().data.user_available_point
                        var user_used_point: Int = response.body().data.user_used_point
                        var user_accumulate_point: Int = response.body().data.user_accumulate_point
                        var user_img: String? = response.body().data.user_img
                        var user_email: String? = response.body().data.user_email

                        bundle.putInt("user_available_point", user_available_point)
                        bundle.putInt("user_used_point", user_used_point)
                        bundle.putInt("user_accumulate_point", user_accumulate_point)
                        bundle.putString("user_img", user_img)
                        bundle.putString("user_email", user_email)
                    }
                } else {
                    Toast.makeText(context, "만든 설문이 없습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyProfileInfo>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
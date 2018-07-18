package org.sopt21.qnd.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import org.sopt21.qnd.Adpater.MyReportRecyclerAdapter
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.GetMySurveyListResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentReport : Fragment(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    private var mDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>? = null
    private var adapter: MyReportRecyclerAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    internal lateinit var btn_back: RelativeLayout
    internal lateinit var mFragment: Fragment

    var clickEvent: View.OnClickListener = View.OnClickListener { }

    internal var loginInfo: SharedPreferences? = null
    internal var user_id: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_report, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo!!.getString("user_id", "")

        Log.v("taehyungReport token: ", user_id)

        mFragment = fragmentManager.findFragmentById(R.id.fragment)
        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout
        btn_back.setOnClickListener(this)

        recyclerView = view.findViewById(R.id.myreport_recycerview) as RecyclerView
        mLayoutManager = LinearLayoutManager(context)
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = mLayoutManager

        mDatas = ArrayList()
        //신고내역 통신 호출
        MyReportListNetwork()

        return view
    }

    //신고내역통신
    fun MyReportListNetwork(){
        val networkService = ApplicationController.instance!!.networkService

        val reportListCallback = networkService!!.getReportSurvey(user_id)
        reportListCallback.enqueue(object : Callback<GetMySurveyListResult> {
            override fun onResponse(call: Call<GetMySurveyListResult>, response: Response<GetMySurveyListResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") { //로그인 성공시 리얼메인액티비티로
                        mDatas = response.body().data
                        adapter = MyReportRecyclerAdapter(mDatas, context, clickEvent)
                        recyclerView!!.adapter = adapter
                    }
                } else {
                    Log.v("taehyung__", response.isSuccessful.toString())
                    Toast.makeText(context, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetMySurveyListResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View) {
        val fm = activity.supportFragmentManager

        when (v.id) {
            R.id.btn_back -> {
                fm.beginTransaction().remove(this).commit()
                fm.popBackStack()
            }
        }
    }

}

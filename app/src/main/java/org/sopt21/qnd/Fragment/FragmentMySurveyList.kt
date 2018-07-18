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
import org.sopt21.qnd.Adpater.MySurveyRecyclerAdapter
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.GetMySurveyListResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by taehyung on 2018-01-06.
 */

class FragmentMySurveyList : Fragment(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    private var mDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>? = null
    private var adapter: MySurveyRecyclerAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    internal lateinit var btn_back: RelativeLayout

    internal lateinit var mFragment: Fragment

    internal var loginInfo: SharedPreferences? = null
    internal var user_id: String? = null

    //참여내역 불러오는 통신
    /*    public void getMySurveyList(){


        NetworkService networkService = ApplicationController.getInstance().getNetworkService();
        Call<GetMySurveyListResult> requestMySurveyList = networkService.getMySurveyList(loginDatas);
        requestMySurveyList.enqueue(new Callback<GetMySurveyListResult>() {
            @Override
            public void onResponse(Call<GetMySurveyListResult> call, Response<GetMySurveyListResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().status.equals("Success")) {
                        MySurveyListData.SurveyData data = new MySurveyListData.SurveyData();
                        data.servay_title = "롱패딩 화이트 vs 블랙";
                        for(int i =0; i<10; i++) {
                            mDatas.add(data);
                        }

                        adapter = new MySurveyRecyclerAdapter(mDatas, getContext(), clickEvent);
                        recyclerView.setAdapter(adapter);
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디 혹은 암호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMySurveyListResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    var clickEvent: View.OnClickListener = View.OnClickListener { }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_mysurvey, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo!!.getString("user_id", "")

        Log.v("taehyungMySurv token: ", user_id)

        mFragment = fragmentManager.findFragmentById(R.id.fragment)

        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout

        recyclerView = view.findViewById(R.id.mysurvey_recycerview) as RecyclerView

        mLayoutManager = LinearLayoutManager(context)
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = mLayoutManager

        btn_back.setOnClickListener(this)

        mDatas = ArrayList()

        //참여내역 통신 호출
        getMySurveyListNetwork()

        return view
    }

    fun getMySurveyListNetwork(){
        val networkService = ApplicationController.instance!!.networkService

//        val userIdData = UserIdData()
//        userIdData.token = user_id

        val mySurveyListCallback = networkService!!.getMySurveyList(user_id)
        mySurveyListCallback.enqueue(object : Callback<GetMySurveyListResult> {
            override fun onResponse(call: Call<GetMySurveyListResult>, response: Response<GetMySurveyListResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") {
                        mDatas = response.body().data
                        adapter = MySurveyRecyclerAdapter(mDatas, context, clickEvent)
                        recyclerView!!.adapter = adapter
                    }
                } else {
                    Toast.makeText(context, "참여한 설문이 없습니다", Toast.LENGTH_SHORT).show()
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

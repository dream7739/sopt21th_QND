package org.sopt21.qnd.Dialog

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sopt21.qnd.Activity.MainActivity
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.MakeSurveyResult
import org.sopt21.qnd.Fragment.FragmentMake
import org.sopt21.qnd.PicsData
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogMake : DialogFragment() {
    lateinit var loginInfo: SharedPreferences

    internal var user_id: String? = null
    internal var servay_servay_type = 0
    internal var servay_valid_period = 0
    internal var servay_goal = 0
    internal var servay_anonymous = 0
    internal var servay_start_age = 0
    internal var servay_end_age = 0
    internal var servay_gender = 0
    internal var servay_marriage = 0
    internal var servay_option_count = 0
    internal var servay_tag_count = 0
    internal var servay_q_count = 0
    internal var servay_duple = 0

    internal var servay_title: String? = null
    internal var servay_tag1: String? = null
    internal var servay_tag2: String? = null
    internal var servay_tag3: String? = null
    internal var servay_explanation: String? = null
    internal var servay_q1: String? = null
    internal var servay_q2: String? = null
    internal var servay_q3: String? = null
    internal var servay_q4: String? = null
    internal var servay_a_txt: String? = null
    internal var servay_b_txt: String? = null
    internal lateinit var ok_btn: RelativeLayout
    internal lateinit var cancle_btn: RelativeLayout
    internal lateinit var coin_tv: TextView

    internal lateinit var fragment_make: FragmentMake

    internal var input_servay_q1: RequestBody? = null
    internal var input_servay_q2: RequestBody? = null
    internal var input_servay_q3: RequestBody? = null
    internal var input_servay_q4: RequestBody? = null

    internal var input_servay_q_count: RequestBody? = null
    internal var input_servay_duple: RequestBody? = null
    internal var input_servay_a_txt: RequestBody? = null
    internal var input_servay_b_txt: RequestBody? = null

    private var image_pics: ArrayList<MultipartBody.Part> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.dialog_make, container)

        fragment_make = FragmentMake()

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo.getString("user_id","")

        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        ok_btn = view.findViewById(R.id.dialog_make_ok) as RelativeLayout
        cancle_btn = view.findViewById(R.id.dialog_make_cancle) as RelativeLayout
        coin_tv = view.findViewById(R.id.dialog_make_coin) as TextView

        if (arguments != null) {
            servay_servay_type = arguments.getInt("servay_servay_type")
            servay_valid_period = arguments.getInt("servay_valid_period")
            servay_goal = arguments.getInt("servay_goal")
            servay_anonymous = arguments.getInt("servay_anonymous")
            servay_start_age = arguments.getInt("servay_start_age")
            servay_end_age = arguments.getInt("servay_end_age")
            servay_gender = arguments.getInt("servay_gender")
            servay_marriage = arguments.getInt("servay_marriage")
            servay_option_count = arguments.getInt("servay_option_count")
            servay_tag_count = arguments.getInt("servay_tag_count")
            servay_q_count = arguments.getInt("servay_q_count")
            servay_duple = arguments.getInt("servay_duple")

            servay_title = arguments.getString("servay_title")
            servay_tag1 = arguments.getString("servay_tag1")
            servay_tag2 = arguments.getString("servay_tag2")
            servay_tag3 = arguments.getString("servay_tag3")
            servay_explanation = arguments.getString("servay_explanation")
            servay_q1 = arguments.getString("servay_q1")
            servay_q2 = arguments.getString("servay_q2")
            servay_q3 = arguments.getString("servay_q3")
            servay_q4 = arguments.getString("servay_q4")
            servay_a_txt = arguments.getString("servay_a_txt")
            servay_b_txt = arguments.getString("servay_b_txt")
        }

        if (servay_option_count == 0) {
            coin_tv.text = "20"
        } else if (servay_option_count == 1) {
            coin_tv.text = "30"
        } else if (servay_option_count == 2) {
            coin_tv.text = "40"
        }

        ok_btn.setOnClickListener {
            try {
                //통신 메소드
                makeSurveyNetwork()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        cancle_btn.setOnClickListener { dismiss() }

        return view
    }

    fun makeSurveyNetwork() {
        val networkService = ApplicationController.instance!!.networkService
//        val makeSurveyData = MakeSurveyData()

//        var input_user_id = RequestBody.create(MediaType.parse("text/plain"), user_id)
        var input_servay_servay_type = RequestBody.create(MediaType.parse("text/plain"), servay_servay_type.toString())
        var input_servay_title = RequestBody.create(MediaType.parse("text/plain"), servay_title)
        var input_servay_valid_period = RequestBody.create(MediaType.parse("text/plain"), servay_valid_period.toString())
        var input_servay_goal = RequestBody.create(MediaType.parse("text/plain"), servay_goal.toString())
        var input_servay_anonymous = RequestBody.create(MediaType.parse("text/plain"), servay_anonymous.toString())
        var input_servay_start_age = RequestBody.create(MediaType.parse("text/plain"), servay_start_age.toString())
        var input_servay_end_age = RequestBody.create(MediaType.parse("text/plain"), servay_end_age.toString())
        var input_servay_tag1 = RequestBody.create(MediaType.parse("text/plain"), servay_tag1)
        var input_servay_tag2 = RequestBody.create(MediaType.parse("text/plain"), servay_tag2)
        var input_servay_tag3 = RequestBody.create(MediaType.parse("text/plain"), servay_tag3)
        var input_servay_explanation = RequestBody.create(MediaType.parse("text/plain"), servay_explanation)
        var input_servay_gender = RequestBody.create(MediaType.parse("text/plain"), servay_gender.toString())
        var input_servay_marriage = RequestBody.create(MediaType.parse("text/plain"), servay_marriage.toString())
        var input_servay_option_count = RequestBody.create(MediaType.parse("text/plain"), servay_option_count.toString())
        var input_servay_tag_count = RequestBody.create(MediaType.parse("text/plain"), servay_tag_count.toString())

        /*
        makeSurveyData.data.user_id = user_id
        makeSurveyData.data.servay_servay_type = servay_servay_type
        makeSurveyData.data.servay_title = servay_title
        makeSurveyData.data.servay_valid_period = servay_valid_period
        makeSurveyData.data.servay_goal = servay_goal
        makeSurveyData.data.servay_anonymous = servay_anonymous
        makeSurveyData.data.servay_start_age = servay_start_age
        makeSurveyData.data.servay_end_age = servay_end_age
        makeSurveyData.data.servay_tag1 = servay_tag1
        makeSurveyData.data.servay_tag2 = servay_tag2
        makeSurveyData.data.servay_tag3 = servay_tag3
        makeSurveyData.data.servay_explanation = servay_explanation
        makeSurveyData.data.servay_gender = servay_gender
        makeSurveyData.data.servay_marriage = servay_marriage
        makeSurveyData.data.servay_option_count = servay_option_count
        makeSurveyData.data.servay_tag_count = servay_tag_count
        */


        if (servay_servay_type == 0) { // 객관식일 때
            input_servay_q1 = RequestBody.create(MediaType.parse("text/plain"), servay_q1.toString())
            input_servay_q2 = RequestBody.create(MediaType.parse("text/plain"), servay_q2.toString())
            input_servay_q3 = RequestBody.create(MediaType.parse("text/plain"), servay_q3.toString())
            input_servay_q4 = RequestBody.create(MediaType.parse("text/plain"), servay_q4.toString())

            input_servay_q_count = RequestBody.create(MediaType.parse("text/plain"), servay_q_count.toString())
            input_servay_duple = RequestBody.create(MediaType.parse("text/plain"), servay_duple.toString())

        } else if (servay_servay_type == 1) { // A/B일 때
            input_servay_a_txt = RequestBody.create(MediaType.parse("text/plain"), servay_a_txt)
            input_servay_b_txt = RequestBody.create(MediaType.parse("text/plain"), servay_b_txt)

            image_pics.add(0, PicsData.servay_a_img!!)
            image_pics.add(1, PicsData.servay_b_img!!)
        }

        val makeSurvey = networkService!!.makeSurvey(
                user_id,
                image_pics,
                input_servay_servay_type,
                input_servay_title,
                input_servay_valid_period,
                input_servay_goal,
                input_servay_anonymous,
                input_servay_start_age,
                input_servay_end_age,
                input_servay_tag1,
                input_servay_tag2,
                input_servay_tag3,
                input_servay_explanation,
                input_servay_gender,
                input_servay_marriage,
                input_servay_option_count,
                input_servay_tag_count,
                input_servay_q1,
                input_servay_q2,
                input_servay_q3,
                input_servay_q4,
                input_servay_q_count,
                input_servay_duple,
                input_servay_a_txt,
                input_servay_b_txt
        )

        makeSurvey.enqueue(object : Callback<MakeSurveyResult> {
            override fun onResponse(call: Call<MakeSurveyResult>, response: Response<MakeSurveyResult>) =
                    if (response.isSuccessful) {
                        Toast.makeText(context, "설문이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        dismiss()

                    } else {
                        Toast.makeText(context, "설문이 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        val fm = activity.supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.add(R.id.make_container, fragment_make)
                        transaction.commit()
                        dismiss()
                    }

            override fun onFailure(call: Call<MakeSurveyResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

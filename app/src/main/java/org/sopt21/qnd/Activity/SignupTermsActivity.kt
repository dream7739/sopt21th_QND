package org.sopt21.qnd.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.RequestBody
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.CommonData
import org.sopt21.qnd.Data.RegisterUserAddResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupTermsActivity : AppCompatActivity() {
    internal lateinit var btn_signup_submit: RelativeLayout
    internal lateinit var btn_check_all: ImageButton
    internal lateinit var btn_check1: ImageButton
    internal lateinit var btn_check2: ImageButton
    internal var check_all: Boolean? = false
    internal var check1: Boolean? = false
    internal var check2: Boolean? = false

    var age: Int = 0
    var type: Int = 0
    var email: String? = null
    var gender: Int = 0
    var marraige: Int = 0
    var job: String? = null
    var hometown: String? = null
    var passwd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_terms)

        var intent = intent
        if (intent != null) {
            age = intent.getIntExtra("age", 0)
            type = intent.getIntExtra("type", 0)
            email = intent.getStringExtra("email")
            gender = intent.getIntExtra("gender", 0)
            marraige = intent.getIntExtra("marraige", 0)
            job = intent.getStringExtra("job")
            hometown = intent.getStringExtra("hometown")
            passwd = intent.getStringExtra("passwd")
        }

        btn_check_all = findViewById(R.id.btn_check_all) as ImageButton
        btn_check1 = findViewById(R.id.btn_check1) as ImageButton
        btn_check2 = findViewById(R.id.btn_check2) as ImageButton
        btn_signup_submit = findViewById(R.id.btn_signup_submit) as RelativeLayout

        btn_check_all.setOnClickListener {
            if (!check_all!!) {
                btn_check_all.setImageResource(R.drawable.make_checkbox_on)
                btn_check1.setImageResource(R.drawable.make_checkbox_on)
                btn_check2.setImageResource(R.drawable.make_checkbox_on)
                check1 = true
                check2 = true
                check_all = true
            } else {
                btn_check_all.setImageResource(R.drawable.make_checkbox_off)
                btn_check1.setImageResource(R.drawable.make_checkbox_off)
                btn_check2.setImageResource(R.drawable.make_checkbox_off)
                check1 = false
                check2 = false
                check_all = false
            }
        }

        btn_check1.setOnClickListener {
            if (!check1!!) {
                btn_check1.setImageResource(R.drawable.make_checkbox_on)
                check1 = true
            } else {
                btn_check1.setImageResource(R.drawable.make_checkbox_off)
                btn_check_all.setImageResource(R.drawable.make_checkbox_off)
                check1 = false
            }
        }

        btn_check2.setOnClickListener {
            if (!check2!!) {
                btn_check2.setImageResource(R.drawable.make_checkbox_on)
                check2 = true
            } else {
                btn_check2.setImageResource(R.drawable.make_checkbox_off)
                btn_check_all.setImageResource(R.drawable.make_checkbox_off)
                check2 = false
            }
        }

        btn_signup_submit.setOnClickListener {
            if (check1!! && check2!!) {
                registerUser()
            } else {
                Toast.makeText(applicationContext, "이용약관을 동의해주셔야 회원가입이 완료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //회원가입 완료 통신
    fun registerUser() {
        val networkService = ApplicationController.instance!!.networkService

        var input_type = RequestBody.create(MediaType.parse("text/plain"), type.toString())
        var input_email = RequestBody.create(MediaType.parse("text/plain"), email)
        var input_age = RequestBody.create(MediaType.parse("text/plain"), age.toString())
        var input_gender = RequestBody.create(MediaType.parse("text/plain"), gender.toString())
        var input_marraige = RequestBody.create(MediaType.parse("text/plain"), marraige.toString())
        var input_job = RequestBody.create(MediaType.parse("text/plain"), job)
        var input_hometown = RequestBody.create(MediaType.parse("text/plain"), hometown)
        var input_passwd = RequestBody.create(MediaType.parse("text/plain"), passwd)

        var emailAuth: Call<RegisterUserAddResult>? = null
        if(CommonData.img != null) {
            emailAuth = networkService!!.joinUser(CommonData.img, input_type, input_email, input_age, input_gender,input_marraige, input_job, input_hometown, input_passwd)
        }else{
            emailAuth = networkService!!.joinUser(null, input_type, input_email, input_age, input_gender,input_marraige, input_job, input_hometown, input_passwd)
        }
        emailAuth.enqueue(object : Callback<RegisterUserAddResult> {
            override fun onResponse(call: Call<RegisterUserAddResult>, response: Response<RegisterUserAddResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //TODO response.isSuccessful() 삭제
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterUserAddResult>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

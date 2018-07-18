package org.sopt21.qnd.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.CommonData
import org.sopt21.qnd.Data.RegisterUserAddResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupEtcActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var email: String
    internal lateinit var passwd: String
    internal lateinit var profile: String
    internal lateinit var job: String
    internal lateinit var education: String
    internal lateinit var hometown: String
    internal var age: Int = 0
    internal var gender: Int = 0
    internal var marraige: Int = 0
    internal var type: Int = 0

    internal lateinit var edit_age: EditText
    internal lateinit var gender_female: Button
    internal lateinit var gender_male: Button
    internal lateinit var marriage_no: Button
    internal lateinit var marriage_yes: Button

    internal lateinit var spinner_job: Spinner
    internal lateinit var spinner_education: Spinner
    internal lateinit var spinner_hometown: Spinner
    internal lateinit var spinner_adapter_job: MySpinnerAdapter
    internal lateinit var spinner_adapter_education: MySpinnerAdapter
    internal lateinit var spinner_adapter_hometown: MySpinnerAdapter
    internal lateinit var selectedJob: String
    internal lateinit var selectedEducation: String
    internal lateinit var selectedHometown: String

    internal lateinit var btn_signup_submit: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_etc)
        val receivedIntent = intent

        type = TYPE_NORMAL
        email = receivedIntent.getStringExtra("email")
        passwd = receivedIntent.getStringExtra("passwd")
        if(receivedIntent.getStringExtra("profile") != null) {
            profile = receivedIntent.getStringExtra("profile") + ".jpg"
        }else{
            profile = "";
        }
        gender = GENDER_FEMALE //성별 default
        marraige = MARRIAGE_NO //결혼유무 default


        edit_age = findViewById(R.id.edit_age) as EditText
        gender_female = findViewById(R.id.gender_female) as Button
        gender_male = findViewById(R.id.gender_male) as Button
        marriage_no = findViewById(R.id.marriage_no) as Button
        marriage_yes = findViewById(R.id.marriage_yes) as Button
        spinner_job = findViewById(R.id.spinner_job) as Spinner
        spinner_education = findViewById(R.id.spinner_education) as Spinner
        spinner_hometown = findViewById(R.id.spinner_hometown) as Spinner

        btn_signup_submit = findViewById(R.id.btn_signup_submit) as RelativeLayout

        gender_female.setOnClickListener(this)
        gender_male.setOnClickListener(this)
        marriage_no.setOnClickListener(this)
        marriage_yes.setOnClickListener(this)
        btn_signup_submit.setOnClickListener(this)

        spinner_adapter_job = MySpinnerAdapter(this, arrayJob, android.R.layout.simple_spinner_dropdown_item)
        spinner_adapter_job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_job.adapter = spinner_adapter_job
        spinner_job.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val current = spinner_adapter_job.getItem(position)
                if (current == "학생") {
                    job = "학생"
                    selectedJob = "학생"
                } else if (current == "전업주부") {
                    job = "전업주부"
                    selectedJob = "전업주부"
                } else if (current == "사무직") {
                    job = "사무직"
                    selectedJob = "사무직"
                } else if (current == "전문직") {
                    job = "전문직"
                    selectedJob = "전문직"
                } else if (current == "관리직") {
                    job = "관리직"
                    selectedJob = "관리직"
                } else if (current == "기능직") {
                    job = "기능직"
                    selectedJob = "기능직"
                }else if (current == "기타") {
                    job = "기타"
                    selectedJob = "기타"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinner_adapter_education = MySpinnerAdapter(this, arrayEducation, android.R.layout.simple_spinner_dropdown_item)
        spinner_adapter_education.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_education.adapter = spinner_adapter_education
        spinner_education.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val current = spinner_adapter_education.getItem(position)
                if (current == "초등학교 졸업") {
                    education = "초등학교 졸업"
                    selectedEducation = "초등학교 졸업"
                } else if (current == "중학교 졸업") {
                    education = "중학교 졸업"
                    selectedEducation = "중학교 졸업"
                } else if (current == "고등학교 졸업") {
                    education = "고등학교 졸업"
                    selectedEducation = "고등학교 졸업"
                } else if (current == "대학교 졸업") {
                    education = "대학교 졸업"
                    selectedEducation = "대학교 졸업"
                } else if (current == "대학원 이상") {
                    education = "대학원 이상"
                    selectedEducation = "대학원 이상"
                } else if (current == "기타") {
                    education = "기타"
                    selectedEducation = "기타"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinner_adapter_hometown = MySpinnerAdapter(this, arrayHometown, android.R.layout.simple_spinner_dropdown_item)
        spinner_adapter_hometown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_hometown.adapter = spinner_adapter_hometown
        spinner_hometown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val current = spinner_adapter_hometown.getItem(position)
                if (current == "서울") {
                    hometown = "서울"
                    selectedHometown = "서울"
                } else if (current == "경기도") {
                    hometown = "경기도"
                    selectedHometown = "경기도"
                } else if (current == "인천") {
                    hometown = "인천"
                    selectedHometown = "인천"
                } else if (current == "세종") {
                    hometown = "세종"
                    selectedHometown = "세종"
                } else if (current == "대전") {
                    hometown = "대전"
                    selectedHometown = "대전"
                } else if (current == "강원도") {
                    hometown = "강원도"
                    selectedHometown = "강원도"
                } else if (current == "인천") {
                    hometown = "인천"
                    selectedHometown = "인천"
                } else if (current == "경상도") {
                    hometown = "경상도"
                    selectedHometown = "경상도"
                } else if (current == "전라도") {
                    hometown = "전라도"
                    selectedHometown = "전라도"
                } else if (current == "광주") {
                    hometown = "광주"
                    selectedHometown = "광주"
                }else if (current == "세종") {
                    hometown = "세종"
                    selectedHometown = "세종"
                }else if (current == "대구") {
                    hometown = "대구"
                    selectedHometown = "대구"
                }else if (current == "울산") {
                    hometown = "울산"
                    selectedHometown = "울산"
                }else if (current == "부산") {
                    hometown = "부산"
                    selectedHometown = "부산"
                }else if (current == "제주") {
                    hometown = "제주"
                    selectedHometown = "제주"
                }else if (current == "기타") {
                    hometown = "기타"
                    selectedHometown = "기타"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.gender_female -> {
                gender_female.setBackgroundResource(R.drawable.singup2_box1)
                gender_female.setTextColor(Color.WHITE)
                gender_male.setBackgroundResource(R.drawable.signup_box)
                gender_male.setTextColor(Color.parseColor("#B9B9B9"))
                gender = Integer.parseInt(gender_female.tag.toString())
            }
            R.id.gender_male -> {
                gender_male.setBackgroundResource(R.drawable.singup2_box1)
                gender_male.setTextColor(Color.WHITE)
                gender_female.setBackgroundResource(R.drawable.signup_box)
                gender_female.setTextColor(Color.parseColor("#B9B9B9"))
                gender = Integer.parseInt(gender_male.tag.toString())
            }
            R.id.marriage_no -> {
                marriage_no.setBackgroundResource(R.drawable.singup2_box1)
                marriage_no.setTextColor(Color.WHITE)
                marriage_yes.setBackgroundResource(R.drawable.signup_box)
                marriage_yes.setTextColor(Color.parseColor("#B9B9B9"))
                marraige = Integer.parseInt(marriage_no.tag.toString())
            }
            R.id.marriage_yes -> {
                marriage_yes.setBackgroundResource(R.drawable.singup2_box1)
                marriage_yes.setTextColor(Color.WHITE)
                marriage_no.setBackgroundResource(R.drawable.signup_box)
                marriage_no.setTextColor(Color.parseColor("#B9B9B9"))
                marraige = Integer.parseInt(marriage_yes.tag.toString())
            }
            R.id.btn_signup_submit -> if (edit_age.length() == 0) {
                Toast.makeText(applicationContext, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_age.requestFocus()
            } else {
                age = Integer.parseInt(edit_age.text.toString())

                var intent = Intent(applicationContext, SignupTermsActivity::class.java)
                intent.putExtra("age", age)
                intent.putExtra("type", type)
                intent.putExtra("email", email)
                intent.putExtra("gender", gender)
                intent.putExtra("marraige", marraige)
                intent.putExtra("job", job)
                intent.putExtra("hometown", hometown)
                intent.putExtra("passwd", passwd)

                startActivity(intent)

            }
        }
    }

    inner class MySpinnerAdapter(internal var context: Context, objects: Array<String>, textViewResourceId: Int) : ArrayAdapter<String>(context, textViewResourceId, objects) {
        internal var items = arrayOf<String>()

        init {
            this.items = objects
        }


        override fun notifyDataSetChanged() {
            super.notifyDataSetChanged()
        }

        /**
         * 스피너 클릭시 보여지는 View의 정의
         */
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView

            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false)
            }

            val tv = convertView!!.findViewById(android.R.id.text1) as TextView
            tv.text = items[position]

            if (items[position] == selectedJob || items[position] == selectedEducation || items[position] == selectedHometown) {
                tv.setTextColor(Color.parseColor("#434343"))
            } else {
                tv.setTextColor(Color.parseColor("#B9B9B9"))
            }

            tv.textSize = 14f
            //            tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
            tv.height = 50
            return convertView
        }

        /**
         * 기본 스피너 View 정의
         */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
            }
            val tv = convertView!!.findViewById(android.R.id.text1) as TextView
            tv.text = items[position]

            tv.setTextColor(Color.parseColor("#434343"))
            //            tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
            tv.textSize = 14f
            return convertView
        }
    }

    //회원가입 완료 통신
    fun registerUser() {
        val networkService = ApplicationController.instance!!.networkService

        age = Integer.parseInt(edit_age.text.toString())

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
            emailAuth = networkService!!.joinUser(CommonData.img, input_type, input_email, input_age, input_gender, input_marraige, input_job, input_hometown, input_passwd)
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
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterUserAddResult>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val TYPE_NORMAL = 0
        private val GENDER_FEMALE = 1 //default
        private val GENDER_MALE = -1
        private val MARRIAGE_YES = 1
        private val MARRIAGE_NO = -1 //default

        internal val arrayJob = arrayOf("학생", "전업주부", "사무직", "전문직", "관리직", "기능직", "기타")
        internal val arrayEducation = arrayOf("초등학교 졸업", "중학교 졸업", "고등학교 졸업", "대학교 졸업", "대학원 이상", "기타")
        internal val arrayHometown = arrayOf("서울", "경기도", "인천", "세종", "대전", "강원도", "인천", "경상도", "전라도", "광주", "세종", "대구", "울산", "부산", "제주", "기타")
    }

}

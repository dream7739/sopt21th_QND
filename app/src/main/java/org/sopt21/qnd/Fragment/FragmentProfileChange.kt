package org.sopt21.qnd.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.ChangeUserInfoData
import org.sopt21.qnd.Data.ChangeUserInfoResult
import org.sopt21.qnd.Data.LoginResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentProfileChange : Fragment(), View.OnClickListener {
    private lateinit var userData: LoginResult

    internal lateinit var btn_back: RelativeLayout
    internal lateinit var mFragment: Fragment

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

    internal lateinit var fm: FragmentManager
    internal lateinit var fragmentEnterSetting: FragmentEnterSetting

    internal lateinit var btn_signup_submit: RelativeLayout

    val bundle = Bundle()

    internal var loginInfo: SharedPreferences? = null
    internal var user_id: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile_change, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        if(loginInfo!!.getString("user_id","") != ""){
            user_id = loginInfo!!.getString("user_id", "");
        }

//        val argument = arguments
//        if (argument != null) { //userData 받아서 user_id 값 넣어주기
//            userData = argument.getSerializable("LoginResult") as LoginResult.LoginResponseData
//        }

        fragmentEnterSetting = FragmentEnterSetting()
        fm = activity.supportFragmentManager

        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout
        btn_signup_submit = view.findViewById(R.id.btn_signup_submit) as RelativeLayout
        edit_age = view.findViewById(R.id.edit_age) as EditText
        gender_female = view.findViewById(R.id.gender_female) as Button
        gender_male = view.findViewById(R.id.gender_male) as Button
        marriage_no = view.findViewById(R.id.marriage_no) as Button
        marriage_yes = view.findViewById(R.id.marriage_yes) as Button
        spinner_job = view.findViewById(R.id.spinner_job) as Spinner
        spinner_education = view.findViewById(R.id.spinner_education) as Spinner
        spinner_hometown = view.findViewById(R.id.spinner_hometown) as Spinner

        btn_back.setOnClickListener(this)
        btn_signup_submit.setOnClickListener(this)
        gender_female.setOnClickListener(this)
        gender_male.setOnClickListener(this)
        marriage_no.setOnClickListener(this)
        marriage_yes.setOnClickListener(this)

        /* 기존정보 불러오는 //
        edit_age.setText(userData.user_age.toString()) // 나이

        if (userData.user_gender == 1) { // 성별
            gender_female.setBackgroundResource(R.drawable.singup2_box1)
            gender_female.setTextColor(Color.WHITE)
            gender_male.setBackgroundResource(R.drawable.signup2_box2)
            gender_male.setTextColor(Color.parseColor("#B9B9B9"))
            gender = Integer.parseInt(gender_female.tag.toString())
        } else if (userData.user_gender == -1) {
            gender_male.setBackgroundResource(R.drawable.singup2_box1)
            gender_male.setTextColor(Color.WHITE)
            gender_female.setBackgroundResource(R.drawable.signup2_box2)
            gender_female.setTextColor(Color.parseColor("#B9B9B9"))
            gender = Integer.parseInt(gender_male.tag.toString())
        }

        if (userData.user_marriage == -1) { // 결혼여부
            marriage_no.setBackgroundResource(R.drawable.singup2_box1)
            marriage_no.setTextColor(Color.WHITE)
            marriage_yes.setBackgroundResource(R.drawable.signup2_box2)
            marriage_yes.setTextColor(Color.parseColor("#B9B9B9"))
            marraige = Integer.parseInt(marriage_no.tag.toString())
        } else if (userData.user_marriage == 1) {
            marriage_yes.setBackgroundResource(R.drawable.singup2_box1)
            marriage_yes.setTextColor(Color.WHITE)
            marriage_no.setBackgroundResource(R.drawable.signup2_box2)
            marriage_no.setTextColor(Color.parseColor("#B9B9B9"))
            marraige = Integer.parseInt(marriage_yes.tag.toString())
        }*/

        // 스피너 세개 해야됨..
        // 회원정보 수정 스피너 제외하고 세개 수정했는데 logindata를 가져오기 때문에 로그인을 다시 해야만 바뀐 결과값 반영..
        spinner_adapter_job = MySpinnerAdapter(context, arrayJob, android.R.layout.simple_spinner_dropdown_item)
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

        spinner_adapter_education = MySpinnerAdapter(context, arrayEducation, android.R.layout.simple_spinner_dropdown_item)
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

        spinner_adapter_hometown = MySpinnerAdapter(context, arrayHometown, android.R.layout.simple_spinner_dropdown_item)
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

        return view
    }

    override fun onClick(v: View) {
        val fm = activity.supportFragmentManager

        when (v.id) {
            R.id.btn_back -> {
                mFragment = fragmentManager.findFragmentById(R.id.fragment)
                fm.beginTransaction().remove(this).commit()
                fm.popBackStack()
            }
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
            R.id.btn_signup_submit -> {
                if (edit_age.text.toString().length == 0) {
                    Toast.makeText(context, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    changeUserInfoNetwork()
                }
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

    fun changeUserInfoNetwork() {
        val networkService = ApplicationController.instance!!.networkService
        val changeUserInfoData = ChangeUserInfoData()
        //TODO SharedPareferences 받아온 user_id로 대치했음
        changeUserInfoData.user_updated_age = Integer.parseInt(edit_age.text.toString())
        changeUserInfoData.user_updated_gender = gender
        changeUserInfoData.user_updated_marriage = marraige
        changeUserInfoData.user_update_job = job
        changeUserInfoData.user_updated_education = education
        changeUserInfoData.user_updated_city = hometown

        val changeUserInfoCallback = networkService!!.changeUserInfo(user_id, changeUserInfoData)
        changeUserInfoCallback.enqueue(object : Callback<ChangeUserInfoResult> {

            override fun onResponse(call: Call<ChangeUserInfoResult>?, response: Response<ChangeUserInfoResult>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

                    mFragment = Fragment.instantiate(context, fragmentEnterSetting.javaClass.name)
                    fm.beginTransaction().add(R.id.make_container, mFragment).commit()

                } else {
                    Toast.makeText(context, "통신실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChangeUserInfoResult>?, t: Throwable?) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

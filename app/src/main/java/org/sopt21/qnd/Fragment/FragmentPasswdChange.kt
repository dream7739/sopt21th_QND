package org.sopt21.qnd.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.ChangeUserPwdData
import org.sopt21.qnd.Data.ChangeUserPwdResult
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPasswdChange : Fragment(), View.OnClickListener {

    internal lateinit var btn_back: RelativeLayout
    internal lateinit var btn_signup_submit: RelativeLayout
    internal lateinit var mFragment: Fragment
    internal lateinit var edit_currentpasswd: EditText
    internal lateinit var edit_newpasswd: EditText
    internal lateinit var edit_cnewpasswd: EditText

    internal lateinit var fm: FragmentManager
    internal lateinit var fragmentEnterSetting: FragmentEnterSetting

    val bundle = Bundle()

    internal var loginInfo: SharedPreferences? = null
    internal var user_id: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_passwd_chage, container, false)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo!!.getString("user_id", "")

        fragmentEnterSetting = FragmentEnterSetting()
        fm = activity.supportFragmentManager

        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout
        btn_signup_submit = view.findViewById(R.id.btn_signup_submit) as RelativeLayout

        edit_currentpasswd = view.findViewById(R.id.edit_currentpasswd) as EditText
        edit_newpasswd = view.findViewById(R.id.edit_newpasswd) as EditText
        edit_cnewpasswd = view.findViewById(R.id.edit_cnewpasswd) as EditText

        btn_back.setOnClickListener(this)
        btn_signup_submit.setOnClickListener(this)

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
            R.id.btn_signup_submit -> {
                if (edit_currentpasswd.text.toString().length == 0) {
                    Toast.makeText(context, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    edit_currentpasswd.requestFocus()
                } else {
                    if (edit_newpasswd.text.toString().length == 0 || edit_cnewpasswd.text.toString().length == 0) {
                        Toast.makeText(context, "새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        edit_newpasswd.requestFocus()
                    } else {
                        if (edit_newpasswd.text.toString().equals(edit_cnewpasswd.text.toString())) {
                            changeUserPwdNetwork()
                        } else {
                            Toast.makeText(context, "비밀번호가 일치한지 확인해주세요.", Toast.LENGTH_SHORT).show()
                            edit_newpasswd.requestFocus()
                        }
                    }
                }
            }
        }
    }

    fun changeUserPwdNetwork() {
        val networkService = ApplicationController.instance!!.networkService
        val changeUserPwdData = ChangeUserPwdData()

        changeUserPwdData.input_current_pwd = edit_currentpasswd.text.toString()
        changeUserPwdData.input_new_pwd = edit_newpasswd.text.toString()

        val changeUserPwdCallback = networkService!!.changeUserPwd(user_id, changeUserPwdData)
        changeUserPwdCallback.enqueue(object : Callback<ChangeUserPwdResult> {
            override fun onResponse(call: Call<ChangeUserPwdResult>?, response: Response<ChangeUserPwdResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") {
                        Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()

                        mFragment = Fragment.instantiate(context, fragmentEnterSetting.javaClass.name)
                        fm.beginTransaction().add(R.id.make_container, mFragment).commit()
                    } else {
                        Toast.makeText(context, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        edit_currentpasswd.requestFocus()
                    }
                } else {
                    Toast.makeText(context, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    edit_currentpasswd.requestFocus()
                }
            }

            override fun onFailure(call: Call<ChangeUserPwdResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

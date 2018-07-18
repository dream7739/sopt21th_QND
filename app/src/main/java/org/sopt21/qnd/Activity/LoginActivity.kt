package org.sopt21.qnd.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.LoginData
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var tv_signup: TextView
    internal lateinit var facebook_login: RelativeLayout
    internal lateinit var edit_login_id: EditText
    internal lateinit var edit_login_passwd: EditText
    internal lateinit var btn_login: Button
    var user_id: String? = null
    lateinit var loginInfo: SharedPreferences

    //facebook Login
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginInfo = this.getSharedPreferences("loginSetting", 0)

        tv_signup = findViewById(R.id.tv_signup) as TextView
        facebook_login = findViewById(R.id.facebook_login) as RelativeLayout

        edit_login_id = findViewById(R.id.edit_login_id) as EditText
        edit_login_passwd = findViewById(R.id.edit_login_passwd) as EditText

        btn_login = findViewById(R.id.btn_login) as Button

        tv_signup.setOnClickListener(this)
        facebook_login.setOnClickListener(this)
        btn_login.setOnClickListener(this)

        //facebook로그인 연동을 위한 해시키 생성
        try {
            val info = packageManager.getPackageInfo("org.sopt21.qnd", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                //fLem7UZGG8/sBJvhsUTNS9T6M/M=
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_signup -> {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivity(intent)
            }

            R.id.facebook_login -> {
                FacebookSdk.sdkInitialize(applicationContext)
                callbackManager = CallbackManager.Factory.create()

                LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList("public_profile", "email"))
                LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val request: GraphRequest
                        request = GraphRequest.newMeRequest(result.accessToken) { user, response ->
                            if (response.error != null) {
                            } else {
                                Log.i("TAG", "user: " + user.toString())
                                Log.i("TAG", "AccessToken: " + result.accessToken.token)
                                setResult(Activity.RESULT_OK)

                                val i = Intent(applicationContext, MainActivity::class.java)
                                try {
                                    i.putExtra("user_id", user.getString("id"))
                                    i.putExtra("user_name", user.getString("name"))
                                    i.putExtra("user_email", user.getString("email"))
                                    i.putExtra("user_gender", user.getString("gender"))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                startActivity(i)
                                finish()
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,gender,birthday")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onError(error: FacebookException) {
                        Log.e("test", "Error: " + error)
                        //finish();
                    }

                    override fun onCancel() {
                        //finish();
                    }
                })
            }

            R.id.btn_login -> {
                if (edit_login_id.text.toString().length < 0) {
                    Toast.makeText(applicationContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    edit_login_id.requestFocus()
                } else if (edit_login_passwd.text.toString().length < 0) {
                    Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                   // loginNetwork()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }


    //서버 안되서 사용X
    fun loginNetwork() {
                val networkService = ApplicationController.instance!!.networkService

                val loginData = LoginData()
                loginData.user_email = edit_login_id.text.toString()
                loginData.user_input_pwd = edit_login_passwd.text.toString()
                loginData.user_type = 0

                val loginCallback = networkService!!.login(loginData)
                loginCallback.enqueue(object : Callback<org.sopt21.qnd.Data.LoginResult> {
                    override fun onResponse(call: Call<org.sopt21.qnd.Data.LoginResult>, response: Response<org.sopt21.qnd.Data.LoginResult>) =
                    if (response.isSuccessful) {
                        if (response.body().status == "Success") { //로그인 성공시 리얼메인액티비티로
                            var loginResponse : org.sopt21.qnd.Data.LoginResult = response.body()!!
                            var editor: SharedPreferences.Editor = loginInfo.edit()
                            editor.putString("user_id", loginResponse.data)
                            editor.commit()

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }

            override fun onFailure(call: Call<org.sopt21.qnd.Data.LoginResult>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                Log.v("taehyung", t.message)
                Log.v("taehyung", t.cause.toString())


            }
        })
    }
}

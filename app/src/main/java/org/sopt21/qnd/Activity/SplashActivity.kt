package org.sopt21.qnd.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import org.sopt21.qnd.R

class SplashActivity : AppCompatActivity() {

    lateinit var loginInfo: SharedPreferences
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loginInfo = this.getSharedPreferences("loginSetting",0)
        token = loginInfo.getString("user_id","NoLogin")

        val delayHandler = DelayHandler()
        delayHandler.sendEmptyMessageDelayed(Activity.RESULT_OK, 1500L)
    }


    internal inner class DelayHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var intent: Intent
            if("NoLogin".equals(token)) {
                intent = Intent(applicationContext, LoginActivity::class.java)
            }else{
                intent = Intent(applicationContext, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
package org.sopt21.qnd.Application

import android.app.Application
import android.content.Context
import android.widget.Toast
import org.sopt21.qnd.Network.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by dream on 2018-01-05.
 */

class ApplicationController : Application() {

    //자유롭게 사용가능합니다.

    var networkService: NetworkService? = null
        private set                        // 네트워크 서비스 객체 선언
    // 네트워크서비스 객체 반환

    override fun onCreate() {
        super.onCreate()

        context = this

        ApplicationController.instance = this //인스턴스 객체 초기화
        buildService()

    }

    fun buildService() {
        val builder = Retrofit.Builder()
        val retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        networkService = retrofit.create<NetworkService>(NetworkService::class.java!!)
    }

    fun makeToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        var instance: ApplicationController? = null
            private set    // 먼저 어플리케이션 인스턴스 객체를 하나 선언
        // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서

        private val baseUrl = "https://qndata.ml/"  // 베이스 url 초기화

        var context: Context? = null
            private set
    }

}


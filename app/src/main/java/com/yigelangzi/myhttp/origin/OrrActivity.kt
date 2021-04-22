package com.yigelangzi.myhttp.origin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yigelangzi.myhttp.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class OrrActivity : AppCompatActivity() {
    private lateinit var mTvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mTvResponse = findViewById(R.id.tv_response)
        mTvResponse.movementMethod = ScrollingMovementMethod.getInstance()

        // OkHttp 控制基础网络通信，如设置连接超时时间等
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        // Retrofit 控制参数的传入及响应的处理
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.yigelangzi.com/api/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)

        val btnGet = findViewById<Button>(R.id.btn_get)
        btnGet.setOnClickListener {
            // RxJava 处理异步操作
            userService.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mTvResponse.text = parseResponse(it)
                }
        }

        val btnPost = findViewById<Button>(R.id.btn_post)
        btnPost.setOnClickListener {
            userService.postLogin("langzi1206@msn.cn", "2545b2805c6db5d5ddf6f87341ce0ef5d97fbb23")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mTvResponse.text = parseResponse(it)
                }
        }
    }

    private fun parseResponse(response: Response<String>): String {
        if (!response.isSuccessful) return "Unexpected code $response"

        val text = StringBuilder()

        text.append("${response.raw().protocol} ${response.code()} ${response.message()}\n")
        for ((key, value) in response.headers()) {
            text.append("$key: $value\n")
        }

        val jo = JSONObject(response.body()!!)
        text.append("\n")
        text.append(jo.toString(4))

        return text.toString()
    }

    private interface UserService {
        @GET("users")
        fun getUsers(): Observable<Response<String>>

        @FormUrlEncoded
        @POST("authenticate")
        fun postLogin(@Field("email") email: String, @Field("passwd") passwd: String): Observable<Response<String>>
    }
}
package com.yigelangzi.myhttp.origin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yigelangzi.myhttp.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.lang.StringBuilder

class RetrofitActivity : AppCompatActivity() {
    private lateinit var mTvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mTvResponse = findViewById(R.id.tv_response)
        mTvResponse.movementMethod = ScrollingMovementMethod.getInstance()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.yigelangzi.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)

        val btnGet = findViewById<Button>(R.id.btn_get)
        btnGet.setOnClickListener {
            userService.getUsers().enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    mTvResponse.text = "Something wrong: ${t.localizedMessage}"
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    mTvResponse.text = parseResponse(response)
                }
            })
        }

        val btnPost = findViewById<Button>(R.id.btn_post)
        btnPost.setOnClickListener {
            userService.postLogin("langzi1206@msn.cn", "2545b2805c6db5d5ddf6f87341ce0ef5d97fbb23").enqueue(object :
                Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    mTvResponse.text = "Something wrong: ${t.localizedMessage}"
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    mTvResponse.text = parseResponse(response)
                }
            })
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

    interface UserService {
        @GET("users")
        fun getUsers(): Call<String>

        @FormUrlEncoded
        @POST("authenticate")
        fun postLogin(@Field("email") email: String, @Field("passwd") passwd: String): Call<String>
    }
}
package com.yigelangzi.myhttp.origin

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yigelangzi.myhttp.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class OkHttpActivity : AppCompatActivity() {
    private lateinit var mTvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mTvResponse = findViewById(R.id.tv_response)
        mTvResponse.movementMethod = ScrollingMovementMethod.getInstance()

        val client = OkHttpClient()

        val btnGet = findViewById<Button>(R.id.btn_get)
        btnGet.setOnClickListener {
            val request = Request.Builder()
                .url("https://www.yigelangzi.com/api/users")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mTvResponse.text = "Something wrong: ${e.localizedMessage}"
                }

                override fun onResponse(call: Call, response: Response) {
                    mTvResponse.text = parseResponse(response)
                }
            })
        }

        val btnPost = findViewById<Button>(R.id.btn_post)
        btnPost.setOnClickListener {
            val formBody = FormBody.Builder()
                .add("email", "langzi1206@msn.cn")
                .add("passwd", "2545b2805c6db5d5ddf6f87341ce0ef5d97fbb23")
                .build()
            val request = Request.Builder()
                .url("https://www.yigelangzi.com/api/authenticate")
                .post(formBody)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mTvResponse.text = "Something wrong: ${e.localizedMessage}"
                }

                override fun onResponse(call: Call, response: Response) {
                    mTvResponse.text = parseResponse(response)
                }
            })
        }
    }

    private fun parseResponse(response: Response): String {
        if (!response.isSuccessful) return "Unexpected code $response"

        val text = StringBuilder()

        text.append("${response.protocol} ${response.code} ${response.message}\n")
        for ((key, value) in response.headers) {
            text.append("$key: $value\n")
        }

        val jo = JSONObject(response.body!!.string())
        text.append("\n")
        text.append(jo.toString(4))

        return text.toString()
    }
}
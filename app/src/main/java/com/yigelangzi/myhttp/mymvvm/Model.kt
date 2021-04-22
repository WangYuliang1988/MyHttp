package com.yigelangzi.myhttp.mymvvm

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class Model {
    private val mClient = OkHttpClient()

    fun getUsers(): String {
        val request = Request.Builder()
            .url("https://www.yigelangzi.com/api/users")
            .build()
        val response = mClient.newCall(request).execute()
        return parseResponse(response)
    }

    fun postLogin(): String {
        val formBody = FormBody.Builder()
            .add("email", "langzi1206@msn.cn")
            .add("passwd", "2545b2805c6db5d5ddf6f87341ce0ef5d97fbb23")
            .build()
        val request = Request.Builder()
            .url("https://www.yigelangzi.com/api/authenticate")
            .post(formBody)
            .build()
        val response = mClient.newCall(request).execute()
        return parseResponse(response)
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
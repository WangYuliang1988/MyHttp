package com.yigelangzi.myhttp.mymvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import com.yigelangzi.myhttp.R

class ViewActivity : AppCompatActivity(), HttpObserver {
    private lateinit var mTvResponse: TextView

    private val mViewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mTvResponse = findViewById(R.id.tv_response)
        mTvResponse.movementMethod = ScrollingMovementMethod.getInstance()

        mViewModel.addObserver(this)

        val btnGet = findViewById<Button>(R.id.btn_get)
        btnGet.setOnClickListener { mViewModel.getUsers() }

        val btnPost = findViewById<Button>(R.id.btn_post)
        btnPost.setOnClickListener { mViewModel.postLogin() }
    }

    override fun onResponse(text: String) {
        mTvResponse.text = text
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.removeObserver(this)
    }
}
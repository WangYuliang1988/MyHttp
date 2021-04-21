package com.yigelangzi.myhttp.jetpackmvvm

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yigelangzi.myhttp.R

class JetViewActivity : AppCompatActivity() {
    private lateinit var mTvResponse: TextView

    // Without ViewModel Library
//    private val mJetViewModel = JetViewModel()

    // With ViewModel Library
    private lateinit var mJetViewModel: JetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mTvResponse = findViewById(R.id.tv_response)
        mTvResponse.movementMethod = ScrollingMovementMethod.getInstance()

        val btnGet = findViewById<Button>(R.id.btn_get)
        btnGet.setOnClickListener { mJetViewModel.getUsers() }

        val btnPost = findViewById<Button>(R.id.btn_post)
        btnPost.setOnClickListener { mJetViewModel.postLogin() }

        mJetViewModel = ViewModelProvider(this).get(JetViewModel::class.java) // With ViewModel Library
        val responseObserver = Observer<String> {
            mTvResponse.text = it
        }
        mJetViewModel.mResponseText.observe(this, responseObserver)
    }
}
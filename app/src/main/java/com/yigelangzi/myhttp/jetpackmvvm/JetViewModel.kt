package com.yigelangzi.myhttp.jetpackmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors

// Without ViewModel Library
//class JetViewModel {
//    private val mJetModel = JetModel()
//    private val mExecutors = Executors.newFixedThreadPool(5)
//
//    val mResponseText: MutableLiveData<String> by lazy {
//        MutableLiveData()
//    }
//
//    fun getUsers() {
//        mExecutors.execute {
//            val text = mJetModel.getUsers()
//            mResponseText.postValue(text)
//        }
//    }
//
//    fun postLogin() {
//        mExecutors.execute {
//            val text = mJetModel.postLogin()
//            mResponseText.postValue(text)
//        }
//    }
//}

// With ViewModel Library
class JetViewModel : ViewModel() {
    private val mJetModel = JetModel()
    private val mExecutors = Executors.newFixedThreadPool(5)

    val mResponseText: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun getUsers() {
        mExecutors.execute {
            val text = mJetModel.getUsers()
            mResponseText.postValue(text)
        }
    }

    fun postLogin() {
        mExecutors.execute {
            val text = mJetModel.postLogin()
            mResponseText.postValue(text)
        }
    }
}
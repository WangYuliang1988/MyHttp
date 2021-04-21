package com.yigelangzi.myhttp.mymvvm

import java.util.concurrent.Executors

class ViewModel : HttpObservable {
    private val mModel = Model()
    private val mObservers = ArrayList<HttpObserver>()
    private val mExecutor = Executors.newFixedThreadPool(5)

    fun getUsers() {
        mExecutor.execute {
            val result = mModel.getUsers()
            for (o in mObservers) {
                o.onResponse(result)
            }
        }
    }

    fun postLogin() {
        mExecutor.execute {
            val result = mModel.postLogin()
            for (o in mObservers) {
                o.onResponse(result)
            }
        }
    }

    override fun addObserver(observer: HttpObserver) {
        mObservers.add(observer)
    }

    override fun removeObserver(observer: HttpObserver) {
        mObservers.remove(observer)
    }
}
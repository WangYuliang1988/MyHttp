package com.yigelangzi.myhttp.mymvvm

interface HttpObservable {
    fun addObserver(observer: HttpObserver)
    fun removeObserver(observer: HttpObserver)
}
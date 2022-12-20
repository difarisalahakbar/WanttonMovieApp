package com.difa.myapplication.core.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.difa.myapplication.core.data.Resource

fun <T> LiveData<Resource<T>>.observes(
    lifecycleOwner: LifecycleOwner,
    onLoading: () -> Unit,
    onSuccess: (items: T) -> Unit,
    onError: (errorMessage: String) -> Unit
) {
    observe(lifecycleOwner) {
        when (it) {
            is Resource.Loading<T> -> onLoading.invoke()
            is Resource.Success<T> -> it.data?.let { data -> onSuccess.invoke(data) }
            else -> onError.invoke(it.message.orEmpty())
        }
    }
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun AppCompatActivity.setupToolbar(toolbar: Toolbar, title: String, showBack: Boolean) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(showBack)
    }
}
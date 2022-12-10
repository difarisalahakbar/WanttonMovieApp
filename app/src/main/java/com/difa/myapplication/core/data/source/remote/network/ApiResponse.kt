package com.difa.myapplication.core.data.source.remote.network

sealed class ApiResponse<out T> {
    class Success<out R>(val data: R) : ApiResponse<R>()
    class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Empty : ApiResponse<Nothing>()
}
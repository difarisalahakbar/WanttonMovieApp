package com.difa.myapplication.core.data

import com.difa.myapplication.core.data.source.remote.network.ApiResponse
import com.difa.myapplication.core.utils.MOVIE
import kotlinx.coroutines.flow.*

abstract class RemoteResource<ResultType, RequestTypeMovie, RequestTypeTv>(showType: Int) {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        if(showType == MOVIE){
            when(val apiResponse = createCall().first()){
                is ApiResponse.Success -> {
                    val showModel = mappingCallResult(apiResponse.data)
                    emitAll(showModel.map { Resource.Success(it) })
                }
                is ApiResponse.Empty ->{
                    emitAll(emptyResult().map { Resource.Success(it) })
                }
                is ApiResponse.Error ->{
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }else{
            when(val apiResponse = createCallSecond().first()){
                is ApiResponse.Success -> {
                    val showModel = mappingCallResultSecond(apiResponse.data)
                    emitAll(showModel.map { Resource.Success(it) })
                }
                is ApiResponse.Empty ->{
                    emitAll(emptyResult().map { Resource.Success(it) })
                }
                is ApiResponse.Error ->{
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }

    }

    protected abstract fun createCall(): Flow<ApiResponse<RequestTypeMovie>>

    protected abstract fun mappingCallResult(data: RequestTypeMovie): Flow<ResultType>

    protected abstract fun createCallSecond(): Flow<ApiResponse<RequestTypeTv>>

    protected abstract fun mappingCallResultSecond(data: RequestTypeTv): Flow<ResultType>

    protected abstract fun emptyResult(): Flow<ResultType>

    fun asFlow(): Flow<Resource<ResultType>> = result

}
package com.difa.myapplication.core.data

import com.difa.myapplication.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundDoubleResource<ResultType, RequestType, RequestTypeSecond> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val db = loadFromDB().first()
        if(shouldFetch(db)){
            emit(Resource.Loading())
            when(val apiResponse = createCall().first()){
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                 }
                is ApiResponse.Empty ->{
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ApiResponse.Error ->{
                    onFetchFailed()
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
            when(val apiResponse = createCallSecond().first()){
                is ApiResponse.Success -> {
                    saveCallResultSecond(apiResponse.data)
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ApiResponse.Empty ->{
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ApiResponse.Error ->{
                    onFetchFailed()
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }else{
            emitAll(loadFromDB().map { Resource.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    protected abstract suspend fun createCallSecond(): Flow<ApiResponse<RequestTypeSecond>>

    protected abstract suspend fun saveCallResultSecond(data: RequestTypeSecond)

    fun asFlow(): Flow<Resource<ResultType>> = result

}



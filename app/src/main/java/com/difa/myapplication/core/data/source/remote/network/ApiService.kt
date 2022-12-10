package com.difa.myapplication.core.data.source.remote.network

import com.difa.myapplication.BuildConfig
import com.difa.myapplication.core.data.source.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getMovieNowPlaying(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getMoviePopular(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getMovieTopRated(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("tv/on_the_air")
    suspend fun getTvNowPlaying(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): TvResponse

    @GET("tv/popular")
    suspend fun getTvPopular(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): TvResponse

    @GET("tv/top_rated")
    suspend fun getTvTopRated(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): TvResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): MovieItem

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): TvItem

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): CastResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCast(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): CastResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") keyword: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY,
    ): MovieResponse

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") keyword: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY,
    ): TvResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getAllSimilarMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): MovieResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getAllSimilarTv(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.API_KEY
    ): TvResponse

}
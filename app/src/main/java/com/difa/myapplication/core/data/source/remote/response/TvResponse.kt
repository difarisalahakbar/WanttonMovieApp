package com.difa.myapplication.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TvResponse(

    @field:SerializedName("results")
    val results: List<TvItem>

    )

data class TvItem(

    @field:SerializedName("id")
    val id_tv: String,

    @field:SerializedName("name")
    val title: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("first_air_date")
    val releaseDate: String?,

    @field:SerializedName("vote_average")
    val voteAverage: Any,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = "",

    @field:SerializedName("genres")
    val genres: List<GenresItem?>? = null,

    @field:SerializedName("adult")
    val adult: Boolean? = false,

    )



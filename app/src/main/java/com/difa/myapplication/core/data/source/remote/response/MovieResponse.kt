package com.difa.myapplication.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

	@field:SerializedName("results")
	val results: List<MovieItem>,

)

data class MovieItem(

	@field:SerializedName("id")
	val id_movie: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("poster_path")
	val posterPath: String?,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("vote_average")
	val voteAverage: Any,

	//data update
	@field:SerializedName("backdrop_path")
	val backdropPath: String?,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>?,

	@field:SerializedName("runtime")
	val runtime: Int?,

	@field:SerializedName("adult")
	val adult: Boolean?

)

data class GenresItem(

	@field:SerializedName("name")
	val name: String?

)
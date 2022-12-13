package com.difa.myapplication.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ShowModel(

    var id: String,

    var title: String,

    var overview: String,

    var posterPath: String?,

    var releaseDate: String?,

    var voteAverage: String,

    //additional data
    var showType: Int,

    var category: Int,

    var isFavorite: Boolean,

    //data update
    var backdropPath: String?,

    var genres1: String?,

    var genres2: String?,

    var genres3: String?,

    var runtime: Int?,

    var adult: Boolean?

    ) : Parcelable
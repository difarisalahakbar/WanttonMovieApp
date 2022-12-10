package com.difa.myapplication.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CastModel(

    var id: String,

    var castId: String,

    var name: String,

    var character: String,

    var profilePath: String? = "",


) : Parcelable
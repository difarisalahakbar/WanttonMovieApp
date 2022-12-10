package com.difa.myapplication.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.difa.myapplication.core.data.source.remote.response.GenresItem
import org.jetbrains.annotations.NotNull

@Entity(tableName = "table_show")
data class ShowEntity(
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "posterPath")
    var posterPath: String?,

    @ColumnInfo(name = "releaseDate")
    var releaseDate: String?,

    @ColumnInfo(name = "voteAverage")
    var voteAverage: String?,

    //additional data
    @ColumnInfo(name = "showType")
    var showType: Int,

    @ColumnInfo(name = "category")
    var category: Int,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean,

    //data update
    @ColumnInfo(name = "backdropPath")
    val backdropPath: String?,

    @ColumnInfo(name = "genres1")
    val genres1: String?,

    @ColumnInfo(name = "genres2")
    val genres2: String?,

    @ColumnInfo(name = "genres3")
    val genres3: String?,

    @ColumnInfo(name = "runtime")
    val runtime: Int?,

    @ColumnInfo(name = "adult")
    val adult: Boolean?
)
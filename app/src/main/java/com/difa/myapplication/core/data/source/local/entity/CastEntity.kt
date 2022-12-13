package com.difa.myapplication.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_cast")
data class CastEntity (
     @PrimaryKey
     @ColumnInfo(name = "castId")
     var castId: String,

     @ColumnInfo(name = "id")
     var id: String,

     @ColumnInfo(name = "name")
     var name: String,

     @ColumnInfo(name = "character")
     var character: String?,

     @ColumnInfo(name = "profilePath")
     var profilePath: String?,

     @ColumnInfo(name = "knownAs")
     var knownAs: String,

     @ColumnInfo(name = "biography")
     var biography: String?,

     @ColumnInfo(name = "birthDay")
     var birthDay: String?,

     @ColumnInfo(name = "deathDay")
     var deathDay: String?
)
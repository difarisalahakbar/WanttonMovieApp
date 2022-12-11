package com.difa.myapplication.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

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
     var character: String,

     @ColumnInfo(name = "profilePath")
     var profilePath: String?,
)
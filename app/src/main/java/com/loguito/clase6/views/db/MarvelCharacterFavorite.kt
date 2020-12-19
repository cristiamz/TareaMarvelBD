package com.loguito.clase6.views.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarvelCharacterFavorite (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean
)

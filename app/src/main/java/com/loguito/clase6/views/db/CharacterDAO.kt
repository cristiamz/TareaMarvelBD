package com.loguito.clase6.views.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.loguito.clase6.views.network.models.MarvelCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDAO {

    @Insert
    suspend fun insert(marvelCharacterFavorite: MarvelCharacterFavorite)

    @Query("SELECT * FROM marvelCharacterFavorite")
    fun getAllCharacters() : Flow<List<MarvelCharacterFavorite>>

}
package com.loguito.clase6.views.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marvelCharacter: MarvelCharacter)

    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(marvelCharacterList: List<MarvelCharacter>);

    @Query("SELECT * FROM marvelCharacter")
    fun getAllCharacters() : Flow<List<MarvelCharacter>>

    @Query("SELECT * from marvelCharacter WHERE id= :id")
    fun getMarvelCharacterById(id: Int): Flow<List<MarvelCharacter?>?>

}
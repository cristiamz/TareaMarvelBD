package com.loguito.clase6.views.repository

import com.loguito.clase6.views.db.CharacterDAO
import com.loguito.clase6.views.db.MarvelCharacterFavorite
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val characterDAO: CharacterDAO) {

    suspend fun insert(marvelCharacterFavorite: MarvelCharacterFavorite) {
        characterDAO.insert(marvelCharacterFavorite)
    }

    val allCharacters: Flow<List<MarvelCharacterFavorite>> = characterDAO.getAllCharacters()

}
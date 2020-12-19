package com.loguito.clase6.views.repository

import com.loguito.clase6.views.db.CharacterDAO
import com.loguito.clase6.views.db.MarvelCharacter
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val characterDAO: CharacterDAO) {

    suspend fun insert(marvelCharacter: MarvelCharacter) {
        characterDAO.insert(marvelCharacter)
    }

    suspend fun insertAll(marvelCharacterList: List<MarvelCharacter>) {
        characterDAO.insertAll(marvelCharacterList)
    }

    suspend fun getMarvelCharacterById(marvelCharacterId: Int): Flow<List<MarvelCharacter?>?> {
        return characterDAO.getMarvelCharacterById(marvelCharacterId)
    }

    val allCharacters: Flow<List<MarvelCharacter>> = characterDAO.getAllCharacters()

}
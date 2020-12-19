package com.loguito.clase6.views.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.loguito.clase6.views.db.CharacterDatabase
import com.loguito.clase6.views.db.MarvelCharacter
import com.loguito.clase6.views.network.RetrofitProvider
import com.loguito.clase6.views.network.models.CharacterResponse
import com.loguito.clase6.views.repository.CharacterRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarvelListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository
    private val database: CharacterDatabase

    init {
        database = CharacterDatabase.getDatabase(application)
        repository = CharacterRepository(database.characterDao())
    }

    private val retrofitProvider = RetrofitProvider()
    private val apiKey: String = "001ac6c73378bbfff488a36141458af2"
    private val hash: String = "72e5ed53d1398abb831c3ceec263f18b"
    private val timestamp: String = "thesoer"

    private val marvelCharacterListResponse: MutableLiveData<List<MarvelCharacter>> =
        MutableLiveData()
    private val isMakingRequest: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()

    //fun getCharacterListResponse(): LiveData<List<MarvelCharacter>> = repository.allCharacters.asLiveData()
    fun getCharacterListResponse(): LiveData<List<MarvelCharacter>> {
        return repository.allCharacters.asLiveData()
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isMakingRequest
    }

    fun getIsError(): LiveData<Boolean> {
        return isError
    }

    fun getCharacterList() {
        isMakingRequest.postValue(true)
        retrofitProvider.getMarvelService().getCharacterList(apiKey, hash, timestamp)
            .enqueue(object :
                Callback<CharacterResponse> {
                override fun onResponse(
                    call: Call<CharacterResponse>,
                    response: Response<CharacterResponse>
                ) {
                    isMakingRequest.postValue(false)
                    if (response.isSuccessful) {
                        response.body()?.let { unwrappedResponse ->

                            val newCharacters: MutableList<MarvelCharacter> =
                                emptyList<MarvelCharacter>().toMutableList()

                            unwrappedResponse.data.results.forEach { character ->

                                viewModelScope.launch {
                                    val charactersOnDB = repository.getMarvelCharacterById(character.id).asLiveData()
                                    if (charactersOnDB.value?.isEmpty() == true){
                                        newCharacters.add(
                                            MarvelCharacter(
                                                character.id,
                                                character.name,
                                                character.description,
                                                "${character.thumbnail.path}.${character.thumbnail.extension}".replace(
                                                    "http",
                                                    "https"
                                                ),
                                                false
                                            )
                                        )
                                    }
                                }
                            }

                            viewModelScope.launch {
                                repository.insertAll(newCharacters)
                            }
//                            marvelCharacterListResponse.postValue(unwrappedResponse.data.results)
                        }
                    } else {
                        isError.postValue(true)
                    }
                }

                override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                    isMakingRequest.postValue(false)
                    isError.postValue(true)
                }
            })
    }

    fun saveFavorite(marvelCharacter: MarvelCharacter) {
        viewModelScope.launch {
            val newCharacter = MarvelCharacter(
                marvelCharacter.id,
                marvelCharacter.name,
                marvelCharacter.description,
                marvelCharacter.thumbnail,
                !marvelCharacter.isFavorite
            )
            repository.insert(newCharacter)
        }
    }

}
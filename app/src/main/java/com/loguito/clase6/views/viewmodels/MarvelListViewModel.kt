package com.loguito.clase6.views.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.loguito.clase6.views.db.CharacterDatabase
import com.loguito.clase6.views.db.MarvelCharacterFavorite
import com.loguito.clase6.views.network.RetrofitProvider
import com.loguito.clase6.views.network.models.MarvelCharacter
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

    private val marvelCharacterListResponse: MutableLiveData<List<MarvelCharacter>> = MutableLiveData()
    private val isMakingRequest: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()

    fun getCharacterListResponse(): LiveData<List<MarvelCharacter>> {
        return marvelCharacterListResponse
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
                            marvelCharacterListResponse.postValue(unwrappedResponse.data.results)
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

    fun insertCharacter (marvelCharacter: MarvelCharacter, isFavorite: Boolean){
        viewModelScope.launch {
            val newCharacter = MarvelCharacterFavorite(marvelCharacter.id, isFavorite)
            repository.insert(newCharacter)
        }
    }

}
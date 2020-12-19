package com.loguito.clase6.views.network.models


data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: CharacterThumbnail,
    val isFavorite: Boolean
)
package com.loguito.clase6.views.network.models

data class CharacterDataResponse(val offset: Int, val limit: Int, val total: Int, val count: Int, val results: List<Character>)
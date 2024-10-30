package com.example.premiereappli_camillekerherve

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainViewModel : ViewModel() {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build();

    val api = retrofit.create(Api::class.java)

    val listmovies = MutableStateFlow<List<ModelMovies>>(listOf())
    val listactors = MutableStateFlow<List<ModelActeurs>>(listOf())
    val listseries = MutableStateFlow<List<ModelSeries>>(listOf())

    private val _selectedFilm = MutableStateFlow<ModelMovies?>(null)
    val selectedFilm: StateFlow<ModelMovies?> = _selectedFilm

    private val _selectedSerie = MutableStateFlow<ModelSeries?>(null)
    val selectedSerie: StateFlow<ModelSeries?> = _selectedSerie

    private val _selectedActeur = MutableStateFlow<ModelActeurs?>(null)
    val selectedActeur: StateFlow<ModelActeurs?> = _selectedActeur

    val api_key = "b57151d36fecd1b693da830a2bc5766f";

    fun getMovies() {
        viewModelScope.launch {
            listmovies.value = api.trendingmovies(api_key).results
        }
    }


    fun getActors() {
        viewModelScope.launch {
            listactors.value = api.trendingactors(api_key).results
        }
    }

    fun getSeries() {
        viewModelScope.launch {
            listseries.value = api.trendingseries(api_key).results
        }
    }

    fun getFilmbyId(filmId:Int){
        viewModelScope.launch{
            try{
                val filmDetails = api.getFilmDetails(filmId, api_key, "credits")
                _selectedFilm.value=filmDetails
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getSeriebyId(serieId:Int){
        viewModelScope.launch{
            try{
                val serieDetails = api.getSerieDetails(serieId, api_key, "credits")
                _selectedSerie.value=serieDetails
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getActeurbyId(acteurId:Int){
        viewModelScope.launch{
            try{
                val acteurDetails = api.getActeurDetails(acteurId, api_key, "credits")
                _selectedActeur.value=acteurDetails
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getFilmsbySearch(query: String){
        viewModelScope.launch {
            listmovies.value = api.filmsSearch(api_key, query).results
        }
    }

    fun getSeriesbySearch(query: String){
        viewModelScope.launch {
            listseries.value = api.seriesSearch(api_key, query).results
        }
    }

    fun getActeursbySearch(query: String){
        viewModelScope.launch {
            listactors.value = api.acteursSearch(api_key, query).results
        }
    }
}
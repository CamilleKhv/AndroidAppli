package com.example.premiereappli_camillekerherve

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("trending/movie/week")
    suspend fun trendingmovies(@Query("api_key") api_key: String): ModelListMovies

    @GET("trending/person/week")
    suspend fun trendingactors(@Query("api_key") api_key: String): ModelListActor

    @GET("trending/tv/week")
    suspend fun trendingseries(@Query("api_key") api_key: String): ModelListSeries

    @GET("movie/{id}")
    suspend fun getFilmDetails(
        @Path("id") filmId:Int,
        @Query("api_key") api_key: String,
        @Query("append_to_response") appendToResponse: String="credits" // pour la distribution
    ):ModelMovies

    @GET("tv/{id}")
    suspend fun getSerieDetails(
        @Path("id") serieId:Int,
        @Query("api_key") api_key: String,
        @Query("append_to_response") appendToResponse: String="credits" // pour la distribution
    ):ModelSeries

    @GET("person/{id}")
    suspend fun getActeurDetails(
        @Path("id") acteurId:Int,
        @Query("api_key") api_key: String,
        @Query("append_to_response") appendToResponse: String="credits" // pour la distribution
    ):ModelActeurs


    @GET("search/movie?")
    suspend fun filmsSearch(
        @Query("api_key") api_key: String,
        @Query("query") query:String
    ): ModelListMovies

    @GET("search/tv?")
    suspend fun seriesSearch(
        @Query("api_key") api_key: String,
        @Query("query") query:String
    ): ModelListSeries

    @GET("search/person?")
    suspend fun acteursSearch(
        @Query("api_key") api_key: String,
        @Query("query") query:String
    ): ModelListActor
}


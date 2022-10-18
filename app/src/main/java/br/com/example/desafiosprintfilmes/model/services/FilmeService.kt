package br.com.example.desafiosprintfilmes.model.services

import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.FilmeResposta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmeService {

    @GET("movie/popular")
    fun buscaFilmePopular(
        @Query("api_key") key: String = "9106a44c761c36bbb02f24c16958a56a",
        @Query("page") page: Int,
        @Query("language") language: String = "pt-BR",
        @Query("include_adult") adult: Boolean = false
    ): Call<FilmeResposta>

    @GET("search/movie")
    fun pesquisaFilmes(
        @Query("api_key") key: String = "9106a44c761c36bbb02f24c16958a56a",
        @Query("page") page: Int,
        @Query("query") query: String?,
        @Query("language") language: String = "pt-BR",
        @Query("include_adult") adult: Boolean = false
    ): Call<FilmeResposta>


}
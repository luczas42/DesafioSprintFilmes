package br.com.example.desafiosprintfilmes.model.services

import br.com.example.desafiosprintfilmes.model.FilmeResposta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeService {

    @GET("movie/popular")
    fun buscaFilmePopular(
        @Query("api_key") key: String = "9106a44c761c36bbb02f24c16958a56a",
        @Query("page") page: Int
    ): Call<FilmeResposta>
}
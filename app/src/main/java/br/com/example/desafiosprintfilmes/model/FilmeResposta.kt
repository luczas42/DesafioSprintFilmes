package br.com.example.desafiosprintfilmes.model

import com.google.gson.annotations.SerializedName

data class FilmeResposta(
    @SerializedName("page") val pagina: Int,
    @SerializedName("results") val filmes: List<Filme>,
    @SerializedName("total_pages") val paginas: Int
)
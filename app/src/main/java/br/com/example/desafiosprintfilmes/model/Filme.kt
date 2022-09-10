package br.com.example.desafiosprintfilmes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Filme(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val titulo: String,
    @SerializedName("overview") val descricao: String,
    @SerializedName("poster_path") val imagemPoster: String,
    @SerializedName("backdrop_path") val imagemFundo: String,
    @SerializedName("vote_average") val nota: Float,
    @SerializedName("release_date") val dataLancamento: String
) : Serializable

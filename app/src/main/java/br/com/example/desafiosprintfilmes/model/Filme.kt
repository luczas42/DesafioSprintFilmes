package br.com.example.desafiosprintfilmes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class Filme(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Long,
    @SerializedName("title") val titulo: String,
    @SerializedName("overview") val descricao: String,
    @SerializedName("poster_path") val imagemPoster: String,
    @SerializedName("backdrop_path") val imagemFundo: String,
    @SerializedName("vote_average") val nota: Float,
    @SerializedName("release_date") val dataLancamento: String,
) : Serializable

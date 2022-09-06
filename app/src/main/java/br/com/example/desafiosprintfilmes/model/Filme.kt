package br.com.example.desafiosprintfilmes.model

import androidx.room.Entity

@Entity
data class Filme(
    val id: String,
    val titulo: String,
    val descricao: String,
    val imagemPoster: String,
    val imagemFundo: String,
    val nota: String,
    val dataLancamento: String

)
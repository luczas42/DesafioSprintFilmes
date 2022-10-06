package br.com.example.desafiosprintfilmes.model

import androidx.room.*


@Entity
data class FilmesFavoritos(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "filme_id")
    val filmeId : Long
    )

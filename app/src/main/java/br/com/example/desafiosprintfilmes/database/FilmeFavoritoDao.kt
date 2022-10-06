package br.com.example.desafiosprintfilmes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.FilmesFavoritos


@Dao
interface FilmeFavoritoDao {

    @Query("SELECT * FROM filmesfavoritos")
    fun pegaTodos(): List<FilmesFavoritos>

    @Query("INSERT INTO filmesfavoritos (filme_id) VALUES (:filmeId)")
    suspend fun insereFilme(filmeId: Long)

    @Query("DELETE FROM filmesfavoritos WHERE filme_id = :filmeId")
    suspend fun removeFilme(filmeId: Long)

    @Query("SELECT EXISTS (SELECT 1 FROM filmesfavoritos WHERE filme_id = :filmeSelecionado)")
    suspend fun checaExiste(filmeSelecionado: Long):Boolean


}
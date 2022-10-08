package br.com.example.desafiosprintfilmes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.example.desafiosprintfilmes.model.Filme


@Dao
interface FilmeFavoritoDao {

    @Query("SELECT * FROM Filme")
    suspend fun pegaTodos(): List<Filme>

    @Insert
    suspend fun insereFilme(filme: Filme)

    @Delete
    suspend fun removeFilme(filme: Filme)

    @Query("SELECT EXISTS (SELECT 1 FROM filme WHERE id = :idSelecionado)")
    suspend fun checaExiste(idSelecionado: Long): Boolean
}
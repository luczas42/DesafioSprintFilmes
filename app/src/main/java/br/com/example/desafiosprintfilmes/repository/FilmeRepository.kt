package br.com.example.desafiosprintfilmes.repository

import androidx.lifecycle.LiveData
import br.com.example.desafiosprintfilmes.database.FilmeFavoritoDao
import br.com.example.desafiosprintfilmes.model.Filme

class FilmeRepository(
    private val favoritosDao: FilmeFavoritoDao
) {
    suspend fun pegaTodos(): List<Filme> {
        return favoritosDao.pegaTodos()
    }

    suspend fun checaExiste(filme: Filme): Boolean {
        return favoritosDao.checaExiste(filme.id)
    }

    suspend fun removeFilme(filme: Filme) {
        favoritosDao.removeFilme(filme)
    }

    suspend fun insereFilme(filme: Filme) {
        favoritosDao.insereFilme(filme)
    }


}
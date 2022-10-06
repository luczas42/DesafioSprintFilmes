package br.com.example.desafiosprintfilmes.repository

import br.com.example.desafiosprintfilmes.database.FilmeFavoritoDao
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.FilmesFavoritos

class FilmeRepository(
private val dao: FilmeFavoritoDao
)
{
    suspend fun checaExiste(filme: Filme): Boolean{
        return dao.checaExiste(filme.id)
    }

    suspend fun insereFilme(filme: Filme){

        return dao.insereFilme(filme.id)
    }

    suspend fun removeFilme(filme: Filme){
        return dao.removeFilme(filme.id)
    }

}
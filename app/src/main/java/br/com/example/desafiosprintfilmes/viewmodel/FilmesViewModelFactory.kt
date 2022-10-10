package br.com.example.desafiosprintfilmes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.example.desafiosprintfilmes.repository.FilmeRepository

class FilmesViewModelFactory(val repository: FilmeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FilmeRepository::class.java).newInstance(repository)
    }
}
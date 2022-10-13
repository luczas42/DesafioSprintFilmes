package br.com.example.desafiosprintfilmes.viewmodel
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.FilmeResposta
import br.com.example.desafiosprintfilmes.model.RetrofitInicializador
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilmesViewModel(val repository: FilmeRepository) : ViewModel() {
    var filmesPopularesPagina = 1
    private var filmesLista = MutableLiveData<List<Filme>>()
    private var filmesFavoritosLista = MutableLiveData<List<Filme>>()
    var modoSelecao = false
    var _filmeSelecionado = MutableLiveData<Filme>()
    var filmeSelecionado : LiveData<Filme> = _filmeSelecionado
    var favorito = MutableLiveData<Boolean>()

    fun checaFavorito(filme:Filme){
        viewModelScope.launch {
            favorito.value = repository.checaExiste(filme)
        }

    }

    fun removeFilmes(filmes: List<Filme>){
        viewModelScope.launch {
            for (element in filmes){
                repository.removeFilme(element)
            }
        }
    }

    fun alteraFavorito(filme:Filme) {
        viewModelScope.launch {
            if (repository.checaExiste(filme)) {
                favorito.value = true
                repository.removeFilme(filme)
            } else {
                favorito.value = false
                repository.insereFilme(filme)
            }
        }
    }


    fun pegaFilmePopular() {
        RetrofitInicializador.filmeService.buscaFilmePopular(page = filmesPopularesPagina)
            .enqueue(object : Callback<FilmeResposta> {
                override fun onResponse(
                    call: Call<FilmeResposta>,
                    response: Response<FilmeResposta>
                ) {
                    if (response.isSuccessful) {
                        val resposta = response.body()
                        if (resposta != null) {
                            filmesLista.value = resposta.filmes
                        } else {
                            Log.d("Filmes", "Sem Resposta")
                        }
                    }
                }

                override fun onFailure(call: Call<FilmeResposta>, t: Throwable) {
                    Log.e("Filmes", "onFailure", t)
                }
            })
    }

    fun pegaFilmesFavoritos(){
        viewModelScope.launch {
            filmesFavoritosLista.value = repository.pegaTodos()
        }
    }

    fun observaFilmeLiveData(): LiveData<List<Filme>> {
        return filmesLista
    }

    fun observaFilmeFavoritoLiveData(): LiveData<List<Filme>>{
        return filmesFavoritosLista
    }

    fun observaFavoritoLiveData(): LiveData<Boolean>{
        return favorito
    }
}


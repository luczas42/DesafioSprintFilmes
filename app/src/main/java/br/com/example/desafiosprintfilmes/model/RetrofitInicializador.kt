package br.com.example.desafiosprintfilmes.model

import android.util.Log
import br.com.example.desafiosprintfilmes.model.services.FilmeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInicializador {
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val filmeService: FilmeService

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        filmeService = retrofit.create(FilmeService::class.java)
    }


    fun pegaFilmePopular(
        page: Int = 1,
        success: (filmes: List<Filme>) -> Unit
    ) {
        filmeService.buscaFilmePopular(page = page)
            .enqueue(object : Callback<FilmeResposta> {
                override fun onResponse(
                    call: Call<FilmeResposta>,
                    response: Response<FilmeResposta>
                ) {
                    if (response.isSuccessful) {
                        val resposta = response.body()
                        if (resposta != null) {
                            success(resposta.filmes)
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
}
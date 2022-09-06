package br.com.example.desafiosprintfilmes.model

data class FilmeResposta(
    val pagina: Int,
    val filmes: List<Filme>,
    val paginas: Int
)
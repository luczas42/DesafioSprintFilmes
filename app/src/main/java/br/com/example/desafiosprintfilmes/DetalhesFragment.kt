package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import br.com.example.desafiosprintfilmes.database.FilmesDatabase
import br.com.example.desafiosprintfilmes.databinding.FragmentDetalhesBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModel
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetalhesFragment : Fragment() {


    private lateinit var fundoFilme: ImageView
    private lateinit var capaFilme: ImageView
    private lateinit var tituloFilme: TextView
    private lateinit var lancamentoFilme: TextView
    private lateinit var descricaoFilme: TextView
    private lateinit var filmeNota: TextView
    private lateinit var fabAddFavorito: FloatingActionButton
    private val repository by lazy {
        FilmeRepository(
            favoritosDao = FilmesDatabase.pegaDatabase(requireContext()).filmeFavoritoDao()
        )
    }
    private val viewModel: FilmesViewModel by activityViewModels {
        FilmesViewModelFactory(repository)
    }


    private var _binding: FragmentDetalhesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val filme: Filme = viewModel.filmeSelecionado.value!!
        val filmeDataFormatada = formataLancamentoFilme(filme.dataLancamento)
        val linkCapa = filme.imagemPoster
        val linkFundo = filme.imagemFundo

        _binding = FragmentDetalhesBinding.inflate(inflater, container, false)
        inicializaCampos()
        viewModel.checaFavorito(filme)
        alteraBotao()


        if (filme.titulo.equals(null)) {
            tituloFilme.text = ""
        } else {
            tituloFilme.text = filme.titulo
        }
        if (filme.dataLancamento.equals(null)) {
            lancamentoFilme.text = ""
        } else {
            lancamentoFilme.text = filmeDataFormatada.toString()

        }
        if (filme.descricao.equals(null)) {
            descricaoFilme.text = ""
        } else {
            descricaoFilme.text = filme.descricao
        }
        if (filme.nota.equals(null)) {
            filmeNota.text = ""
        } else {
            filmeNota.text = filme.nota.toString()
        }
        if (!linkCapa.equals(null)) {
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${linkCapa}").centerCrop()
                .into(capaFilme)
        }
        if (!linkFundo.equals(null)) {
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${linkFundo}").centerCrop()
                .into(fundoFilme)
        }

        fabAddFavorito.setOnClickListener {
            viewModel.alteraFavorito(filme)
            alteraBotao()
        }


    return binding.root

}

    private fun alteraBotao() {
        viewModel.observaFavoritoLiveData().observe(viewLifecycleOwner){ favorito ->
            if (favorito) {
                fabAddFavorito.setImageDrawable(
                    (
                            resources.getDrawable(
                                R.drawable.ic_baseline_star_24,
                                null
                            )
                            )
                )
            } else {
                fabAddFavorito.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_baseline_star_border_24,
                        null
                    )
                )
            }
        }

    }

    private fun inicializaCampos() {
    fundoFilme = binding.fragmentSecondFilmeBackground
    capaFilme = binding.fragmentSecondFilmeCapa
    tituloFilme = binding.fragmentSecondFilmeTitulo
    lancamentoFilme = binding.fragmentSecondFilmeAno
    descricaoFilme = binding.fragmentSecondFilmeDescricao
    filmeNota = binding.fragmentSecondFilmeNota
    fabAddFavorito = binding.fabAdicionaFavorito
}

private fun formataLancamentoFilme(dataLancamento: String): Any {
    val data = LocalDate.parse(dataLancamento)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return data.format(formatter)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

}


override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}
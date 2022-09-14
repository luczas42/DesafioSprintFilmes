package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.example.desafiosprintfilmes.databinding.FragmentSecondBinding
import br.com.example.desafiosprintfilmes.model.Filme
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private lateinit var fundoFilme: ImageView
    private lateinit var capaFilme: ImageView
    private lateinit var tituloFilme: TextView
    private lateinit var lancamentoFilme: TextView
    private lateinit var descricaoFilme: TextView
    private lateinit var filmeNota: TextView

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val filme: Filme = arguments?.getSerializable("filmeSelecionado") as Filme

        val filmeDataFormatada = formataLancamentoFilme(filme.dataLancamento)
        val linkCapa = filme.imagemPoster
        val linkFundo = filme.imagemFundo

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        inicializaCampos()

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
        return binding.root

    }

    private fun inicializaCampos() {
        fundoFilme = binding.fragmentSecondFilmeBackground
        capaFilme = binding.fragmentSecondFilmeCapa
        tituloFilme = binding.fragmentSecondFilmeTitulo
        lancamentoFilme = binding.fragmentSecondFilmeAno
        descricaoFilme = binding.fragmentSecondFilmeDescricao
        filmeNota = binding.fragmentSecondFilmeNota
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
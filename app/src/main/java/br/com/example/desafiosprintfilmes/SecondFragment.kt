package br.com.example.desafiosprintfilmes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import br.com.example.desafiosprintfilmes.databinding.FragmentSecondBinding
import br.com.example.desafiosprintfilmes.model.Filme

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
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        fundoFilme = binding.fragmentSecondFilmeBackground
        capaFilme = binding.fragmentSecondFilmeCapa
        tituloFilme = binding.fragmentSecondFilmeTitulo
        lancamentoFilme = binding.fragmentSecondFilmeAno
        descricaoFilme = binding.fragmentSecondFilmeDescricao
        filmeNota = binding.fragmentSecondFilmeNota

        val args = this.arguments

        tituloFilme.text = args?.getString("filmeNome")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
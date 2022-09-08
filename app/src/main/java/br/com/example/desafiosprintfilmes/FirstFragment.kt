package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.adapter.RecyclerFilmesAdapter
import br.com.example.desafiosprintfilmes.databinding.FragmentFirstBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.RetrofitInicializador

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        RetrofitInicializador.pegaFilmePopular(
            success = ::filmesPopularesEncontrados
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun filmesPopularesEncontrados(filmes: List<Filme>){
        val recyclerView = binding.recyclerViewFilmes
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = RecyclerFilmesAdapter(filmes,requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
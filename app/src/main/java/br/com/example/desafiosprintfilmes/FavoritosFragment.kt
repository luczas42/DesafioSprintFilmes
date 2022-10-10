package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.adapter.RecyclerFilmesAdapter
import br.com.example.desafiosprintfilmes.database.FilmesDatabase
import br.com.example.desafiosprintfilmes.databinding.FragmentFavoritosBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModel
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModelFactory

class FavoritosFragment : Fragment() {
    private lateinit var recyclerFilmes: RecyclerView

    private val repository by lazy {
        FilmeRepository(
            favoritosDao = FilmesDatabase.pegaDatabase(requireContext()).filmeFavoritoDao()
        )
    }
    private val viewModel: FilmesViewModel by activityViewModels {
        FilmesViewModelFactory(repository)
    }
    private val recyclerFilmesAdapter by lazy { RecyclerFilmesAdapter() }

    private lateinit var recyclerFilmesLayoutManager: GridLayoutManager


    private var _binding: FragmentFavoritosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configuraRecycler()
        pegaFavoritos()
        configuraOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun pegaFavoritos() {
        viewModel.pegaFilmesFavoritos()
        viewModel.observaFilmeFavoritoLiveData().observe(viewLifecycleOwner) { filmes ->
            recyclerFilmesAdapter.atualizaListaFilmesFavoritos(filmes as MutableList<Filme>)
        }
    }

    override fun onResume() {
        pegaFavoritos()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    private fun configuraOnClickListener() {
        recyclerFilmesAdapter.setOnItemClickListener(object :
            RecyclerFilmesAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                viewModel._filmeSelecionado.value = recyclerFilmesAdapter.pegaFilmeSelecionado(position)
                vaiParaSecondFragment()
            }

        })
    }

    private fun vaiParaSecondFragment() {
        parentFragment?.let {
            NavHostFragment.findNavController(it)
                .navigate(R.id.action_favoritosFragment_to_SecondFragment)
        }
    }

    private fun configuraRecycler() {
        recyclerFilmes = binding.recyclerViewFilmesFavoritos
        recyclerFilmesLayoutManager = GridLayoutManager(context, 4)
        recyclerFilmes.layoutManager = recyclerFilmesLayoutManager
        recyclerFilmes.adapter = recyclerFilmesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
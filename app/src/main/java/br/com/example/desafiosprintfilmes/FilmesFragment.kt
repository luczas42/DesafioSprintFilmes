package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.adapter.RecyclerFilmesAdapter
import br.com.example.desafiosprintfilmes.database.FilmesDatabase
import br.com.example.desafiosprintfilmes.databinding.FragmentFilmesBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModel
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModelFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FilmesFragment : Fragment() {
    private lateinit var recyclerFilmes: RecyclerView
    private val repository by lazy {
        FilmeRepository(
            favoritosDao = FilmesDatabase.pegaDatabase(requireContext()).filmeFavoritoDao())
    }
    private val viewModel: FilmesViewModel by activityViewModels {
        FilmesViewModelFactory(repository)
    }
    private val recyclerFilmesAdapter by lazy { RecyclerFilmesAdapter() }

    private lateinit var recyclerFilmesLayoutManager: GridLayoutManager
    private lateinit var recyclerViewFilmesObservador: RecyclerView.OnScrollListener


    private var _binding: FragmentFilmesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configuraRecycler()
//        configuraViewModel()
        pegaPopulares()
        configuraOnClickListener()
        super.onViewCreated(view, savedInstanceState)

    }
//
//    private fun configuraViewModel() {
//        viewModelFactory = FilmesViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory)[FilmesViewModel::class.java]
//    }

    private fun pegaPopulares() {
        viewModel.pegaFilmePopular()
        viewModel.observaFilmeLiveData().observe(viewLifecycleOwner) { filmes ->
            recyclerFilmesAdapter.atualizaListaFilmes(filmes as MutableList<Filme>)
            if (::recyclerViewFilmesObservador.isInitialized) {
                recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
                recyclerFilmes.addOnScrollListener(recyclerViewFilmesObservador)
            }
        }
    }

    private fun anexaFilmesOnScrollListener() {
        recyclerViewFilmesObservador = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemVisible = visibleItemCount + pastVisibleItems
                    val totalItemCount = layoutManager.itemCount

                    if (totalItemVisible >= totalItemCount) {
                        viewModel.filmesPopularesPagina += 1
                        recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
                        pegaPopulares()

                    }
                }

            }
        }
        recyclerFilmes.addOnScrollListener(recyclerViewFilmesObservador)
    }

    override fun onResume() {

        anexaFilmesOnScrollListener()

        super.onResume()
    }

    override fun onPause() {
        recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
        super.onPause()
    }


    private fun configuraOnClickListener() {
        recyclerFilmesAdapter.setOnItemClickListener(object :
            RecyclerFilmesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                viewModel._filmeSelecionado.value = recyclerFilmesAdapter.pegaFilmeSelecionado(position)
                vaiParaSecondFragment()
            }

        })
    }

    private fun vaiParaSecondFragment() {
        parentFragment?.let {
            NavHostFragment.findNavController(it)
                .navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun configuraRecycler() {
        recyclerFilmes = binding.recyclerViewFilmes
        recyclerFilmesLayoutManager = GridLayoutManager(context, 4)
        recyclerFilmes.layoutManager = recyclerFilmesLayoutManager
        recyclerFilmes.adapter = recyclerFilmesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
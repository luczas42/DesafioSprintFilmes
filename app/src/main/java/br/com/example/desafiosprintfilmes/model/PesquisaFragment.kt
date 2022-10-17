package br.com.example.desafiosprintfilmes.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.R
import br.com.example.desafiosprintfilmes.adapter.RecyclerFilmesAdapter
import br.com.example.desafiosprintfilmes.database.FilmesDatabase
import br.com.example.desafiosprintfilmes.databinding.FragmentPesquisaBinding
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModel
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModelFactory

class PesquisaFragment : Fragment() {

    private lateinit var recyclerFilmes: RecyclerView
    private lateinit var searchViewFilmes: SearchView
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
    private lateinit var recyclerViewFilmesObservador: RecyclerView.OnScrollListener

    private var _binding: FragmentPesquisaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesquisaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configuraSearchView()
        configuraRecycler()
        configuraOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun configuraSearchView() {
        searchViewFilmes = binding.searchViewFilmes

        searchViewFilmes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == ""){
                    recyclerFilmesAdapter.atualizaPesquisados()
                }else{
                    viewModel.queryPesquisa = newText!!
                    recyclerFilmesAdapter.atualizaPesquisados()
                    viewModel.filmesPesquisadosPagina = 1
                    pegaPesquisados()
                }
                return false
            }

        })
    }

    private fun pegaPesquisados() {
        viewModel.pesquisaFilmes()
        viewModel.observaPesquisadosLiveData().observe(viewLifecycleOwner) { filmes ->
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
                        if (viewModel.filmesPesquisadosPagina <= viewModel.filmesPesquisadosTotalPaginas) {
                            viewModel.filmesPesquisadosPagina += 1
                            recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
                            pegaPesquisados()
                        }
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
                viewModel._filmeSelecionado.value =
                    recyclerFilmesAdapter.pegaFilmeSelecionado(position)
                vaiParaSecondFragment()
            }

        })
    }

    private fun vaiParaSecondFragment() {
        parentFragment?.let {
            NavHostFragment.findNavController(it)
                .navigate(R.id.action_pesquisaFragment_to_SecondFragment)
        }
    }

    private fun configuraRecycler() {
        recyclerFilmes = binding.recyclerViewFilmesPesquisados
        recyclerFilmesLayoutManager = GridLayoutManager(context, 4)
        recyclerFilmes.layoutManager = recyclerFilmesLayoutManager
        recyclerFilmes.adapter = recyclerFilmesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
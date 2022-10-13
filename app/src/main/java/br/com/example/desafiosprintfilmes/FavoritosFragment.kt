package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.adapter.RecyclerFavoritosAdapter
import br.com.example.desafiosprintfilmes.database.FilmesDatabase
import br.com.example.desafiosprintfilmes.databinding.FragmentFavoritosBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.repository.FilmeRepository
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModel
import br.com.example.desafiosprintfilmes.viewmodel.FilmesViewModelFactory

class FavoritosFragment : Fragment() {
    private lateinit var menuHost: MenuHost
    private lateinit var recyclerFilmes: RecyclerView
    private lateinit var provider: MenuProvider


    private val repository by lazy {
        FilmeRepository(
            favoritosDao = FilmesDatabase.pegaDatabase(requireContext()).filmeFavoritoDao()
        )
    }
    private val viewModel: FilmesViewModel by activityViewModels {
        FilmesViewModelFactory(repository)
    }
    private val recyclerFilmesAdapter by lazy {
        RecyclerFavoritosAdapter()
    }
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
        menuHost = requireActivity()
        configuraProvider()
        viewModel.modoSelecao = false
        configuraMenuDeletar()
        configuraRecycler()
        pegaFavoritos()
        configuraOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun configuraMenuDeletar() {
        menuHost.removeMenuProvider(provider)
        menuHost.addMenuProvider(provider)
    }

    private fun configuraProvider() {
        provider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                exibirMenuDeletar(menu)
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_deletar -> {
                        val filmesDeletar: List<Filme> =
                            recyclerFilmesAdapter.mostraListaDeSelecionadods()
                        Toast.makeText(
                            requireContext(),
                            filmesDeletar.size.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.removeFilmes(filmesDeletar)
                        recyclerFilmesAdapter.removeSelecionandos()
                        viewModel.modoSelecao = !viewModel.modoSelecao
                        menuHost.invalidateMenu()
                        true
                    }
                    R.id.menu_selecionar_todos -> {
                        recyclerFilmesAdapter.selecionaTodos()
                        menuHost.invalidateMenu()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun exibirMenuDeletar(menu: Menu) {
        if (viewModel.modoSelecao) {
            menu.findItem(R.id.menu_deletar).isVisible = viewModel.modoSelecao
            menu.findItem(R.id.menu_selecionar_todos).isVisible = viewModel.modoSelecao
        } else {
            menu.clear()
        }
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
            RecyclerFavoritosAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (viewModel.modoSelecao) {
                    recyclerFilmesAdapter.alteraSelecao(position)
                } else {
                    viewModel._filmeSelecionado.value =
                        recyclerFilmesAdapter.pegaFilmeSelecionado(position)
                    vaiParaSecondFragment()
                }
            }
        })
        recyclerFilmesAdapter.onItemLongClicked = { position ->
            viewModel.modoSelecao = !viewModel.modoSelecao
            menuHost.invalidateMenu()
            if (viewModel.modoSelecao) {
                recyclerFilmesAdapter.alteraSelecao(position)
            } else {
                configuraMenuDeletar()
                recyclerFilmesAdapter.limpaSelecao()
            }

        }
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
        menuHost.removeMenuProvider(provider)
        super.onDestroyView()
        _binding = null
    }

}
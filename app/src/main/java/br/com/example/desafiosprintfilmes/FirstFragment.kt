package br.com.example.desafiosprintfilmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.desafiosprintfilmes.adapter.RecyclerFilmesAdapter
import br.com.example.desafiosprintfilmes.databinding.FragmentFirstBinding
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.RetrofitInicializador

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var recyclerFilmes: RecyclerView

    private val recyclerFilmesAdapter by lazy { RecyclerFilmesAdapter() }
    private val filmes: MutableList<Filme> = mutableListOf()

    private lateinit var recyclerFilmesLayoutManager: GridLayoutManager
    private lateinit var recyclerViewFilmesObservador: RecyclerView.OnScrollListener


    private var filmesPopularesPagina = 1


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        return binding.root

    }

    private fun pegaFilmesPopulares() {
        RetrofitInicializador.pegaFilmePopular(
            filmesPopularesPagina,
            success = ::filmesPopularesEncontrados
        )
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
                        recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
                        filmesPopularesPagina ++
                        pegaFilmesPopulares()

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerFilmes = binding.recyclerViewFilmes
        recyclerFilmesLayoutManager = GridLayoutManager(context, 2)
        recyclerFilmes.layoutManager = recyclerFilmesLayoutManager


        recyclerFilmes.adapter = recyclerFilmesAdapter
        recyclerFilmesAdapter.setOnItemClickListener(object : RecyclerFilmesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val filme: Filme = recyclerFilmesAdapter.pegaFilmeSelecionado(position)

                val fragment: Fragment = SecondFragment()
                val bundle = Bundle()
                bundle.putSerializable("filmeSelecionado", filme)
                fragment.arguments = bundle
                parentFragment?.let { NavHostFragment.findNavController(it).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle) }
            }

        })
        pegaFilmesPopulares()
        super.onViewCreated(view, savedInstanceState)

    }


    private fun filmesPopularesEncontrados(filmes: MutableList<Filme>) {
        recyclerFilmesAdapter.atualizaListaFilmes(filmes)
        if(::recyclerViewFilmesObservador.isInitialized){
            recyclerFilmes.removeOnScrollListener(recyclerViewFilmesObservador)
            recyclerFilmes.addOnScrollListener(recyclerViewFilmesObservador)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
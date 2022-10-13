package br.com.example.desafiosprintfilmes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import br.com.example.desafiosprintfilmes.R
import br.com.example.desafiosprintfilmes.databinding.RecyclerviewFilmeItemBinding
import br.com.example.desafiosprintfilmes.model.Filme
import com.bumptech.glide.Glide

class RecyclerFavoritosAdapter() :
    Adapter<RecyclerFavoritosAdapter.ViewHolder>() {


    private var filmes: MutableList<Filme> = mutableListOf()
    private var filmesSelecionados: MutableList<Filme> = mutableListOf()

    private lateinit var botaoSelecionado: ImageView

    private lateinit var mListener: OnItemClickListener
    lateinit var onItemLongClicked: (position: Int) -> Unit


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class ViewHolder(
        view: View,
        listener: OnItemClickListener,
        private val binding: RecyclerviewFilmeItemBinding,
    ) : RecyclerView.ViewHolder(view) {

        fun vincula(filme: Filme) {
            botaoSelecionado = binding.botaoSelecionado
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500${filme.imagemPoster}")
                .into(binding.recyclerviewFilmeItemCapaFilme)

            itemView.setOnLongClickListener {
                onItemLongClicked.invoke(adapterPosition)
                true
            }
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_filme_item, parent, false)
        return ViewHolder(view, mListener, RecyclerviewFilmeItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(filmes[position])
        botaoSelecionado.isClickable = false
        botaoSelecionado.isVisible = isSelected(position)
    }


    fun atualizaListaFilmesFavoritos(filmes: MutableList<Filme>) {
        this.filmes.clear()
        this.filmes.addAll(filmes)
        notifyDataSetChanged()
    }

    fun removeSelecionandos(){
        for (i in 0 until filmesSelecionados.size){
            this.filmes.remove(filmesSelecionados[i])
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return filmes.size
    }

    fun pegaFilmeSelecionado(position: Int): Filme {
        return this.filmes[position]
    }

    fun isSelected(position: Int): Boolean {
        return filmesSelecionados.contains(filmes[position])
    }

    fun alteraSelecao(position: Int) {
        if (isSelected(position)) {
            filmesSelecionados.remove(filmes[position])
        } else {
            filmesSelecionados.add(filmes[position])
        }
        notifyItemChanged(position)

    }

    fun selecionaTodos() {
        for (i in 0 until filmes.size) {
            if (!isSelected(i)) {
                filmesSelecionados.add(filmes[i])
            }
            notifyItemChanged(i)
        }
    }

    fun limpaSelecao() {
        botaoSelecionado.visibility = View.INVISIBLE
        filmesSelecionados.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItemCount(): Int {
        return filmesSelecionados.size
    }

    fun mostraListaDeSelecionadods(): List<Filme> {
        return filmesSelecionados
    }
}

package br.com.example.desafiosprintfilmes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import br.com.example.desafiosprintfilmes.R
import br.com.example.desafiosprintfilmes.databinding.RecyclerviewFilmeItemBinding
import br.com.example.desafiosprintfilmes.model.Filme
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import java.io.Serializable

class RecyclerFilmesAdapter() :
    Adapter<RecyclerFilmesAdapter.ViewHolder>() {

    private val filmes: MutableList<Filme> = mutableListOf()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(
        view: View, listener: onItemClickListener,
        private val binding: RecyclerviewFilmeItemBinding
    ) : RecyclerView.ViewHolder(view) {
        fun vincula(filme: Filme) {

            val ano = filme.dataLancamento.take(4)

            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500${filme.imagemPoster}")
                .into(binding.recyclerviewFilmeItemCapaFilme)
            binding.recyclerviewFilmeItemAnoFilme.text = ano
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_filme_item, parent, false)
        return ViewHolder(view, mListener, RecyclerviewFilmeItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(filmes[position])

    }

    fun atualizaListaFilmes(filmes: MutableList<Filme>){

        val oldItemRange = this.filmes.size
        val newItemRange = filmes.size
        this.filmes.addAll(filmes)
        notifyItemRangeInserted(oldItemRange, newItemRange)
    }




    override fun getItemCount(): Int {
        return filmes.size
    }

    fun pegaFilmeSelecionado(position: Int): Filme {
        return this.filmes[position]
    }
}

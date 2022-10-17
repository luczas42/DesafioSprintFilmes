package br.com.example.desafiosprintfilmes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import br.com.example.desafiosprintfilmes.R
import br.com.example.desafiosprintfilmes.databinding.RecyclerviewFilmeItemBinding
import br.com.example.desafiosprintfilmes.model.Filme
import com.bumptech.glide.Glide

class RecyclerFilmesAdapter(
) :
    Adapter<RecyclerFilmesAdapter.ViewHolder>() {

    private var filmes: MutableList<Filme> = mutableListOf()



    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class ViewHolder(
        view: View, listener: OnItemClickListener,
        private val binding: RecyclerviewFilmeItemBinding
    ) : RecyclerView.ViewHolder(view) {
        fun vincula(filme: Filme) {

            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500${filme.imagemPoster}")
                .into(binding.recyclerviewFilmeItemCapaFilme)
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

    @SuppressLint("NotifyDataSetChanged")
    fun atualizaPesquisados() {
        this.filmes.clear()
        notifyDataSetChanged()
    }

    fun atualizaListaFilmes(filmes: MutableList<Filme>){
        val oldItemRange = this.filmes.size
        val newItemRange = filmes.size
        this.filmes.removeAll(filmes)
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

package com.loguito.clase6.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loguito.clase6.R
import com.loguito.clase6.views.db.MarvelCharacter
import kotlinx.android.synthetic.main.marvel_character_cell.view.*

class MarvelListAdapter(val clickListener: (MarvelCharacter) -> Unit) :
    RecyclerView.Adapter<MarvelListAdapter.MarvelCharacterViewHolder>() {

    var marvelCharacterList: List<MarvelCharacter> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MarvelCharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(marvelCharacter: MarvelCharacter) {

            if (marvelCharacter.isFavorite)
                itemView.favorite.setImageResource(R.drawable.ic_baseline_star_rate_24)
            else
                itemView.favorite.setImageResource(R.drawable.ic_baseline_star_border_24)


            itemView.nameTextView.text = marvelCharacter.name
            itemView.descriptionTextView.text = marvelCharacter.description
            Glide.with(itemView.context)
                .load(marvelCharacter.thumbnail)
                .circleCrop()
                .into(itemView.characterImageView)

            itemView.setOnClickListener {
                clickListener.invoke(marvelCharacter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marvel_character_cell, parent, false)
        return MarvelCharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarvelCharacterViewHolder, position: Int) {
        holder.bind(marvelCharacterList[position])
    }

    override fun getItemCount() = marvelCharacterList.size
}
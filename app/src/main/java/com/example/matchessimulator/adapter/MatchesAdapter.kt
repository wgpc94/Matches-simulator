package com.example.matchessimulator.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchessimulator.databinding.MatchItemBinding
import com.example.matchessimulator.domain.Match
import com.example.matchessimulator.ui.DetailActivity

data class MatchesAdapter(
    val matchList: MutableList<Match>
) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(matchList[position])
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    class ViewHolder(private val binding : MatchItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(match: Match){
            Glide.with(binding.root.context).load(match.homeTeam.image).circleCrop().into(binding.imageHomeTeam)
            Glide.with(binding.root.context).load(match.visitingTeam.image).circleCrop().into(binding.imageVisitingTeam)
            if (match.homeTeam.score != null){
                binding.scoreHomeTeam.text =  match.homeTeam.score.toString()
            }
            if (match.visitingTeam.score != null){
                binding.scoreVisitingTeam.text = match.visitingTeam.score.toString()
            }
            binding.nameHomeTeam.text = match.homeTeam.name
            binding.nameVisitingTeam.text = match.visitingTeam.name
            binding.root.setOnClickListener{
                val intent = Intent(it.context,DetailActivity::class.java)
                intent.putExtra(DetailActivity.Extras.Match, match)
                it.context.startActivity(intent)
            }
        }
    }
}
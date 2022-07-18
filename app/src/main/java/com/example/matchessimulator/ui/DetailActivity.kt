package com.example.matchessimulator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.matchessimulator.databinding.ActivityDetailBinding
import com.example.matchessimulator.domain.Match

class DetailActivity : AppCompatActivity() {
    object Extras {
        const val Match = "MATCH_EXTRAS"
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loandingMacthFromExtras()
    }

    private fun loandingMacthFromExtras() {
        intent.extras?.getParcelable<Match>(Extras.Match).let {
            supportActionBar?.title = it?.place?.name
            Glide.with(this).load(it?.place?.image).into(binding.ivPlace)
            Glide.with(this).load(it?.homeTeam?.image).into(binding.ivHomeTeam)
            Glide.with(this).load(it?.visitingTeam?.image).into(binding.ivAwayTeam)
            if (it != null) {
                binding.rbHomeTeamStars.rating = it.homeTeam.stars.toFloat()
            }
            if (it != null) {
                binding.rbAwayTeamStars.rating = it.visitingTeam.stars.toFloat()
            }
        }
    }
}
package com.example.matchessimulator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchessimulator.adapter.MatchesAdapter
import com.example.matchessimulator.data.MatchesApi
import com.example.matchessimulator.databinding.ActivityMainBinding
import com.example.matchessimulator.domain.Match
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var matchesApi: MatchesApi
    private lateinit var matchesAdapter: MatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHTTPClient()
        setupMatchesList()
        setupMatchesRefresh()
        setupFloatingActionButton()
    }

    private fun setupHTTPClient() {
        matchesApi = Retrofit.Builder()
            .baseUrl("https://wgpc94.github.io/matches_simulator_api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatchesApi::class.java)
    }

    private fun setupMatchesRefresh() {
        binding.srLayout.setOnRefreshListener(this::findMatches)
    }

    private fun setupFloatingActionButton() {
        binding.fabSimulator.setOnClickListener {
            it.animate().rotationBy(360F).setDuration(500).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                   val random = Random()
                    for (i in 0 until matchesAdapter.itemCount){
                        val match = matchesAdapter.matchList[i]
                        match.homeTeam.score = random.nextInt(match.homeTeam.stars)
                        match.visitingTeam.score = random.nextInt(match.visitingTeam.stars)
                        matchesAdapter.notifyItemChanged(i)
                    }
                }
            })
        }
    }

    private fun setupMatchesList() {
        binding.rvMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        findMatches()
    }

    private fun findMatches() {
        binding.srLayout.isRefreshing = true
        matchesApi.findMatches().enqueue(object : Callback<List<Match>> {
            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                val matchList: List<Match>
                if (response.isSuccessful && response.body() != null){
                    matchList = response.body()!!
                    matchesAdapter = MatchesAdapter(matchList.toMutableList())
                    binding.rvMain.adapter = matchesAdapter
                }else{
                    showError(response.errorBody()?.string() ?: "Error")
                }
                binding.srLayout.isRefreshing = false
            }
            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                showError(t.message ?: "Error")
                binding.srLayout.isRefreshing = false
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(binding.fabSimulator,message,Snackbar.LENGTH_LONG).show()
    }
}
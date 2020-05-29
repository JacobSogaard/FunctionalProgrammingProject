package org.jetbrains.kotlin.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tournamentandroid.Presenters.TournamentPresenter
import com.example.tournamentandroid.R
import com.example.tournamentandroid.TournamentFinishedActivity
import com.example.tournamentandroid.kotlin.activities.TournamentViewModel
import enums.TournamentType

import kotlinx.android.synthetic.main.activity_tournament.*
import models.Game
import models.Team

class TournamentActivity : AppCompatActivity() {
    private lateinit var _viewModel: TournamentViewModel
    private lateinit var _presenter: TournamentPresenter
    private lateinit var currentGame: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)
        val listItems: ArrayList<Team>? = intent.extras?.get("Teams") as? ArrayList<Team>
        _viewModel = listItems?.let { TournamentViewModel(TournamentType.SINGLEELIMINATION, it) }!!
        _presenter = _viewModel._presenter
        _presenter.start()

        currentGame = _presenter.getNextMatch()!!
        updateView(currentGame)
        concludeGameBTN.setOnClickListener {
            currentGame.team1?.let { it1 -> currentGame.setMatchWinner(it1, _presenter.getCurrentRound()) }
            _presenter.concludeMatch(currentGame)

            if (_presenter.getNextMatch() == null) {
                val intent = Intent()
                intent.setClass(this, TournamentFinishedActivity::class.java)
                intent.putExtra("winner", "done")
                startActivity(intent)
            } else {
                currentGame = _presenter.getNextMatch()!!
            }

            updateView(currentGame)

        }



    }

    private fun updateView(game: Game) {
        team1NameTW.text = game.team1?.teamName ?: ""
        team1PlayersTW.text = game.team1?.players()
        team2NameTW.text = game.team2?.teamName ?: ""
        team2PlayersTW.text = game.team2?.players()

    }
}

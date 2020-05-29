package org.jetbrains.kotlin.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tournamentandroid.R
import kotlinx.android.synthetic.main.setup_tournament.*
import models.Team

class SetupTournamentActivity : AppCompatActivity() {
    private lateinit var viewModel: SetupTournamentViewModel
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "tournament_app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setup_tournament)
        this.setupButtons()

        val sp = this.getSharedPreferences(PREF_NAME,PRIVATE_MODE)


        this.viewModel = SetupTournamentViewModel()
        val listItems: ArrayList<Team>? = intent.extras?.get("Teams") as? ArrayList<Team>
        if (listItems != null) {
            this.viewModel.setTeams(listItems)
        }

    }
/*
    private fun setupButtons() {
        SingleElimBTN.setOnClickListener{
            SingleElimBTN.setBackgroundColor(Color.BLUE)
            DoubleElimBTN.setBackgroundColor(android.R.drawable.btn_default)
            viewModel.setTournamentType(TournamentType.SINGLEELIMINATION)
        }

        DoubleElimBTN.setOnClickListener{
            DoubleElimBTN.setBackgroundColor(Color.BLUE)
            SingleElimBTN?.setBackgroundColor(android.R.drawable.btn_default)
            viewModel.setTournamentType(TournamentType.DOUBLEELIMINATION)
        }

        AddTeamBTN.setOnClickListener{
            if (viewModel.isTournamentFull()) {
                val team = Team(
                    TeamNameInput.text.toString(),
                    Player(Player1NameInput.text.toString()),
                    Player(Player2NameInput.text.toString())
                )  //check the value of both inputs maybe!

                viewModel.addTeam(team)
                val teamAdded = getString(R.string.team_added, TeamNameInput.text.toString())
                Toast.makeText(this, teamAdded, Toast.LENGTH_LONG).show()
                TeamNameInput.text.clear()
                Player1NameInput.text.clear()
                Player2NameInput.text.clear()
            }
        }

    */

    private fun setupButtons() {
        StartBTN.setOnClickListener {
            //viewModel.startTournament()
            val intent = Intent()
            intent.setClass(this, TournamentActivity::class.java)
            intent.putExtra("Teams", viewModel.getTeams() as? ArrayList<Team>)
            startActivity(intent)
        }

        SeeTeamsListBTN.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, TeamsListViewActivity::class.java)
            intent.putExtra("Teams", viewModel.getTeams() as? ArrayList<Team>)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}
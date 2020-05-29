package com.example.tournamentandroid.kotlin.activities

import Presenters.SetupTournamentPresenter
import androidx.lifecycle.ViewModel
import com.example.tournamentandroid.Presenters.TournamentPresenter
import enums.TournamentType
import models.Team

class TournamentViewModel(type: TournamentType, teams: ArrayList<Team>) : ViewModel() {
    val _presenter: TournamentPresenter = TournamentPresenter(type, teams) //This constructor might not be proper




}
package models

import com.example.tournamentandroid.R
import java.io.Serializable


class Team (val teamName: String, val player1: Player = Player(""), val player2: Player = Player("")) : Serializable {

    override fun toString(): String =  "t" + teamName + "p1" + player1.name + "p2" + player2.name
    fun players() : String = player1.name + R.string.and + player2.name
}
package org.jetbrains.kotlin.activities

import Adapters.TeamListAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tournamentandroid.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.team_list_view.*
import models.Player
import models.Team


class TeamsListViewActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var _adapter: TeamListAdapter
    private lateinit var _viewModel: SetupTournamentViewModel
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "tournament_app"
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.team_list_view)
        listView = findViewById(R.id.teams_list)
        this._viewModel = SetupTournamentViewModel()

        val listItems: ArrayList<Team>? = intent.extras?.get("Teams") as? ArrayList<Team>

        if (listItems != null) {
            this._viewModel.setTeams(listItems)
        }

        _adapter = TeamListAdapter(this, listItems as MutableList<Team>)
        listView.adapter = _adapter
        registerForContextMenu(listView)




        listfab.setOnClickListener {
            showAddItemDialog(this)
        }

    }

    private fun showAddItemDialog(c: Context) {
        val taskEditText = EditText(c)
        val dialog: AlertDialog = AlertDialog.Builder(c)
            .setTitle(R.string.add_team)
            .setView(taskEditText)
            .setPositiveButton(R.string.add,
                DialogInterface.OnClickListener { dialog, which ->
                    val task = taskEditText.text.toString()
                    addTeam(task, "pl1", "pl2")
                })
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
    }

    private fun addTeam(name: String, player1: String, player2: String) {
        if (_viewModel.isTournamentFull()) {
            val team = Team(
                name,
                Player(player1),
                Player(player2)
            )  //check the value of both inputs maybe!

            _viewModel.addTeam(team)
            val teamAdded = getString(R.string.team_added, name)
            Toast.makeText(this, teamAdded, Toast.LENGTH_LONG).show()

            //_adapter.addTeam(team)
            _adapter.notifyDataSetChanged()
            return
        }

        Toast.makeText(this, R.string.tournament_full, Toast.LENGTH_LONG).show()



    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.setClass(this, SetupTournamentActivity::class.java)
        intent.putExtra("Teams", _viewModel.getTeams() as? ArrayList<Team>)
        startActivity(intent)
        //super.onBackPressed()
    }

    @SuppressLint("ResourceType")
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.layout.tournament_menu, menu)

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.delete_menu_item) {
                val info = item.menuInfo as AdapterContextMenuInfo
                _adapter.deleteItem(info.position)
                _adapter.notifyDataSetChanged()
                return true
            }
        }
        return false
    }



}

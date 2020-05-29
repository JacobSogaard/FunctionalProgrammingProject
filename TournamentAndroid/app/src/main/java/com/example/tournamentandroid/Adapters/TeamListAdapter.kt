package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.tournamentandroid.R
import models.Team


class TeamListAdapter(private val context: Context,
                    val dataSource: MutableList<Team>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_added_teams_item, parent, false) //this should be fixed
        // Get title element
        val teamNameTextView = rowView.findViewById(R.id.teamName) as TextView
        val teamMembersTextView = rowView.findViewById(R.id.teamMembers) as TextView

        val team = getItem(position)

        teamNameTextView.text = team.teamName
        teamMembersTextView.text = context.getString(R.string.team_members_list_item, team.player1, team.player2)

        return rowView
    }

    public fun addTeam(team: Team){
        dataSource.add(team)
    }


    public fun deleteItem(item: Int) {
        dataSource.remove(dataSource[item])
    }

    override fun getItem(position: Int): Team {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

}
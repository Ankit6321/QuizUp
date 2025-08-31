package com.example.quizaro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 🔹 Adapter takes a list of participants (strings for now)
class ParticipantsAdapter(private val participants: List<String>) :
    RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    // ViewHolder = single row in the RecyclerView
    class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.participantName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.nameText.text = participants[position]
    }

    override fun getItemCount(): Int = participants.size
}

package com.example.testtanaw.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

// Adapter for the institutions list
class InstitutionAdapter2(private val institutions: List<Institution>) :
    RecyclerView.Adapter<InstitutionAdapter2.InstitutionViewHolder>() {

    // Inflates the item layout and returns the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_institution, parent, false)
        return InstitutionViewHolder(view)
    }

    // Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: InstitutionViewHolder, position: Int) {
        val institution = institutions[position]
        holder.bind(institution)
    }

    // Returns the size of the list
    override fun getItemCount(): Int {
        return institutions.size
    }

    // Inner ViewHolder to hold the item view
    inner class InstitutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val institutionLogo: ImageView = itemView.findViewById(R.id.institutionLogo)
        private val institutionName: TextView = itemView.findViewById(R.id.institutionName)

        fun bind(institution: Institution) {
            institutionLogo.setImageResource(institution.logoResId)
            institutionName.text = institution.institution
        }
    }
}

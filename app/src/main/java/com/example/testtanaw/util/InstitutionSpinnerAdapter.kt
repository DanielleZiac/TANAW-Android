package com.example.testtanaw.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.testtanaw.R

class InstitutionAdapter(context: Context, private val institutions: List<String>) :
    ArrayAdapter<String>(context, 0, institutions) {

    // This is used to customize the view of the spinner item when it is selected
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_institution, parent, false
        )
        val icon = view.findViewById<ImageView>(R.id.spinner_icon)
        val text = view.findViewById<TextView>(R.id.spinner_text)

        // Set the icon and text for the spinner item
        icon.setImageResource(R.drawable.baseline_account_balance_24) // Use your vector drawable here
        text.text = institutions[position]

        return view
    }

    // This is used to customize the drop-down view of the spinner
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_institution, parent, false
        )
        val icon = view.findViewById<ImageView>(R.id.spinner_icon)
        val text = view.findViewById<TextView>(R.id.spinner_text)

        // Set the icon and text for the drop-down view
        icon.setImageResource(R.drawable.baseline_account_balance_24) // Use your vector drawable here
        text.text = institutions[position]

        return view
    }
}

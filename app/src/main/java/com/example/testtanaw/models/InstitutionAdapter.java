package com.example.testtanaw.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testtanaw.R;

import java.util.List;

public class InstitutionAdapter extends RecyclerView.Adapter<InstitutionAdapter.InstitutionViewHolder> {

    private List<Institution> institutions;

    // Constructor to initialize the institution list
    public InstitutionAdapter(List<Institution> institutions) {
        this.institutions = institutions;
    }

    // Inflates the item layout and returns the ViewHolder
    @Override
    public InstitutionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institution, parent, false);
        return new InstitutionViewHolder(view);
    }

    // Binds the data to the ViewHolder
    @Override
    public void onBindViewHolder(InstitutionViewHolder holder, int position) {
        Institution institution = institutions.get(position);
        holder.bind(institution); // Pass the data to the view holder
    }

    // Returns the size of the list
    @Override
    public int getItemCount() {
        return institutions.size();
    }

    // Inner ViewHolder to hold the item view
    public class InstitutionViewHolder extends RecyclerView.ViewHolder {
        private ImageView institutionLogo;
        private TextView institutionName;  // TextView for the institution name
        private TextView campusName;      // TextView for the campus name

        public InstitutionViewHolder(View itemView) {
            super(itemView);
            institutionLogo = itemView.findViewById(R.id.institutionLogo);
            institutionName = itemView.findViewById(R.id.institutionName);  // Initialize institution name
            campusName = itemView.findViewById(R.id.campusName);            // Initialize campus name
        }

        // Binds the data to the views in the ViewHolder
        public void bind(Institution institution) {
            institutionLogo.setImageResource(institution.getLogoResId());
            institutionName.setText(institution.getInstitution());  // Set the institution name
            campusName.setText(institution.getCampus());            // Set the campus name
        }
    }
}

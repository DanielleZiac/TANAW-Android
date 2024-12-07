package com.example.testtanaw.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import java.util.List;

// Adapter for the institutions list
public class InstitutionAdapter2 extends RecyclerView.Adapter<InstitutionAdapter2.InstitutionViewHolder> {
    private final List<Institution> institutions;

    public InstitutionAdapter2(List<Institution> institutions) {
        this.institutions = institutions;
    }

    // Inflates the item layout and returns the ViewHolder
    @NonNull
    @Override
    public InstitutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_institution, parent, false);
        return new InstitutionViewHolder(view);
    }

    // Binds the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull InstitutionViewHolder holder, int position) {
        Institution institution = institutions.get(position);
        holder.bind(institution);
    }

    // Returns the size of the list
    @Override
    public int getItemCount() {
        return institutions.size();
    }

    // ViewHolder to hold the item view
    public class InstitutionViewHolder extends RecyclerView.ViewHolder {
        private final ImageView institutionLogo;
        private final TextView institutionName;

        public InstitutionViewHolder(View itemView) {
            super(itemView);
            institutionLogo = itemView.findViewById(R.id.institutionLogo);
            institutionName = itemView.findViewById(R.id.institutionName);
        }

        public void bind(Institution institution) {
            institutionLogo.setImageResource(institution.getLogoResId());
            institutionName.setText(institution.getInstitution());
        }
    }
}

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

public class InstitutionAdapter2 extends RecyclerView.Adapter<InstitutionAdapter2.InstitutionViewHolder> {
    private final List<Institution> institutions;

    public InstitutionAdapter2(List<Institution> institutions) {
        this.institutions = institutions;
    }

    @NonNull
    @Override
    public InstitutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_institution, parent, false);
        return new InstitutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstitutionViewHolder holder, int position) {
        Institution institution = institutions.get(position);
        holder.institutionName.setText(institution.getInstitution());
        holder.institutionDescription.setText(institution.getCampus());
        holder.institutionLogo.setImageResource(institution.getLogoResId());
    }

    @Override
    public int getItemCount() {
        return institutions.size();
    }

    static class InstitutionViewHolder extends RecyclerView.ViewHolder {
        ImageView institutionLogo;
        TextView institutionName;
        TextView institutionDescription;

        InstitutionViewHolder(View itemView) {
            super(itemView);
            institutionLogo = itemView.findViewById(R.id.institutionLogo);
            institutionName = itemView.findViewById(R.id.institutionName);
            institutionDescription = itemView.findViewById(R.id.institutionDescription);
        }
    }
}

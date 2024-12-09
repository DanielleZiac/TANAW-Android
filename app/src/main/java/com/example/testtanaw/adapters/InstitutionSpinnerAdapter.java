package com.example.testtanaw.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testtanaw.R;
import com.example.testtanaw.models.Institution;

import java.util.List;

public class InstitutionSpinnerAdapter extends ArrayAdapter<Institution> {
    private final Context context;
    private final List<Institution> institutions;

    public InstitutionSpinnerAdapter(Context context, List<Institution> institutions) {
        super(context, R.layout.spinner_institution, institutions);
        this.institutions = institutions;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("InstitutionSpinnerAdapter", "getView");
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.spinner_institution, parent, false
            );
        }

        ImageView icon = view.findViewById(R.id.spinner_icon);
        TextView text = view.findViewById(R.id.spinner_text);

        Institution institution = institutions.get(position);
        // TODO: Uncomment once logo for institution is resolved
        // icon.setImageResource(institution.getLogoResId());
        text.setText(institution.getInstitution());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.spinner_institution, parent, false
            );
        }

        ImageView icon = view.findViewById(R.id.spinner_icon);
        TextView text = view.findViewById(R.id.spinner_text);

        icon.setImageResource(R.drawable.baseline_account_balance_24);
        text.setText(institutions.get(position).getInstitution());

        return view;
    }
}

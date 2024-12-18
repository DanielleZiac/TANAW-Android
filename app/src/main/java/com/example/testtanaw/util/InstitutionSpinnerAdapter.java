package com.example.testtanaw.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.testtanaw.R;
import java.util.List;

public class InstitutionSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> institutions;

    public InstitutionSpinnerAdapter(Context context, List<String> institutions) {
        super(context, 0, institutions);
        this.context = context;
        this.institutions = institutions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                R.layout.spinner_institution, parent, false
            );
        }
        
        ImageView icon = view.findViewById(R.id.spinner_icon);
        TextView text = view.findViewById(R.id.spinner_text);

        icon.setImageResource(R.drawable.baseline_account_balance_24);
        text.setText(institutions.get(position));

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
        text.setText(institutions.get(position));

        return view;
    }
}

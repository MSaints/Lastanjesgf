package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afrofx.code.anjesgf.models.FornecedorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/22/2018.
 */

public class FornecedorAdapter extends ArrayAdapter<FornecedorModel> {

    Context context;
    int resource, textViewResourceId;
    List<FornecedorModel> items, tempItems, suggestions;


    public FornecedorAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<FornecedorModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = objects;
        tempItems = new ArrayList<FornecedorModel>(items);
        suggestions = new ArrayList<FornecedorModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        FornecedorModel cmodel = items.get(position);
        if (cmodel != null) {
            TextView lblName = (TextView) view.findViewById(android.R.id.text1);
            if (lblName != null)
                lblName.setText(cmodel.getFornecedor_nome());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((FornecedorModel) resultValue).getFornecedor_nome();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (FornecedorModel cmodel : tempItems) {
                    if (cmodel.getFornecedor_nome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(cmodel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<FornecedorModel> filterList = (ArrayList<FornecedorModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (FornecedorModel cmodel : filterList) {
                    add(cmodel);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

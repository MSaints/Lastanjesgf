package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afrofx.code.anjesgf.models.UnidadeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class UnidadeAdapter extends ArrayAdapter<UnidadeModel> {
    Context context;
    int resource, textViewResourceId;
    List<UnidadeModel> itens, tempItems, suggestions;

    public UnidadeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<UnidadeModel> itens) {
        super(context, resource, textViewResourceId, itens);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.itens = itens;
        tempItems = new ArrayList<UnidadeModel>(itens); // this makes the difference.
        suggestions = new ArrayList<UnidadeModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        UnidadeModel uModel = itens.get(position);
        if (uModel != null) {
            TextView lblName = (TextView) view.findViewById(android.R.id.text1);
            if (lblName != null)
                lblName.setText(uModel.getUnidadee_nome());
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
            String str = ((UnidadeModel) resultValue).getUnidadee_nome();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (UnidadeModel uModel : tempItems) {
                    if (uModel.getUnidadee_nome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(uModel);
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
            List<UnidadeModel> filterList = (ArrayList<UnidadeModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (UnidadeModel uModel : filterList) {
                    add(uModel);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

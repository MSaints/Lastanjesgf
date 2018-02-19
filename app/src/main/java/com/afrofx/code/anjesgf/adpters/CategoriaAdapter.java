package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.CategoriaModel;


/**
 * Created by Afro FX on 2/12/2018.
 */

public class CategoriaAdapter extends ArrayAdapter<CategoriaModel> {

    Context context;
    int resource, textViewResourceId;
    List<CategoriaModel> items, tempItems, suggestions;

    public CategoriaAdapter(@NonNull Context context, int resource, int textViewResourceId, List<CategoriaModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<CategoriaModel>(items); // this makes the difference.
        suggestions = new ArrayList<CategoriaModel>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_categoria, parent, false);
        }
        CategoriaModel cmodel = items.get(position);
        if (cmodel != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(cmodel.getCategoria());
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
            String str = ((CategoriaModel) resultValue).getCategoria();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CategoriaModel cmodel : tempItems) {
                    if (cmodel.getCategoria().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<CategoriaModel> filterList = (ArrayList<CategoriaModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CategoriaModel cmodel : filterList) {
                    add(cmodel);
                    notifyDataSetChanged();
                }
            }
        }
    };


}

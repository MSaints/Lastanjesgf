package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/22/2018.
 */

public class ProdutoAdapter extends ArrayAdapter<StockModel> {

    Context context;
    int resource, textViewResourceId;
    List<StockModel> items, tempItems, suggestions;



    public ProdutoAdapter(@NonNull Context context, int resource, int textViewResourceId, List<StockModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<StockModel>(items); // this makes the difference.
        suggestions = new ArrayList<StockModel>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_categoria, parent, false);
        }
        StockModel cmodel = items.get(position);
        if (cmodel != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(cmodel.getProduto_nome());
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
            String str = ((StockModel) resultValue).getProduto_nome();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (StockModel cmodel : tempItems) {
                    if (cmodel.getProduto_nome().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<StockModel> filterList = (ArrayList<StockModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (StockModel cmodel : filterList) {
                    add(cmodel);
                    notifyDataSetChanged();
                }
            }
        }
    };


}

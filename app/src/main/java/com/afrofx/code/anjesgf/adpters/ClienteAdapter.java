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
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/22/2018.
 */

public class ClienteAdapter extends ArrayAdapter<ClienteModel> {

    Context context;
    int resource, textViewResourceId;
    List<ClienteModel> items, tempItems, suggestions;


    public ClienteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ClienteModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = objects;
        tempItems = new ArrayList<ClienteModel>(items);
        suggestions = new ArrayList<ClienteModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_categoria, parent, false);
        }
        ClienteModel cmodel = items.get(position);
        if (cmodel != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(cmodel.getNomeCliente());
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
            String str = ((ClienteModel) resultValue).getNomeCliente();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ClienteModel cmodel : tempItems) {
                    if (cmodel.getNomeCliente().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<ClienteModel> filterList = (ArrayList<ClienteModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ClienteModel cmodel : filterList) {
                    add(cmodel);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

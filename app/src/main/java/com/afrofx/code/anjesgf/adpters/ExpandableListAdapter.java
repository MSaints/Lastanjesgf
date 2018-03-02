package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Afro FX on 2/26/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listaCabecalho;
    private HashMap<String, List<String>> listaFilho;


    public ExpandableListAdapter(Context context, List<String> listaCabecalho, HashMap<String, List<String>> listaFilho) {
        this.context = context;
        this.listaCabecalho = listaCabecalho;
        this.listaFilho = listaFilho;
    }


    @Override
    public int getGroupCount() {
        return listaCabecalho.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listaFilho.get(this.listaCabecalho.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listaCabecalho.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listaFilho.get(this.listaCabecalho.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

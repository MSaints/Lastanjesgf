package com.afrofx.code.anjesgf.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.FornecedorAdapter;
import com.afrofx.code.anjesgf.models.FornecedorModel;

import java.util.List;

public class FornecedorFragment extends Fragment {


    View v;

    private List<FornecedorModel> fornecedorList;

    DatabaseHelper db;



    public FornecedorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_fornecedor, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewFornecedor);
        recyclerView.setHasFixedSize(true);
        FornecedorAdapter fornecedorAdapter = new FornecedorAdapter(getContext(), fornecedorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(fornecedorAdapter);

        return v;
    }


}

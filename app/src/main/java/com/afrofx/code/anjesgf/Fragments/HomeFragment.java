package com.afrofx.code.anjesgf.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afrofx.code.anjesgf.Activities.CaixaActivity;
import com.afrofx.code.anjesgf.Activities.RelatorioActivity;
import com.afrofx.code.anjesgf.Activities.ScrollingActivity;
import com.afrofx.code.anjesgf.Activities.StockActivity;
import com.afrofx.code.anjesgf.Activities.VendasActivity;
import com.afrofx.code.anjesgf.R;

public class HomeFragment extends Fragment {

    private View v;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_home, container, false);

        final CardView cardView_Stock = (CardView) v.findViewById(R.id.card_entrar_stock);
        cardView_Stock.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StockActivity.class));
            }
        });

        final CardView cardView_Caixa = (CardView) v.findViewById(R.id.card_entrar_caixa);
        cardView_Caixa.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CaixaActivity.class));
            }
        });

        final CardView cardView_relatorio = (CardView) v.findViewById(R.id.card_entrar_despesas);
        cardView_relatorio.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScrollingActivity.class));
            }
        });

        final CardView cardView_Vendas = (CardView) v.findViewById(R.id.card_entrar_vendas);
        cardView_Vendas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VendasActivity.class));
            }
        });

        return v;

    }
}

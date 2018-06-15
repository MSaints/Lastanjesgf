package com.afrofx.code.anjesgf.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afrofx.code.anjesgf.Activities.home.DespesasActivity;
import com.afrofx.code.anjesgf.Activities.home.MercadoriaActivity;
import com.afrofx.code.anjesgf.Activities.home.MinhaBancaActivity;
import com.afrofx.code.anjesgf.Activities.home.MolaActivity;
import com.afrofx.code.anjesgf.Activities.home.RendimentosActivity;
import com.afrofx.code.anjesgf.Activities.home.VendasActivity;
import com.afrofx.code.anjesgf.Dialogs.DialogInfo;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;

public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        View homeLine1 = v.findViewById(R.id.homeLine1);
        homeLine1.setBackgroundColor(Constant.color);
        View homeLine2 = v.findViewById(R.id.homeLine2);
        homeLine2.setBackgroundColor(Constant.color);
        View homeLine3 = v.findViewById(R.id.homeLine3);
        homeLine3.setBackgroundColor(Constant.color);
        View homeLine4 = v.findViewById(R.id.homeLine4);
        homeLine4.setBackgroundColor(Constant.color);
        View homeLine5 = v.findViewById(R.id.homeLine5);
        homeLine5.setBackgroundColor(Constant.color);
        View homeLine6 = v.findViewById(R.id.homeLine6);
        homeLine6.setBackgroundColor(Constant.color);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean value = (mSharedPreference.getBoolean("icon_view", true));


        final CardView cardView_Stock = v.findViewById(R.id.card_entrar_stock);
        cardView_Stock.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MercadoriaActivity.class));
            }
        });

        final CardView cardView_Caixa = v.findViewById(R.id.card_entrar_caixa);
        cardView_Caixa.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MolaActivity.class));
            }
        });

        final CardView cardView_Despesas = v.findViewById(R.id.card_entrar_despesas);
        cardView_Despesas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DespesasActivity.class));
            }
        });

        final CardView cardView_Vendas = v.findViewById(R.id.card_entrar_vendas);
        cardView_Vendas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VendasActivity.class));
            }
        });
        final CardView cardView_Redimentos = v.findViewById(R.id.card_entrar_rendimentos);
        cardView_Redimentos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RendimentosActivity.class));
            }
        });
        final CardView cardView_Banca = v.findViewById(R.id.card_entrar_banca);
        cardView_Banca.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MinhaBancaActivity.class));
            }
        });

        final ImageView infoRendimentos = v.findViewById(R.id.infoCardRendimentos);
        infoRendimentos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Rendimentos", getResources().getString(R.string.info_rendimento));
                dialogInfo.show();
            }
        });

        final ImageView infoMola = v.findViewById(R.id.infoCardMola);
        infoMola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Mola", getResources().getString(R.string.info_mola));
                dialogInfo.show();
            }
        });

        final ImageView infoDespesas = v.findViewById(R.id.infoCardDespesas);
        infoDespesas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Despesas", getResources().getString(R.string.info_despesa));
                dialogInfo.show();
            }
        });

        final ImageView infoVenda = v.findViewById(R.id.infoCardVendas);
        infoVenda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Vendas", getResources().getString(R.string.info_venda));
                dialogInfo.show();
            }
        });

        final ImageView infoBanca = v.findViewById(R.id.infoCardBanca);
        infoBanca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Minha Banca", getResources().getString(R.string.info_banca));
                dialogInfo.show();
            }
        });

        final ImageView infoMercadoria = v.findViewById(R.id.infoCardMercadoria);
        infoMercadoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(getContext(), "Mercadoria", getResources().getString(R.string.info_mercadoria));
                dialogInfo.show();
            }
        });

        if(!value){
            infoRendimentos.setVisibility(View.GONE);
            infoMola.setVisibility(View.GONE);
            infoDespesas.setVisibility(View.GONE);
            infoVenda.setVisibility(View.GONE);
            infoBanca.setVisibility(View.GONE);
            infoMercadoria.setVisibility(View.GONE);
        }else {
            infoRendimentos.setVisibility(View.VISIBLE);
            infoMola.setVisibility(View.VISIBLE);
            infoDespesas.setVisibility(View.VISIBLE);
            infoVenda.setVisibility(View.VISIBLE);
            infoBanca.setVisibility(View.VISIBLE);
            infoMercadoria.setVisibility(View.VISIBLE);
        }

        return v;
    }
}

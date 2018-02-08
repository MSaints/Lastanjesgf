package com.afrofx.code.anjesgf.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;

/**
 * Created by Afro FX on 2/7/2018.
 */

public class CaixaActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView saldo;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_screen);

        db = new DatabaseHelper(this);

        saldo = (TextView)findViewById(R.id.txt_saldo_caixa);

         db.saldoCaixa();

       /* saldo.setText(""+s);*/
    }
}

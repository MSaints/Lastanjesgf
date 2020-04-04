package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.afrofx.code.anjesgf.R;
import com.tsongkha.spinnerdatepicker.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DialogPagaStock extends AlertDialog {

    private String text1, text2;
    public Context context;
    Locale localeBR = new Locale("pt", "BR");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", localeBR);
    private TextView data1, data2;

    public DialogPagaStock(Context context) {
        super(context);
        this.text1 = text1;
        this.text2 = text2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_data);

        CardView dataInicio = findViewById(R.id.dataInicio);
        CardView dataFim = findViewById(R.id.dataFim);

        data1 = findViewById(R.id.data1);
        data2 = findViewById(R.id.data2);

        dataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void onDateSet1(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        data1.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void onDateSet2(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        data2.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {

    }
}

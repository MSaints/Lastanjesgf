package com.afrofx.code.anjesgf.Dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;

public class DialogAjuda extends AlertDialog {

    Context context;

    public DialogAjuda(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.prompt_ajuda);

        TextView title = findViewById(R.id.definitionInfoTitle);

        title.setBackgroundColor(Constant.color);

        TextView txtOpcaoManual1 = findViewById(R.id.textOpcaoManual1);
        txtOpcaoManual1.setTextColor(Constant.color);

        TextView txtOpcaoManual2 = findViewById(R.id.textOpcaoManual2);
        txtOpcaoManual2.setTextColor(Constant.color);

        CardView OpcaoManual1 = findViewById(R.id.OpcaoManual1);
        CardView OpcaoManual2 = findViewById(R.id.OpcaoManual2);

        OpcaoManual1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        OpcaoManual2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String numero = "+258844145100";
                callIntent.setData(Uri.parse("tel:" + numero));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);
            }
        });

    }
}

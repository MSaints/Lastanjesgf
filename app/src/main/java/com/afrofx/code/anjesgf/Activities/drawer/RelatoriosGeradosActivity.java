package com.afrofx.code.anjesgf.Activities.drawer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.RecyclerRelatoriosGerados;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;

import java.io.File;
import java.util.ArrayList;

public class RelatoriosGeradosActivity extends AppCompatActivity {

    public RecyclerView listView;
    public static ArrayList<File> fileList = new ArrayList<File>();
    public RecyclerRelatoriosGerados objectAdapter;
    public static int Request_Permission = 1;
    public boolean permission;
    public File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_relatorios_gerados);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRelatoriosGerados);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Iniciar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(RelatoriosGeradosActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Iniciar(){
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        listView = (RecyclerView) findViewById(R.id.recyclerViewRelatoriosGerados);
        dir = new File(Environment.getExternalStorageDirectory().toString()+"/Econta_Relatorios");
        fn_permission();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplicationContext(), VisualizadorPDFActivity.class);
//                intent.putExtra("position", i);
//                startActivity(intent);
//
//                Log.e("Position", i + "");
//            }
//        });
    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")) {

                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getName().equals(listFile[i].getName())) {
                                booleanpdf = true;
                            } else {

                            }
                        }

                        if (booleanpdf) {
                            booleanpdf = false;
                        } else {
                            fileList.add(listFile[i]);

                        }
                    }
                }
            }
        }
        return fileList;
    }


    private void fn_permission() {
            permission = true;

            getfile(dir);

            objectAdapter = new RecyclerRelatoriosGerados(this, fileList);
            listView.setAdapter(objectAdapter);

            listView.setHasFixedSize(true);
            objectAdapter = new RecyclerRelatoriosGerados(this, fileList);
            listView.setLayoutManager(new LinearLayoutManager(this));

            listView.setAdapter(objectAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Permission) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permission = true;
                getfile(dir);

                objectAdapter = new RecyclerRelatoriosGerados(this, fileList);
                listView.setAdapter(objectAdapter);

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
}

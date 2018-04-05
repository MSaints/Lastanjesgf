package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Fragments.AddProdutosFragment;
import com.afrofx.code.anjesgf.Fragments.DespesasFragment;
import com.afrofx.code.anjesgf.Fragments.FornecedorFragment;
import com.afrofx.code.anjesgf.Fragments.ListaProdutosFragment;
import com.afrofx.code.anjesgf.Fragments.RedimentosFragment;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.DispesasModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

public class DespesasActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private TabLayout tab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topbar_despesas_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainDespesas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        tab1 = (TabLayout) findViewById(R.id.tabLayoutDespesas);
        viewPager = (ViewPager) findViewById(R.id.pageViewerDespesas);

        pageAdapters = new PageAdapters(getSupportFragmentManager());

        pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
        pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
        viewPager.setAdapter(pageAdapters);
        tab1.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(DespesasActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

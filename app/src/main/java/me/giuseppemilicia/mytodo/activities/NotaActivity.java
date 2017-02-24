package me.giuseppemilicia.mytodo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.giuseppemilicia.mytodo.R;


/**
 * Created by Giuseppe on 20/02/2017.
 */

public class NotaActivity extends AppCompatActivity {

    public static final String TITOLO_KEY = "titolo";
    public static final String CORPO_KEY = "corpo";
    public static final String COLORE_KEY = "colore";
    private TextView dataUltimaModificaTv;
    private EditText titoloEt, corpoEt;
    private Toolbar toolbar;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        dataUltimaModificaTv = (TextView) findViewById(R.id.data_ultima_modifica);
        titoloEt = (EditText) findViewById(R.id.titolo);
        corpoEt = (EditText) findViewById(R.id.corpo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_nota_rv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dataUltimaModificaTv.setText(getString(R.string.data_ultima_modifica) + " " + dateFormat.format(new Date()));

        Intent intent = getIntent();
        if (intent.getAction().equals(MainActivity.ACTION_MODIFY)) {
            titoloEt.setText(intent.getStringExtra(TITOLO_KEY));
            corpoEt.setText(intent.getStringExtra(CORPO_KEY));
            int colore = intent.getIntExtra(COLORE_KEY, Color.GRAY);
            relativeLayout.setBackgroundColor(colore);
            dataUltimaModificaTv.setBackgroundColor(colore);
        } else if (intent.getAction().equals(Intent.ACTION_SEND)) {
            corpoEt.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_nota, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_confirm) {
            Intent intent = new Intent();
            intent.putExtra(TITOLO_KEY, titoloEt.getText().toString());
            intent.putExtra(CORPO_KEY, corpoEt.getText().toString());
            intent.putExtra(MainActivity.INDICE_KEY, getIntent().getIntExtra(MainActivity.INDICE_KEY, 0));
            setResult(RESULT_OK, intent);
            finish();
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

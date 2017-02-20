package me.giuseppemilicia.mytodo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.giuseppemilicia.mytodo.R;
import me.giuseppemilicia.mytodo.models.Nota;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class NotaActivity extends Activity implements View.OnClickListener {

    public static final String TITOLO_KEY = "titolo";
    public static final String CORPO_KEY = "corpo";
    private TextView dataUltimaModificaTv;
    private EditText titoloEt, corpoEt;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        dataUltimaModificaTv = (TextView) findViewById(R.id.data_ultima_modifica);
        titoloEt = (EditText) findViewById(R.id.titolo);
        corpoEt = (EditText) findViewById(R.id.corpo);
        saveBtn = (Button) findViewById(R.id.save_btn);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dataUltimaModificaTv.setText(getString(R.string.data_ultima_modifica) + " " + dateFormat.format(new Date()));

        Intent intent = getIntent();
        if (intent.getAction().equals(MainActivity.ACTION_MODIFY)) {
            titoloEt.setText(intent.getStringExtra(TITOLO_KEY));
            corpoEt.setText(intent.getStringExtra(CORPO_KEY));
        }

        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(TITOLO_KEY, titoloEt.getText().toString());
        intent.putExtra(CORPO_KEY, corpoEt.getText().toString());
        intent.putExtra(MainActivity.INDICE_KEY, getIntent().getIntExtra(MainActivity.INDICE_KEY, 0));
        setResult(RESULT_OK, intent);
        finish();
    }
}

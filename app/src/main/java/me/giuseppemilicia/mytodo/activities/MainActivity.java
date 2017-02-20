package me.giuseppemilicia.mytodo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

import me.giuseppemilicia.mytodo.R;
import me.giuseppemilicia.mytodo.models.Nota;
import me.giuseppemilicia.mytodo.views.adapters.NotaAdapter;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String ACTION_MODIFY = "modify";
    public static final String ACTION_ADD = "add";
    public static final String INDICE_KEY = "indice";
    public static final int ADD_REQUEST_CODE = 1;
    public static final int MODIFY_REQUEST_CODE = 2;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    protected NotaAdapter notaAdapter;
    private FloatingActionButton addFloatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        addFloatingBtn = (FloatingActionButton) findViewById(R.id.add_floating_btn);

        notaAdapter = new NotaAdapter();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(notaAdapter);
        notaAdapter.setDataSet(getDataSet());
        addFloatingBtn.setOnClickListener(this);
    }

    public ArrayList<Nota> getDataSet() {
        ArrayList<Nota> dataSet = new ArrayList<>();
        dataSet.add(new Nota("Tfasfest", "fadfsafsaf fafsafsafasfsa fasfasfsafsa"));
        dataSet.add(new Nota("Tfsafsaest", "fdasdsadsadaadfsafsaf fasfsafsafsafsafsafsafsafsa fasfasfsafsa"));
        dataSet.add(new Nota("Tefsafsast", "aasdsadsadas fafsafsafsafdasffasfsa fasfasfsafsa"));
        dataSet.add(new Nota("Tefsafsast", "fadfsafsaf fafsafsfsafsafsafsaafasfsa fasfasfsafsa"));
        dataSet.add(new Nota("Tessafsafsat", "fadfssdafsafsafsafassfafsafasfsaafsaf fafsafsafasfsa fasfasfsafsa"));
        return dataSet;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent (this, NotaActivity.class);
        intent.setAction(ACTION_ADD);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            Nota nota = new Nota(data.getStringExtra(NotaActivity.TITOLO_KEY), data.getStringExtra(NotaActivity.CORPO_KEY));
            notaAdapter.addNota(nota);
        } else if (requestCode == MODIFY_REQUEST_CODE && resultCode == RESULT_OK) {
            Nota nota = notaAdapter.getNotaByIndice(data.getIntExtra(INDICE_KEY, 0));
            nota.setTitolo(data.getStringExtra(NotaActivity.TITOLO_KEY));
            nota.setCorpo(data.getStringExtra(NotaActivity.CORPO_KEY));
            notaAdapter.editNota(nota);
        }
    }
}

package me.giuseppemilicia.mytodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

import java.util.ArrayList;
import java.util.List;

import me.giuseppemilicia.mytodo.R;
import me.giuseppemilicia.mytodo.db.SQLiteHandler;
import me.giuseppemilicia.mytodo.models.Nota;
import me.giuseppemilicia.mytodo.helper.MySelectedAdapter;
import me.giuseppemilicia.mytodo.views.adapters.NotaAdapter;
import me.giuseppemilicia.mytodo.helper.SimpleItemTouchHelperCallback;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActionMode.Callback, MySelectedAdapter.MyOnSelectedItem<NotaAdapter.NotaAdapterVH> {

    public static final String ACTION_MODIFY = "modify";
    public static final String ACTION_ADD = "add";
    public static final String INDICE_KEY = "indice";
    public static final int ADD_REQUEST_CODE = 1;
    public static final int MODIFY_REQUEST_CODE = 2;
    private Toolbar toolbar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    protected NotaAdapter notaAdapter;
    private FloatingActionButton addFloatingBtn;
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        addFloatingBtn = (FloatingActionButton) findViewById(R.id.add_floating_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notaAdapter = new NotaAdapter(this);
        notaAdapter.setMyOnSelectedItem(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(notaAdapter);
        addFloatingBtn.setOnClickListener(this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(notaAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        if (intent != null && intent.getAction().equals(Intent.ACTION_SEND)) {
            String corpo = intent.getStringExtra(Intent.EXTRA_TEXT);
            Intent addNote = new Intent(this, NotaActivity.class);
            addNote.setAction(Intent.ACTION_SEND);
            addNote.putExtra(Intent.EXTRA_TEXT, corpo);
            startActivityForResult(addNote, ADD_REQUEST_CODE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        deselectSelectedItem();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NotaActivity.class);
        intent.setAction(ACTION_ADD);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            String titolo = data.getStringExtra(NotaActivity.TITOLO_KEY);
            String corpo = data.getStringExtra(NotaActivity.CORPO_KEY);
            Nota nota = new Nota(titolo, corpo);
            notaAdapter.addNota(nota);
            recyclerView.smoothScrollToPosition(0);
        } else if (requestCode == MODIFY_REQUEST_CODE && resultCode == RESULT_OK) {
            int index = data.getIntExtra(INDICE_KEY, 0);
            Nota nota = notaAdapter.getNotaByIndex(index);
            nota.setTitolo(data.getStringExtra(NotaActivity.TITOLO_KEY));
            nota.setCorpo(data.getStringExtra(NotaActivity.CORPO_KEY));
            notaAdapter.editNota(nota);
        }
    }

    public void startActionMode() {
        if (mActionMode != null) {
            return;
        }
        mActionMode = startSupportActionMode(this);
    }

    public void stopActionMode() {
        if (mActionMode == null) {
            return;
        }
        mActionMode.finish();
        mActionMode = null;
        deselectSelectedItem();
    }

    public void updateActionMode() {
        mActionMode.setTitle(notaAdapter.getNumberOfSelectedItems() + " " + getString(R.string.select_items));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_multiple_select_nota, menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.material_grey_500));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remove) {
            deleteSelectedItem();
            deselectSelectedItem();
            return true;
        } else if (id == R.id.action_color) {
            showColorPalete();
            return true;
        } else if (id == R.id.action_special) {
            specialSelectedItem();
            deselectSelectedItem();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        deselectSelectedItem();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }


    public void showColorPalete() {
        int[] colors = getResources().getIntArray(R.array.colors);
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

        boolean sameColor = true;
        List<Integer> tempSelection = notaAdapter.getListOfSelectedItems();
        for (int i = 0; i < tempSelection.size() - 1; i++) {
            Nota first = notaAdapter.getNotaByIndex(tempSelection.get(i));
            Nota second = notaAdapter.getNotaByIndex(tempSelection.get(i + 1));
            if (first.getColore() != second.getColore()) {
                sameColor = false;
                break;
            }
        }

        int selectedColor = 0;
        if (sameColor) {
            selectedColor = notaAdapter.getNotaByIndex(tempSelection.get(0)).getColore();
        }

        colorPickerDialog.initialize(
                R.string.color_picker_default_title, colors, selectedColor, 4, colors.length);

        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                changeColorSelectedItem(color);
                stopActionMode();
            }
        });
        colorPickerDialog.show(getFragmentManager(), "ColorPicker");
    }

    public void changeColorSelectedItem(int newColor) {
        for (int index : notaAdapter.getListOfSelectedItems()) {
            Nota nota = notaAdapter.getNotaByIndex(index);
            nota.setColore(newColor);
            notaAdapter.editNota(nota);
        }
    }

    public void deselectSelectedItem() {
        for (int i : notaAdapter.getListOfSelectedItems()) {
            if (recyclerView.getChildAt(i) != null) {
                recyclerView.getChildAt(i).setSelected(false);
            }
        }
        notaAdapter.clearSelections();
    }

    public void deleteSelectedItem() {
        final List<Nota> tempNote = new ArrayList<>();
        for (int index : notaAdapter.getListOfSelectedItems()) {
            Nota nota = notaAdapter.getNotaByIndex(index);
            tempNote.add(nota);
        }

        final List<Integer> tempPosition = new ArrayList<>(tempNote.size());
        for (Nota nota : tempNote) {
            tempPosition.add(notaAdapter.getIndexByNota(nota));
            notaAdapter.removeNota(nota);
        }

        Snackbar snackbar = Snackbar.make(recyclerView, notaAdapter.getNumberOfSelectedItems() + " " + getResources().getString(R.string.cancels_ok), Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.annulla), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = tempPosition.size() - 1; i >= 0; i--) {
                    notaAdapter.addNota(tempNote.get(i), tempPosition.get(i));
                }
            }
        });
        snackbar.show();
    }

    public void specialSelectedItem() {
        List<Nota> tempNote = new ArrayList<>();
        for (int index : notaAdapter.getListOfSelectedItems()) {
            Nota nota = notaAdapter.getNotaByIndex(index);
            tempNote.add(nota);
        }

        for (Nota nota : tempNote) {
            notaAdapter.markNote(nota);
        }
    }

    @Override
    public void onSelectItem(NotaAdapter.NotaAdapterVH viewHolder) {
        updateActionMode();
    }

    @Override
    public void onDeselectItem(NotaAdapter.NotaAdapterVH viewHolder) {
        updateActionMode();
    }

    @Override
    public void onStopSelectionMode() {
        stopActionMode();
    }

    @Override
    public void onStartSelectionMode() {
        startActionMode();
    }

}

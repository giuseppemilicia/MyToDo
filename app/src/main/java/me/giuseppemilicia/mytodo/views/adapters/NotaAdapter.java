package me.giuseppemilicia.mytodo.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.giuseppemilicia.mytodo.R;
import me.giuseppemilicia.mytodo.activities.MainActivity;
import me.giuseppemilicia.mytodo.activities.NotaActivity;
import me.giuseppemilicia.mytodo.db.SQLiteHandler;
import me.giuseppemilicia.mytodo.helper.ItemTouchHelperAdapter;
import me.giuseppemilicia.mytodo.helper.ItemTouchHelperViewHolder;
import me.giuseppemilicia.mytodo.helper.MySelectedAdapter;
import me.giuseppemilicia.mytodo.models.Nota;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class NotaAdapter extends MySelectedAdapter<NotaAdapter.NotaAdapterVH> implements ItemTouchHelperAdapter {

    private SQLiteHandler sqliteHandler;
    private Activity activity;
    private ArrayList<Nota> dataSet;

    public NotaAdapter(Activity activity) {
        this.activity = activity;
        this.sqliteHandler = new SQLiteHandler(activity);
        this.setDataSet(sqliteHandler.getAllNotes());
    }

    public SQLiteHandler getSQLiteHandler() {
        return sqliteHandler;
    }

    public Nota getNotaByIndex(int index) {
        return dataSet.get(index);
    }

    public int getIndexByNota(Nota nota) {
        return dataSet.indexOf(nota);
    }

    public boolean addNota(Nota nota, int index) {
        long result = sqliteHandler.addNote(nota, true);
        if (result > -1) {
            dataSet.add(index, nota);
            changeItemPosition();
            notifyItemInserted(index);
            return true;
        } else {
            return false;
        }
    }

    public boolean addNota(Nota nota) {
        int index = getFirstEmptySpace();
        long result = sqliteHandler.addNote(nota);
        if (result > -1) {
            nota.setId(result);
            dataSet.add(index, nota);
            changeItemPosition();
            notifyItemInserted(index);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNota(Nota nota) {
        int result = sqliteHandler.deleteNote(nota);
        if (result != 0) {
            notifyItemRemoved(dataSet.indexOf(nota));
            dataSet.remove(nota);
            return true;
        } else {
            return false;
        }
    }

    public boolean editNota(Nota nota) {
        int result = sqliteHandler.updateNote(nota);
        if (result != 0) {
            dataSet.set(dataSet.indexOf(nota), nota);
            notifyItemChanged(dataSet.indexOf(nota));
            return true;
        } else {
            return false;
        }
    }

    public boolean editNota(Nota nota, boolean update) {
        int result = sqliteHandler.updateNote(nota);
        if (result != 0) {
            dataSet.set(dataSet.indexOf(nota), nota);
            if (update) notifyItemChanged(dataSet.indexOf(nota));
            return true;
        } else {
            return false;
        }
    }

    private void setDataSet(ArrayList dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    private void changeItemPosition() {
        for (int i = 0; i < dataSet.size(); i++) {
            Nota nota = dataSet.get(i);
            nota.setPosizione(i);
            editNota(nota, false);
        }
    }

    public void markNote(Nota nota) {
        nota.setSpeciale(!nota.isSpeciale());
        if (editNota(nota)) {
            int index = getIndexByNota(nota);
            int newIndex = (nota.isSpeciale()) ? 0 : getFirstEmptySpace(nota);
            dataSet.remove(nota);
            dataSet.add(newIndex, nota);
            notifyItemMoved(index, newIndex);
            changeItemPosition();
        }
    }

    public int getFirstEmptySpace() {
        for (Nota nota : dataSet) {
            if (!nota.isSpeciale()) {
                return getIndexByNota(nota);
            }
        }
        return (dataSet.size() == 0) ? 0 : dataSet.size() - 1;
    }

    public int getFirstEmptySpace(Nota myNota) {
        for (int i = 0; i < dataSet.size(); i++) {
            Nota nota = dataSet.get(i);
            if (!nota.isSpeciale() && nota != myNota) {
                return i - 1;
            }
        }
        return (dataSet.size() == 0) ? 0 : dataSet.size() - 1;
    }

    @Override
    public NotaAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        /*StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) parent.getLayoutParams();
        layoutParams.setFullSpan(true);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(NotaAdapterVH holder, int position) {
        Nota nota = getNotaByIndex(position);
        holder.setSpecial(nota.isSpeciale());
        holder.titoloTv.setText(nota.getTitolo());
        holder.corpoTv.setText(nota.getCorpo());
        holder.relativeLayout.setBackgroundColor(nota.getColore());
        holder.itemView.setSelected(isItemSelected(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        clearSelections();
        Nota nota = getNotaByIndex(fromPosition);
        int nextPosition = getFirstEmptySpace();
        if (nota.isSpeciale()) {
            if (toPosition >= nextPosition) {
                return false;
            }
        } else {
            if (nextPosition > toPosition) {
                return false;
            }
        }
        dataSet.remove(nota);
        dataSet.add(toPosition, nota);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int index) {
    }

    public class NotaAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder {

        private RelativeLayout relativeLayout;
        private TextView titoloTv, corpoTv;
        private ImageView markSpecial;

        public NotaAdapterVH(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.nota_relative_layout);
            titoloTv = (TextView) itemView.findViewById(R.id.nota_titolo);
            corpoTv = (TextView) itemView.findViewById(R.id.nota_corpo);
            markSpecial = (ImageView) itemView.findViewById(R.id.nota_mark_special);
            relativeLayout.setOnClickListener(this);
            relativeLayout.setOnLongClickListener(this);
        }

        public void setSpecial(boolean special) {
            if (special) {
                markSpecial.setVisibility(View.VISIBLE);
            } else {
                markSpecial.setVisibility(View.INVISIBLE);
            }
        }

        public void toggleSelectionItem() {
            boolean result = toggleSelection(getAdapterPosition(), this);
            this.itemView.setSelected(result);
            notifyItemChanged(getAdapterPosition());
        }

        public void delete() {
            final int position = getAdapterPosition();
            final Nota temp = getNotaByIndex(position);
            removeNota(temp);
            Snackbar snackbar = Snackbar.make(itemView, activity.getResources().getString(R.string.cancel_ok), Snackbar.LENGTH_LONG);
            snackbar.setAction(activity.getResources().getString(R.string.annulla), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNota(temp, position);
                }
            });
            snackbar.show();
        }

        @Override
        public void onClick(View v) {
            if (!isSelectionMode()) {
                Intent intent = new Intent(v.getContext(), NotaActivity.class);
                intent.putExtra(MainActivity.INDICE_KEY, getAdapterPosition());
                intent.putExtra(NotaActivity.TITOLO_KEY, titoloTv.getText().toString());
                intent.putExtra(NotaActivity.CORPO_KEY, corpoTv.getText().toString());
                intent.putExtra(NotaActivity.COLORE_KEY, dataSet.get(getAdapterPosition()).getColore());
                intent.setAction(MainActivity.ACTION_MODIFY);
                activity.startActivityForResult(intent, MainActivity.MODIFY_REQUEST_CODE);
            } else {
                toggleSelectionItem();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            toggleSelectionItem();
            return true;
        }

        @Override
        public void onItemSelected() {
            itemView.setActivated(true);
        }

        @Override
        public void onItemClear() {
            changeItemPosition();
            itemView.setActivated(false);
        }

        @Override
        public void onItemSwiped() {
            delete();
        }
    }
}

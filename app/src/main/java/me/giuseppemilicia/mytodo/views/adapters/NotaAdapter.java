package me.giuseppemilicia.mytodo.views.adapters;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.giuseppemilicia.mytodo.R;
import me.giuseppemilicia.mytodo.activities.MainActivity;
import me.giuseppemilicia.mytodo.activities.NotaActivity;
import me.giuseppemilicia.mytodo.models.Nota;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaAdapterVH> {

    private ArrayList<Nota> dataSet = new ArrayList<>();

    public void addNota(Nota nota) {
        dataSet.add(nota);
        notifyItemInserted(dataSet.size() - 1);
    }

    public void addNota(int position, Nota nota) {
        dataSet.add(position, nota);
        notifyItemInserted(position);
    }

    public Nota getNotaByIndice(int indice) {
        return dataSet.get(indice);
    }

    public void removeNota(Nota nota) {
        notifyItemRemoved(dataSet.indexOf(nota));
        dataSet.remove(nota);
    }

    public void editNota(Nota nota) {
        dataSet.set(dataSet.indexOf(nota), nota);
        notifyItemChanged(dataSet.indexOf(nota));
    }

    public void setDataSet(ArrayList dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public NotaAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(NotaAdapterVH holder, int position) {
        Nota nota = dataSet.get(position);
        holder.titoloTv.setText(nota.getTitolo());
        holder.corpoTv.setText(nota.getCorpo());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class NotaAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout linearLayout;
        private TextView titoloTv, corpoTv;

        public NotaAdapterVH(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.nota_linear_layout);
            titoloTv = (TextView) itemView.findViewById(R.id.nota_titolo);
            corpoTv = (TextView) itemView.findViewById(R.id.nota_corpo);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    snackbar(v);
                    return true;
                }
            });
        }

        public void snackbar(View v) {
            final int position = getAdapterPosition();
            final Nota temp = getNotaByIndice(getAdapterPosition());
            removeNota(getNotaByIndice(getAdapterPosition()));
            Snackbar snackbar = Snackbar.make(v, v.getResources().getString(R.string.cancel_ok), Snackbar.LENGTH_LONG);
            snackbar.setAction(v.getResources().getString(R.string.annulla), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNota(position, temp);
                }
            });
            snackbar.show();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), NotaActivity.class);
            intent.putExtra(MainActivity.INDICE_KEY, getAdapterPosition());
            intent.putExtra(NotaActivity.TITOLO_KEY, titoloTv.getText().toString());
            intent.putExtra(NotaActivity.CORPO_KEY, corpoTv.getText().toString());
            intent.setAction(MainActivity.ACTION_MODIFY);
            ((MainActivity) v.getContext()).startActivityForResult(intent, MainActivity.MODIFY_REQUEST_CODE);

        }
    }
}

package me.giuseppemilicia.mytodo.helper;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.giuseppemilicia.mytodo.models.Nota;

/**
 * Created by Giuseppe on 22/02/2017.
 */

public abstract class MySelectedAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected SparseBooleanArray selectedItems = new SparseBooleanArray();
    private MyOnSelectedItem<VH> myOnSelectedItem;
    private boolean isSelectionModeActive = false;

    public interface MyOnSelectedItem<VH> {
        void onStopSelectionMode();

        void onStartSelectionMode();

        void onDeselectItem(VH viewHolder);

        void onSelectItem(VH viewHolder);
    }

    public boolean isSelectionMode() {
        return isSelectionModeActive;
    }

    public void setMyOnSelectedItem(MyOnSelectedItem<VH> myOnSelectedItem) {
        this.myOnSelectedItem = myOnSelectedItem;
    }

    public boolean isItemSelected(int position) {
        return selectedItems.get(position, false);
    }

    public boolean toggleSelection(int position, VH viewHolder) {
        boolean status = selectedItems.get(position, false);

        if (isSelectionModeActive == false && myOnSelectedItem != null) {
            isSelectionModeActive = true;
            myOnSelectedItem.onStartSelectionMode();
        }

        if (status) {
            selectedItems.delete(position);
            if (myOnSelectedItem != null) {
                myOnSelectedItem.onDeselectItem(viewHolder);
            }
        } else {
            selectedItems.put(position, true);
            if (myOnSelectedItem != null) {
                myOnSelectedItem.onSelectItem(viewHolder);
            }
        }

        if (getNumberOfSelectedItems() == 0) {
            clearSelections();
        }

        return !status;
    }

    public void clearSelections() {
        boolean oldIsSelectionMode = isSelectionMode();
        isSelectionModeActive = false;
        if (oldIsSelectionMode && myOnSelectedItem != null) {
            myOnSelectedItem.onStopSelectionMode();
        }
        selectedItems.clear();
    }

    public int getNumberOfSelectedItems() {
        return selectedItems.size();
    }

    public List<Integer> getListOfSelectedItems() {
        List<Integer> listSelectedItems = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            listSelectedItems.add(selectedItems.keyAt(i));
        }
        return listSelectedItems;
    }
}

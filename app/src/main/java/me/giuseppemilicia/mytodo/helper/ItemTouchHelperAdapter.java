package me.giuseppemilicia.mytodo.helper;

/**
 * Created by Giuseppe on 23/02/2017.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
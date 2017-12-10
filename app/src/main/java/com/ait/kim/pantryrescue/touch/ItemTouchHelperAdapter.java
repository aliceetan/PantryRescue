package com.ait.kim.pantryrescue.touch;

/**
 * Created by kimpham on 12/10/17.
 */

public interface ItemTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}


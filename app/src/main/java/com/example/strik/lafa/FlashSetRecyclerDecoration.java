package com.example.strik.lafa;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * Standard decoration for FlashSet recycler views
 */
public class FlashSetRecyclerDecoration extends ItemDecoration {
    /**
     * Horizontal spacing between FlashSet elements
     */
    final int MARGIN_HORIZONTAL = 10;
    /**
     * Vertical spacing between FlashSet elements
     */
    final int MARGIN_VERTICAL = 10;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = outRect.right = MARGIN_HORIZONTAL;
        outRect.top = outRect.bottom = MARGIN_VERTICAL;

    }
}

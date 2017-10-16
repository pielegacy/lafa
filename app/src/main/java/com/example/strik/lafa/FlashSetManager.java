package com.example.strik.lafa;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.Collection;

/**
 * A simple manager class for working with multiple flash cards4
 */
public class FlashSetManager {

    private FragmentActivity activity;

    private int id;

    private FlashSet set;

    private int index = 0;

    /**
     * Constructor for a new FlashSet manager, taking in all required fields
     * @param activity the activity the manager works with
     * @param id the view id which is being outputted to
     * @param set the flashset being managed
     */
    public FlashSetManager(FragmentActivity activity, int id, FlashSet set)
    {
        this.activity = activity;
        this.id = id;
        this.set = set;
    }

    /**
     * Push the cards to the view
     */
    public void pushCardsToList()
    {
        for (FlashCard card:
             this.set.getSetOrdered()) {
            LAFA.pushFlashCardToView(this.activity, this.id, card);
        }
    }

    // TODO: Use fragment manager



}

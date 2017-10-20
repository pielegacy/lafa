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

    /**
     * Push a specific card to a list.
     * @param index the index of the card to push
     */
    public void pushCardToList(int index)
    {
        if (index < 0 || index >= this.set.getSetOrdered().size())
            index = 0;
        LAFA.pushFlashCardToView(this.activity, this.id, this.set.getSetOrdered().get(index));
    }

    /**
     * Push a specific card to a list.
     * @param index the index of the card to push
     */
    public void replaceCardInList(int index)
    {
        if (index < 0 || index >= this.set.getSetOrdered().size())
            index = 0;
        LAFA.replaceFlashCardInView(this.activity, this.id, this.set.getSetOrdered().get(index));
    }

    // TODO: Use fragment manager



}

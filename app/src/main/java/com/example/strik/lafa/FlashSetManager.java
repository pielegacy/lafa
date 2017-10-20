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
    public void populateCardList()
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
    public void pushCard(int index)
    {
        if (index < 0 || index >= this.set.getSetOrdered().size())
           this.index = 0;
        LAFA.pushFlashCardToView(
                this.activity, this.id, this.set.getSetOrdered().get(this.index = index));
    }

    /**
     * Push a specific card to a list.
     * @param index the index of the card to push
     */
    public void replaceCard(int index)
    {
        if (this.index < 0 || this.index >= this.set.getSetOrdered().size())
            this.index = 0;
        LAFA.replaceFlashCardInView(
                this.activity, this.id, this.set.getSetOrdered().get(this.index = index));
    }

    /**
     * Shuffle the set and display the first card in the collection
     */
    public void shuffleCards()
    {
        this.set.shuffleSet();
        LAFA.replaceFlashCardInView(
                this.activity, this.id, this.set.getSetOrdered().get(this.index = 0));
    }

    /**
     * Push the next card in the set
     * @return whether or not the push was successful
     */
    public boolean nextCard()
    {
        boolean result = this.index < (this.set.getSetOrdered().size() - 1);
        if (result)
            LAFA.replaceFlashCardInView(
                    this.activity, this.id, this.set.getSetOrdered().get(++this.index));
        return result;
    }

    /**
     * Push the previous card in the set
     * @return whether or not the push was successful
     */
    public boolean previousCard()
    {
        boolean result = this.index > 0;
        if (result)
            LAFA.replaceFlashCardInView(
                    this.activity, this.id, this.set.getSetOrdered().get(--this.index));
        return result;
    }

    /**
     * Get the current index of the set
     *
     * @return the index in a readable format
     */
    public int getIndex()
    {
        return this.index + 1;
    }

    /**
     * Get the size of the set
     * @return
     */
    public int getSetSize()
    {
        return this.set.getSet().size();
    }

    /**
     * Get a readable representation of the current
     * card in the set the user is up to.
     *
     * @return "index of size" as a string
     */
    public String getSetLabel()
    {
        return getIndex() + " of " + getSetSize();
    }

    // TODO: Use fragment manager



}

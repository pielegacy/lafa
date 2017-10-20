package com.example.strik.lafa;


import java.util.ArrayList;
import java.util.Collection;

/**
 * A collection of FlashCards with associated meta data
 */
public class FlashSet {

    /**
     * The collection of flashcards you're working with
     */
    private Collection<FlashCard> set;

    /**
     * The order of the flashcards
     */
    private int[] order;

    /**
     * The name of the flashset
     */
    private String name;

    /**
     * The author of the set
     */
    private String author;

    /**
     * The URL for the set
     */
    private String url;

    /**
     * The constructor for all the properties.
     *
     * @param set the set of flashcards
     * @param name the name of the set
     * @param author the author
     * @param url the URL for reference
     * @param order the order of the cards
     */
    public FlashSet(Collection<FlashCard> set, String name, String author, String url, int[] order)
    {
        this.set = set;
        this.order = order;
        this.name = name;
        this.author = author;
        this.url = url;
    }

    /**
     * Construct a new FlashSet from a collection of flashcards
     * and a name. Infer the order, author and URL of the set.
     *
     * @param set the set of flashcards
     * @param name the name of the set
     */
    public FlashSet(Collection<FlashCard> set, String name)
    {
        this(set, name, "Me", "", new int[set.size()]);
        int[] orderValues = new int[set.size()];
        FlashCard[] cardArray =  new FlashCard[set.size()];
        cardArray = set.toArray(cardArray);
        for (int i = 0; i < cardArray.length; i++)
            orderValues[i] = cardArray[i].getId();
        this.order = orderValues;
    }

    /**
     * Get the set of cards in an unordered form
     * @return
     */
    public ArrayList<FlashCard> getSet() {
        return new ArrayList<>(set);
    }

    /**
     * Get the set of flashcards in the correct order
     *
     * @return
     */
    public ArrayList<FlashCard> getSetOrdered() {
        FlashCard[] cardArray = (FlashCard[])set.toArray();
        ArrayList<FlashCard> result = new ArrayList<>();
        for (int i = 0; i < cardArray.length; i++)
            result.add(cardArray[order[i]]);
        return result;
    }

    public int[] getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}

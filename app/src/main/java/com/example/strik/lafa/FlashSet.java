package com.example.strik.lafa;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.list;
import static java.util.Collections.shuffle;

/**
 * A collection of FlashCards with associated meta data
 */
public class FlashSet implements Parcelable {

    /**
     * The collection of flashcards you're working with
     */
    private Collection<FlashCard> set;

    /**
     * The order of the flashcards
     */
    private ArrayList<Integer> order;

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
     * @param set    the set of flashcards
     * @param name   the name of the set
     * @param author the author
     * @param url    the URL for reference
     * @param order  the order of the cards
     */
    public FlashSet(Collection<FlashCard> set, String name, String author, String url, ArrayList<Integer> order) {
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
     * @param set  the set of flashcards
     * @param name the name of the set
     */
    public FlashSet(Collection<FlashCard> set, String name) {
        this(set, name, "Me", "", new ArrayList<Integer>());
        int[] orderValues = new int[set.size()];
        FlashCard[] cardArray = getSetArray();
        for (int i = 0; i < cardArray.length; i++)
            this.order.add(cardArray[i].getId());
    }

    /**
     * Get the set of cards in an unordered form
     *
     * @return the pure set of FlashCards
     */
    public ArrayList<FlashCard> getSet() {
        return new ArrayList<>(set);
    }

    /**
     * Get the set of flashcards in the correct order
     *
     * @return the ordered set of FlashCards
     */
    public ArrayList<FlashCard> getSetOrdered() {
        FlashCard[] cardArray = getSetArray();
        ArrayList<FlashCard> result = new ArrayList<>();
        for (int i = 0; i < cardArray.length; i++)
            result.add(cardArray[order.get(i)]);
        return result;
    }

    /**
     * Shuffle the set of FlashCards, return it.
     *
     * @return the shuffled set
     */
    public ArrayList<FlashCard> shuffleSet() {
        FlashCard[] cardArray = getSetArray();
        List<FlashCard> cardList = Arrays.asList(cardArray);
        Collections.shuffle(cardList);
        for (int i = 0; i < cardList.size(); i++) {
            order.set(i, cardList.get(i).getId());
        }
        return this.getSetOrdered();
    }

    /**
     * Get the set as an array rather than a collection
     * @return
     */
    private FlashCard[] getSetArray()
    {
        FlashCard[] cardArray = new FlashCard[set.size()];
        cardArray = set.toArray(cardArray);
        return cardArray;
    }
    /**
     * Push a FlashCard to the set, updating the ID if necessary.
     *
     * @param flashCard the FlashCard we're adding
     */
    public void pushFlashCard(FlashCard flashCard)
    {
        if (flashCard.getId() == -1)
            flashCard.setId(set.size());
        set.add(flashCard);
        order.add(flashCard.getId());
    }

    public ArrayList<Integer> getOrder() {
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

    public void setUrl(String value) {
        url = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(new ArrayList<>(this.set));
        parcel.writeString(this.name);
        parcel.writeString(this.author);
        parcel.writeString(this.url);
        parcel.writeList(this.order);
    }

    public static final Parcelable.Creator<FlashSet> CREATOR = new Parcelable.Creator<FlashSet>() {
        @Override
        public FlashSet createFromParcel(Parcel parcel) {
            return new FlashSet(parcel);
        }

        @Override
        public FlashSet[] newArray(int i) {
            return new FlashSet[i];
        }
    };

    public FlashSet(Parcel parcel) {
        this.set = parcel.readArrayList(FlashCard.class.getClassLoader());
        this.name = parcel.readString();
        this.author = parcel.readString();
        this.url = parcel.readString();
        this.order = new ArrayList<>();
        this.order = parcel.readArrayList(this.order.getClass().getClassLoader());
    }

    /**
     * Remove a FlashCard from the set based on its Id.
     * @param id the ID of the FlashCard to remove.
     * @return the card removed, null if no card was found.
     */
    public FlashCard removeById(int id)
    {
        FlashCard removedCard = getById(id);
        if (removedCard != null)
            set.remove(removedCard);
        return removedCard;
    }

    /**
     * Get a FlashCard in the set based on its ID.
     * @param id the ID of the FlashCard to retrieve.
     * @return the card retrieved, null if not found.
     */
    public FlashCard getById(int id)
    {
        FlashCard[] cardArray = getSetArray();
        FlashCard flashCard = null;
        for (FlashCard aCardArray : cardArray) {
            if (aCardArray.getId() == id) {
                flashCard = aCardArray;
                break;
            }
        }
        return flashCard;
    }

}

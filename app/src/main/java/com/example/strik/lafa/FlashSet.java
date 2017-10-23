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
     * @param set    the set of flashcards
     * @param name   the name of the set
     * @param author the author
     * @param url    the URL for reference
     * @param order  the order of the cards
     */
    public FlashSet(Collection<FlashCard> set, String name, String author, String url, int[] order) {
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
        this(set, name, "Me", "", new int[set.size()]);
        int[] orderValues = new int[set.size()];
        FlashCard[] cardArray = new FlashCard[set.size()];
        cardArray = set.toArray(cardArray);
        for (int i = 0; i < cardArray.length; i++)
            orderValues[i] = cardArray[i].getId();
        this.order = orderValues;
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
        FlashCard[] cardArray = new FlashCard[set.size()];
        cardArray = set.toArray(cardArray);
        ArrayList<FlashCard> result = new ArrayList<>();
        for (int i = 0; i < cardArray.length; i++)
            result.add(cardArray[order[i]]);
        return result;
    }

    /**
     * Shuffle the set of FlashCards, return it.
     *
     * @return the shuffled set
     */
    public ArrayList<FlashCard> shuffleSet() {
        FlashCard[] cardArray = new FlashCard[set.size()];
        cardArray = set.toArray(cardArray);
        List<FlashCard> cardList = Arrays.asList(cardArray);
        Collections.shuffle(cardList);
        for (int i = 0; i < cardList.size(); i++) {
            order[i] = cardList.get(i).getId();
        }
        return this.getSetOrdered();
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
        parcel.writeIntArray(this.order);
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
        this.order = new int[this.set.size()];
        parcel.readIntArray(this.order);
    }

}

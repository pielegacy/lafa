package com.example.strik.lafa;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Random;

/**
 * LAFA Flashcard
 * The foundation of LAFA, the FlashCard is the
 * way of displaying Q&A formatted data in app.
 *
 * @author Alex Billson
 */
public class FlashCard implements Parcelable {
    /**
     * A unique index for the card
     */
    private int id;

    /**
     * The question/statement of the card
     */
    private String question;

    /**
     * The answer used by the flashcard
     */
    private String answer;

    /**
     * Initialize a new FlashCard with all parameters
     * @param id the unique ID of the card
     * @param question the question on the card
     * @param answer the answer on the card
     */
    public FlashCard(int id, String question, String answer)
    {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    /**
     * Default constructor for GSon usage
     */
    public  FlashCard()
    {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.question);
        parcel.writeString(this.answer);
    }

    public static final Parcelable.Creator<FlashCard> CREATOR = new Parcelable.Creator<FlashCard>() {
        @Override
        public FlashCard createFromParcel(Parcel parcel) {
            return new FlashCard(parcel);
        }

        @Override
        public FlashCard[] newArray(int i) {
            return new FlashCard[i];
        }
    };

    /**
     * Parcelable constructor
     * @param parcel the parcel
     */
    private FlashCard(Parcel parcel)
    {
        this(parcel.readInt(), parcel.readString(), parcel.readString());
    }
}

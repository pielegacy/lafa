package com.example.strik.lafa;

import android.widget.Toast;

import com.google.gson.Gson;

/**
 * LAFA is a singleton which provides methods used
 * throughout the application.
 *
 * @author Alex Billson
 */
public final class LAFA {

    private static Gson gson = new Gson();

    /**
     * Create a new FlashCard from a JSON string
     * @param JSON the json string containing the FlashCard
     * @return a serialized flashcard object
     */
    public static FlashCard cardFromJson(String JSON)
    {
        try {
            return gson.fromJson(JSON, FlashCard.class);
        }
        catch (Exception e)
        {
            return new FlashCard(-1, "", "");
        }
    }

    /**
     * Generate a JSON string from a FlashCard object
     * @param card the flashcard to stringify
     * @return the JSON representation of the card
     */
    public static String cardToJson(FlashCard card)
    {
        try {
            return gson.toJson(card);
        }
        catch (Exception e)
        {
            return "";
        }
    }
}

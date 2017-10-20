package com.example.strik.lafa;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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
     *
     * @param JSON the json string containing the FlashCard
     * @return a serialized flashcard object
     */
    public static FlashCard cardFromJson(String JSON) {
        try {
            return gson.fromJson(JSON, FlashCard.class);
        } catch (Exception e) {
            return new FlashCard(-1, "", "");
        }
    }

    /**
     * Generate a JSON string from a FlashCard object
     *
     * @param card the flashcard to stringify
     * @return the JSON representation of the card
     */
    public static String cardToJson(FlashCard card) {
        try {
            return gson.toJson(card);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Push a flashcard to a view in a FragmentActivity, handling all related
     * transactions and arguments in the process.
     *
     * @param activity the fragment activity (for context purposes)
     * @param id       the id of the output element
     * @param card     the actual flashcard
     */
    public static void pushFlashCardToView(FragmentActivity activity, int id, FlashCard card) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FlashCardFragment fragment = new FlashCardFragment();
        Bundle args = new Bundle();
        args.putParcelable("card", card);
        fragment.setArguments(args);
        fragmentTransaction.add(id, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Replace a flashcard to a view in a FragmentActivity, handling all related
     * transactions and arguments in the process.
     *
     * @param activity the fragment activity (for context purposes)
     * @param id       the id of the output element
     * @param card     the actual flashcard
     */
    public static void replaceFlashCardInView(FragmentActivity activity, int id, FlashCard card) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FlashCardFragment fragment = new FlashCardFragment();
        Bundle args = new Bundle();
        args.putParcelable("card", card);
        fragment.setArguments(args);
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

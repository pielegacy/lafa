package com.example.strik.lafa;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * LAFA is a singleton which provides methods used
 * throughout the application.
 *
 * @author Alex Billson
 */
public final class LAFA {

    /**
     * The endpoint used by LAFA to openly store data
     */
    private static final String STORAGE_ENDPOINT = "https://api.myjson.com/";

    /**
     * The media type used by JSON in the application
     */
    private static final MediaType TYPE_JSON
            = MediaType.parse("application/json");

    /**
     * Defines the key which holds the URI from the storage POST
     */
    private static final String STORAGE_URI_KEY = "uri";

    /**
     * The GSON instance used by LAFA.
     * <p>
     * Statically instantiated as it is
     * assumed the user will required GSon at least once during the
     * application session.
     */
    private static Gson gson = new Gson();

    /**
     * Static set of FlashSets used by the application.
     */
    private static ArrayList<FlashSet> appSets = new ArrayList<>();

    /**
     * Create a new FlashCard from a JSON string
     *
     * @param json the json string containing the FlashCard
     * @return a serialized flashcard object
     */
    public static FlashCard cardFromJson(String json) {
        try {
            return gson.fromJson(json, FlashCard.class);
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
            return gson.toJson(card, FlashCard.class);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Create a new FlashSet from a JSON string.
     *
     * @param json the json string containing the FlashSet data
     * @return a serialized FlashSet
     */
    public static FlashSet setFromJson(String json) {
        try {
            return gson.fromJson(json, FlashSet.class);
        } catch (Exception e) {
            return new FlashSet(new ArrayList<FlashCard>(), "", "", "", new ArrayList<Integer>());
        }
    }

    /**
     * Generate a JSON string from a FlashSet object
     *
     * @param set the FlashSet to stringify
     * @return the JSON representation of the set
     */
    public static String setToJson(FlashSet set) {
        try {
            return gson.toJson(set);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Upload a FlashSet's data to MyJson and share the link using the native
     * Android sharing dialog.
     *
     * @param set the set to share
     */
    public static void shareFlashSet(final Context context, FlashSet set) {
        PostSet(set, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context,
                        "Error sharing FlashSet, ensure you are connected to the Internet",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                if (res.code() == 201) {
                    Map<String, String> result = new HashMap<>();
                    result = gson.fromJson(res.body().string(), result.getClass());
                    if (result.containsKey(STORAGE_URI_KEY)) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, result.get(STORAGE_URI_KEY));
                        shareIntent.setType("text/plain");
                        context.startActivity(
                                Intent.createChooser(shareIntent, "Share FlashSet Link"));
                    } else
                        Toast.makeText(context,
                                "Error sharing FlashSet, ensure you are connected to the Internet",
                                Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context,
                            "Error sharing FlashSet, ensure you are connected to the Internet",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * POST a FlashSet's JSON to the defined endpoint, return the URL.
     *
     * @param set      the FlashSet that is being posted
     * @param callback the functions that are run on the post succeeding
     * @return the URL of the post
     */
    private static void PostSet(FlashSet set, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody body = RequestBody.create(TYPE_JSON, setToJson(set));
            Request req = new Request.Builder()
                    .url(STORAGE_ENDPOINT + "bins")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .post(body)
                    .build();

            Call resCall = client.newCall(req);
            resCall.enqueue(callback);

        } catch (Exception e) {
            Log.e("OkHTTP", e.getMessage());
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
        fragmentTransaction.commit();
    }

    /**
     * Add a FlashSet to the global collection of FlashSets
     *
     * @param flashSet
     */
    public static void addFlashSet(FlashSet flashSet)
    {
        appSets.add(flashSet);
    }

    /**
     * Get the current state of the AppSet
     * @return
     */
    public static ArrayList<FlashSet> getAppSets()
    {
        return appSets;
    }
}

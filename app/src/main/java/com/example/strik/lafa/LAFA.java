package com.example.strik.lafa;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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
     * Defines the filename used by LAFA for saving resources locally.
     */
    private static final String FILE_NAME = "lafa.json";

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
     * Load a collection of FlashSet from the default LAFA save file.
     *
     * @param context The application Context used for File IO.
     * @return the FlashSet data.
     */
    public static ArrayList<FlashSet> loadSetsFromFile(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            Boolean createdFile = file.createNewFile();
            // Do not attempt to load the file if it didn't exist.
            if (createdFile)
                return appSets = new ArrayList<>();
            else {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                appSets = gson.fromJson(stringBuilder.toString() != "" ?
                        stringBuilder.toString() : "[]",
                        new TypeToken<ArrayList<FlashSet>>(){}.getType());
                return appSets;
            }

        } catch (Exception e) {
            Toast.makeText(context,
                    "Error loading from file, clear application data if this problem persists.",
                    Toast.LENGTH_SHORT).show();
            file.delete();
            return appSets = new ArrayList<>();
        }
    }

    /**
     * Delete the LAFA application data file and reload the current appSet
     * @param context an application context to use for File IO.
     * @return the result of the file deletion.
     */
    public static boolean clearData(Context context)
    {
        File file = new File(context.getFilesDir(), FILE_NAME);
        try
        {
            Boolean isDeleted = file.delete();
            if (isDeleted)
                loadSetsFromFile(context);
            return isDeleted;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Write the current contents of AppSets to file.
     *
     * @param context
     */
    public static void writeToFile(Context context) {
        try {
            FileOutputStream outputStream = null;
            try {
                outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                outputStream.write(gson.toJson(appSets).getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null)
                    outputStream.close();
            }

        } catch (Exception e) {
            Toast.makeText(context,
                    "Error writing to file, clear application data if this problem persists.",
                    Toast.LENGTH_SHORT).show();
        }
    }

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
     * Download a FlashSet from a remote store.
     * @param context application context
     * @param endpoint the endpoint to download the FlashSet from.
     */
    public static void downloadFlashSet(final Context context, final MainActivity activity, final String endpoint)
    {
        GetSet(endpoint, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(
                        context,
                        "Failed to download FlashSet from " + endpoint,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try
                {
                    final FlashSet flashSet = gson.fromJson(response.body().string(), FlashSet.class);
                    addFlashSet(flashSet);
                    LAFA.writeToFile(context);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.populateFlashSets();
                            Toast.makeText(
                                    context,
                                    "Downloaded '" + flashSet.getName() + "'.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (Exception e)
                {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(
                                    context,
                                    "Error creating FlashSet from URL.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
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
     * Get a FlashSet from a JSON endpoint.
     * @param endpoint the endpoint to query
     * @param callback the function which is ran when the GET request is finished
     */
    private static void GetSet(String endpoint, Callback callback)
    {
        OkHttpClient client = new OkHttpClient();

        try {
            Request req = new Request.Builder()
                    .url(endpoint)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .get()
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
    public static void addFlashSet(FlashSet flashSet) {
        appSets.add(flashSet);
    }

    /**
     * Get the current state of the AppSet
     *
     * @return
     */
    public static ArrayList<FlashSet> getAppSets() {
        return appSets;
    }
}

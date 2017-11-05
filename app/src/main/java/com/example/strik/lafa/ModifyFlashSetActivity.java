package com.example.strik.lafa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ModifyFlashSetActivity extends AppCompatActivity {

    /**
     * The set being modified.
     */
    private FlashSet flashSet;
    private FlashCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flash_set);
        if ((flashSet = getIntent().getExtras().getParcelable("data")) != null) {
            setTitle("Editing " + flashSet.getName());
            populateList(flashSet.getSet());
            applyUITriggers();
        } else {
            Toast.makeText(this, "Invalid FlashSet...", Toast.LENGTH_SHORT).show();
            Intent navIntent = new Intent(this, MainActivity.class);
            navIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(navIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                saveSet();
                break;
        }
        return true;
    }

    /**
     * Save the FlashSet and navigate to the home page.
     */
    private void saveSet() {
        if (flashSet.getSet().size() == 0) {
            Snackbar.make(findViewById(R.id.layout_modifyFlashSet),
                    "Cannot save empty FlashSet",
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        LAFA.addFlashSet(flashSet);
        LAFA.writeToFile(this);
        Intent navIntent = new Intent(this, MainActivity.class);
        navIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(navIntent);
        Toast.makeText(getApplicationContext(), "FlashSet Saved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Apply event logic to the UI elements of the page.
     */
    private void applyUITriggers() {
        findViewById(R.id.button_add_flashcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCard();
            }
        });
    }

    /**
     * Using a list of FlashCards, populate the ListView of the modify screen.
     *
     * @param list the source of the population.
     */
    private void populateList(List<FlashCard> list) {
        ListView listViewFlashCards = (ListView) findViewById(R.id.list_flashCardsModify);

        if (adapter == null)
            adapter = new FlashCardAdapter(this, list);
        else {
            adapter.clear();
            adapter.addAll(list);
        }
        listViewFlashCards.setAdapter(adapter);
        listViewFlashCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int id = ((FlashCard)adapterView.getItemAtPosition(i)).getId();
                PopupMenu popupMenuEditFlashCard = new PopupMenu(
                        view.getContext(),
                        view.findViewById(R.id.button_flashCardMarker));
                popupMenuEditFlashCard.getMenuInflater().inflate(R.menu.popup_flashcard,
                        popupMenuEditFlashCard.getMenu());
                popupMenuEditFlashCard.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.menu_item_flashcard_edit:
                                editCard(id);
                                return true;
                            case R.id.menu_item_flashcard_delete:
                                removeCard(id);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenuEditFlashCard.show();
            }
        });
    }

    /**
     * Add a new card to the FlashSet and the List.
     */
    private void addNewCard() {
        EditText editTextQuestion = (EditText) findViewById(R.id.editText_card_question);
        EditText editTextAnswer = (EditText) findViewById(R.id.editText_card_answer);

        if (!editTextQuestion.getText().toString().isEmpty() &&
                !editTextAnswer.getText().toString().isEmpty()) {
            FlashCard card = new FlashCard(-1,
                    editTextQuestion.getText().toString(),
                    editTextAnswer.getText().toString());
            flashSet.pushFlashCard(card);
            editTextQuestion.setText("");
            editTextQuestion.requestFocus();
            editTextAnswer.setText("");
            populateList(flashSet.getSet());
        }
    }

    /**
     * Remove the card in question from the array, copying the card to the inputs
     * of the form.
     *
     * @param cardId the ID of the card to edit.
     */
    private void editCard(int cardId) {
        FlashCard flashCard = flashSet.removeById(cardId);
        if (flashCard != null) {
            EditText editTextQuestion = (EditText) findViewById(R.id.editText_card_question);
            EditText editTextAnswer = (EditText) findViewById(R.id.editText_card_answer);
            editTextQuestion.setText(flashCard.getQuestion());
            editTextAnswer.setText(flashCard.getAnswer());
        }
        populateList(flashSet.getSet());
    }

    /**
     * Remove the card from the FlashSet after confirming with this user that this
     * was the intended decision.
     * @param cardId the ID of the card to remove.
     */
    private void removeCard(final int cardId) {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you'd like to remove this card from the set?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        flashSet.removeById(cardId);
                        populateList(flashSet.getSet());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}

package com.example.strik.lafa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ModifyFlashSetActivity extends AppCompatActivity {

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

    /**
     * Apply event logic to the UI elements of the page
     */
    private void applyUITriggers()
    {
        findViewById(R.id.button_add_flashcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCard();
            }
        });
    }

    /**
     * Using a list of FlashCards, populate the ListView of the modify screen
     *
     * @param list
     */
    private void populateList(List<FlashCard> list) {
        ListView listViewFlashCards = (ListView) findViewById(R.id.list_flashCardsModify);

        if (adapter == null)
            adapter = new FlashCardAdapter(this, list);
        else
        {
            adapter.clear();
            adapter.addAll(list);
        }

        listViewFlashCards.setAdapter(adapter);
    }

    /**
     * Add a new card to the FlashSet and the List
     */
    private void addNewCard()
    {
        EditText editTextQuestion = (EditText)findViewById(R.id.editText_card_question);
        EditText editTextAnswer = (EditText)findViewById(R.id.editText_card_answer);

        if (!editTextQuestion.getText().toString().isEmpty() &&
                !editTextAnswer.getText().toString().isEmpty()) {
            FlashCard card = new FlashCard(-1,
                    editTextQuestion.getText().toString(),
                    editTextAnswer.getText().toString());
            flashSet.pushFlashCard(card);
            populateList(flashSet.getSet());
        }
    }
}

package com.example.strik.lafa;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateFlashSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flash_set);
        setTitle(getResources().getString(R.string.title_activity_create_flash_set));
        findViewById(R.id.fab_create_flash_next)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isFormValid()) {
                            Intent submitFormIntent = new Intent(view.getContext(), ModifyFlashSetActivity.class);
                            submitFormIntent.putExtra("data", getFormData());
                            startActivity(submitFormIntent);
                        }
                    }
                });
    }

    /**
     * Ensure the form is valid, apply any error checking if necessary
     *
     * @return true if form is valid, false otherwise
     */
    private boolean isFormValid() {
        boolean isValid = true;

        EditText editTextSetTitle = (EditText) findViewById(R.id.editText_set_title);

        if (editTextSetTitle.getText().toString().isEmpty()) {
            isValid = false;
            editTextSetTitle.setError("FlashSet Title is required!");
        }

        return isValid;
    }

    /**
     * Get the data of the form as a HashMap
     *
     * @return
     */
    private FlashSet getFormData() {
        ArrayList<FlashCard> list = new ArrayList<>();
        list.add(new FlashCard(0, "Q", "A"));
        return new FlashSet(list,
                ((EditText) findViewById(R.id.editText_set_title)).getText().toString(),
                ((EditText) findViewById(R.id.editText_set_author)).getText().toString(),
                "",
                new int[0]
        );
    }
}

package com.example.strik.lafa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlashCard[] cards = {
            new FlashCard(0, "Are traps gay?", "Yes."),
            new FlashCard(1, "Are you sure?", "Yes.")
        };

        FlashSetManager manager = new FlashSetManager(this, R.id.layout_output,
                new FlashSet(Arrays.asList(cards), "Test Collection"));
        manager.pushCardsToList();
        testGson();
    }

    private void testGson()
    {
        String testJson = "{'id': 1, 'question': 'Test question', 'answer': 'test asnwer'}";
        FlashCard testCard = LAFA.cardFromJson(LAFA.cardToJson(LAFA.cardFromJson(testJson)));
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}

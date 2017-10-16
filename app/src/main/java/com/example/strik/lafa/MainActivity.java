package com.example.strik.lafa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FlashCardFragment fragment = new FlashCardFragment();
        Bundle args = new Bundle();
        args.putParcelable("card", new FlashCard(0, "Test", "Answer"));
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.layout_output, fragment);
        fragmentTransaction.commit();

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

package com.example.strik.lafa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ModifyFlashSetActivity extends AppCompatActivity {

    private FlashSet flashSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flash_set);
        if ((flashSet = getIntent().getExtras().getParcelable("data")) != null)
        {
            setTitle("Editing " + flashSet.getName());
        }
        else
        {
            Toast.makeText(this, "Invalid FlashSet...", Toast.LENGTH_SHORT).show();
            Intent navIntent = new Intent(this, MainActivity.class);
            navIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(navIntent);
        }
    }
}

package com.example.strik.lafa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        applySettingsTriggers();
    }

    /**
     * Apply UI events and logic to the settings page
     */
    private void applySettingsTriggers() {
        findViewById(R.id.button_clear_all_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Are you sure you want to clear your LAFA collection?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Boolean clearSuccessful = LAFA.clearData(view.getContext());
                                if (clearSuccessful) {
                                    Intent navIntent = new Intent(view.getContext(), MainActivity.class);
                                    navIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(navIntent);
                                }
                                Toast.makeText(view.getContext(),
                                        clearSuccessful
                                                ? "Data cleared successfully!"
                                                : "Error clearing application data, please try again."
                                        , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }
}

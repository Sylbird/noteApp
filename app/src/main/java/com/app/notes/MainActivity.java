package com.app.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    //globalVars
    private int NightMode;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize realm
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        // Add an initial note to Realm
        addInitialNote();

        // Retrieve and display notes
        RealmResults<Note> listNotes = realm.where(Note.class).findAll();
        RecyclerView recyclerView = findViewById(R.id.notesContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(),listNotes);
        recyclerView.setAdapter(noteAdapter);

        listNotes.addChangeListener(notes -> noteAdapter.notifyDataSetChanged());

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        NightMode = sharedPreferences.getInt("NightModeInt", 1);
        AppCompatDelegate.setDefaultNightMode(NightMode);

        SwitchCompat switchBtn = findViewById(R.id.switch_dayNight);

        switchBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        switchBtn.setChecked(isNightModeOn);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        NightMode = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("NightModeInt", NightMode);
        editor.apply();
    }

    private void addInitialNote() {
        // Check if the note already exists
        if (realm.where(Note.class).equalTo("id", 1).findFirst() == null) {
            // Start a Realm transaction
            realm.executeTransactionAsync(realm -> {
                // Create a new note
                Note initialNote = realm.createObject(Note.class, 1);
                initialNote.setTitle("Sample Title");
                initialNote.setContent("This is a sample note content.");
                initialNote.setCreatedTime(23232323L);
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance when the activity is destroyed
        realm.close();
    }
}
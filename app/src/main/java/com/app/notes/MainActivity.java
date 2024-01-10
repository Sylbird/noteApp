package com.app.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    //globalVars
    private int NightMode;
    private Realm realm;
    private SharedPreferences sharedPreferences;
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize realm
        Realm.init(getApplicationContext());

        // Setup config to allow writeOnUI
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
        //
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

        // Get NightMode from SharedPreferences if it exists
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        NightMode = sharedPreferences.getInt("NightModeInt", 1);
        AppCompatDelegate.setDefaultNightMode(NightMode);

        // Switch Night Mode
        SwitchCompat switchBtn = findViewById(R.id.switch_dayNight);
        switchBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        // Check if Night Mode is ON
        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        switchBtn.setChecked(isNightModeOn);

        // Adding Listeners for Buttons
        FloatingActionButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            // Create intent to start NoteCreationActivity
            Intent intent = new Intent(MainActivity.this, NoteCreationActivity.class);
            startActivity(intent);
        });

        FloatingActionButton syncJsonButton = findViewById(R.id.syncJsonButton);
        syncJsonButton.setOnClickListener(v -> {
            HttpService.get("http://10.0.2.2:3000/Notes", response -> runOnUiThread(() -> {

                // Replace the existing notes with the new ones from the JSON response
                realm.executeTransactionAsync(realm -> {
                    // Convert the JSON response to a list of Note objects
                    Type listType = new TypeToken<ArrayList<Note>>(){}.getType();

                    List<Note> notes = new Gson().fromJson(response, listType);

                    // Clear the existing notes
                    realm.delete(Note.class);

                    // Add the notes from the JSON response
                    for (Note note : notes) {
                        realm.insert(note);
                    }
                });
                Toast.makeText(MainActivity.this,"Sincronizacion Exitosa!.",Toast.LENGTH_SHORT).show();
            }));
        });

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            logout();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Get NightMode from AppCompatDelegate
        NightMode = AppCompatDelegate.getDefaultNightMode();
        // Save NightMode to SharedPreferences
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("NightModeInt", NightMode);
        editor.apply();
    }

    private void addInitialNote() {
        // Check if the note already exists
        if (realm.where(Note.class).equalTo("id", 0).findFirst() == null) {
            // Start a Realm transaction
            realm.executeTransactionAsync(realm -> {
                // Create a new note
                Note initialNote = realm.createObject(Note.class,0);
                initialNote.setTitle("Sample Title");
                initialNote.setContent("This is a sample note content.");
                initialNote.setCreatedTime(1678828800000L);
            });
        }
    }

    private void logout() {
        // Acceder a SharedPreferences y eliminar la información de inicio de sesión
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.putBoolean("isLoggedIn", false); // Actualizar el estado de inicio de sesión
        editor.apply();

        // Redirigir a LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance when the activity is destroyed
        realm.close();
    }
}
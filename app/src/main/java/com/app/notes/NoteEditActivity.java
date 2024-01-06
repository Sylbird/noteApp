package com.app.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import io.realm.Realm;

public class NoteEditActivity extends AppCompatActivity {

    //globalVars
    private EditText titleEditText;
    private EditText contentEditText;
    private Realm realm;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        ImageButton btnSaveNote = findViewById(R.id.noteEdit_buttonSaveNote);
        ImageButton btnGoBack = findViewById(R.id.noteEdit_buttonGoBack);
        ImageButton btnDeleteNote = findViewById(R.id.noteEdit_buttonDeleteNote);

        // Buttons Listener
        btnSaveNote.setOnClickListener(view -> onSaveClick());
        btnGoBack.setOnClickListener(view -> finish());
        btnDeleteNote.setOnClickListener(view -> onDeleteClick());

        // Get the note ID from the intent
        long noteId = getIntent().getLongExtra("noteId", -1);

        // Initialize Realm
        realm = Realm.getDefaultInstance();

        // Fetch the note from Realm
        note = realm.where(Note.class).equalTo("id", noteId).findFirst();

        // Check if the note exists
        if (note != null) {
            // Populate activity_note_edit.xml with the note details (EditText for title, content, etc.)
            titleEditText = findViewById(R.id.noteEdit_editTextTitle);
            contentEditText = findViewById(R.id.noteEdit_editTextContent);

            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
        } else {
            finish();
        }
    }

    private void onSaveClick() {
        // Update the note with the edited data on the UI thread
        runOnUiThread(() -> realm.executeTransaction(realm -> {
            note.setTitle(titleEditText.getText().toString());
            note.setContent(contentEditText.getText().toString());
            note.setCreatedTime(System.currentTimeMillis());
        }));
        Toast.makeText(NoteEditActivity.this, "Nota modificada.", Toast.LENGTH_SHORT).show();
        // Finish the activity
        finish();
    }

    // Delete button click event
    public void onDeleteClick() {
        // Delete the note from Realm
        realm.executeTransaction(realm -> note.deleteFromRealm());
        Toast.makeText(NoteEditActivity.this, "Nota eliminada.", Toast.LENGTH_SHORT).show();
        // Finish the activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance
        realm.close();
    }
}
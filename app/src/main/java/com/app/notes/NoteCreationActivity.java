package com.app.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import io.realm.Realm;

public class NoteCreationActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_creation);

        etTitle = findViewById(R.id.editTextTitle);
        etContent = findViewById(R.id.editTextContent);
        ImageButton btnSaveNote = findViewById(R.id.buttonSaveNote);
        ImageButton btnGoBack = findViewById(R.id.buttonGoBack);

        // Inicializar Realm
        realm = Realm.getDefaultInstance();

        btnSaveNote.setOnClickListener(v -> guardarNota());

        btnGoBack.setOnClickListener(view -> finish());
    }

    private void guardarNota() {
        final String title = etTitle.getText().toString();
        final String content = etContent.getText().toString();

        if (!title.isEmpty() && !content.isEmpty()) {
            // Generar un ID único para la nueva nota
            Number currentIdNum = realm.where(Note.class).max("id");
            long nextId = (currentIdNum != null) ? currentIdNum.longValue() + 1 : 1;

            // Crear una nueva nota en Realm
            realm.executeTransactionAsync(realm -> {
                Note newNote = realm.createObject(Note.class, nextId);
                newNote.setTitle(title);
                newNote.setContent(content);
                newNote.setCreatedTime(System.currentTimeMillis());
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Notificar que la nota se guardó correctamente
                    Toast.makeText(NoteCreationActivity.this, "Nota guardada", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la actividad anterior
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Manejar errores si la nota no se pudo guardar
                    Toast.makeText(NoteCreationActivity.this, "Error al guardar la nota", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Manejar caso donde los campos están vacíos
            Toast.makeText(NoteCreationActivity.this, "Completa los campos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar el Realm instance cuando la actividad se destruye
        if (realm != null) {
            realm.close();
        }
    }
}

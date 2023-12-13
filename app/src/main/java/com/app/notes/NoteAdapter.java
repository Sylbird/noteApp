package com.app.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.RealmResults;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    RealmResults<Note> listNotes;

    public NoteAdapter(Context context, RealmResults<Note> listNotes) {
        this.context = context;
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());

        String formatedTime = DateFormat.getDateTimeInstance().format(note.createdTime);
        holder.noteTimeStamp.setText(formatedTime);

        // Set a click listener for the entire note view
        holder.cardViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here, for example, open the EditNoteActivity
                Note clickedNote = listNotes.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, NoteEditActivity.class);
                intent.putExtra("noteId", clickedNote.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView noteContent;
        TextView noteTimeStamp;
        CardView cardViewNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewNote = itemView.findViewById(R.id.cardViewNote);
            noteTitle = itemView.findViewById(R.id.textNoteTitle);
            noteContent = itemView.findViewById(R.id.textNoteContent);
            noteTimeStamp = itemView.findViewById(R.id.textNoteTimestamp);
        }
    }

}

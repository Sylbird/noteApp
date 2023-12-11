package com.app.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView noteContent;
        TextView noteTimeStamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.textNoteTitle);
            noteContent = itemView.findViewById(R.id.textNoteContent);
            noteTimeStamp = itemView.findViewById(R.id.textNoteTimestamp);
        }
    }

}

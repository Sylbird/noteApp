package com.app.notes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Note extends RealmObject {
    @PrimaryKey
    long id;
    String title;
    String content;
    Long createdTime;

    // Constructor, getters, and setters

    public Note(){

    }

    public Note(String title, String content, Long createdTime) {
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatedTime(){ return createdTime;}

    public void setCreatedTime(Long createdTime){ this.createdTime= createdTime; }
}

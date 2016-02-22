package com.jude.know.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public class Answer implements Parcelable {
    String id;
    String date;
    String content;
    String authorId;
    String authorName;

    public String getAuthorFace() {
        return authorFace==null?"":authorFace;
    }

    public void setAuthorFace(String authorFace) {
        this.authorFace = authorFace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    String authorFace;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeString(this.content);
        dest.writeString(this.authorId);
        dest.writeString(this.authorName);
        dest.writeString(this.authorFace);
    }

    public Answer() {
    }

    protected Answer(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.content = in.readString();
        this.authorId = in.readString();
        this.authorName = in.readString();
        this.authorFace = in.readString();
    }

    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}

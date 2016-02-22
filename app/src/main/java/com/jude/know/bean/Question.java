package com.jude.know.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class Question implements Parcelable {
    int id;
    String title;
    String content;
    String bestAnswerId;
    String date;
    String authorId;
    String authorName;
    String authorFace;
    int answerCount;
    String recent;

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getBestAnswerId() {
        return bestAnswerId;
    }

    public void setBestAnswerId(String bestAnswerId) {
        this.bestAnswerId = bestAnswerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAuthorFace() {
        return authorFace==null?"":authorFace;
    }

    public void setAuthorFace(String authorFace) {
        this.authorFace = authorFace;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.bestAnswerId);
        dest.writeString(this.date);
        dest.writeString(this.authorId);
        dest.writeString(this.authorName);
        dest.writeString(this.authorFace);
        dest.writeInt(this.answerCount);
        dest.writeString(this.recent);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.bestAnswerId = in.readString();
        this.date = in.readString();
        this.authorId = in.readString();
        this.authorName = in.readString();
        this.authorFace = in.readString();
        this.answerCount = in.readInt();
        this.recent = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}

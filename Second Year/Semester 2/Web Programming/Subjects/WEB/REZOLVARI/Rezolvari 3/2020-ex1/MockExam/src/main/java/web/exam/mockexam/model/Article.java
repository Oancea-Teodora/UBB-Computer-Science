package web.exam.mockexam.model;

import java.sql.Timestamp;

public class Article {
    private int id;
    private String user;
    private int journalId;
    private String summary;
    private Timestamp date;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", journalId=" + journalId +
                ", summary='" + summary + '\'' +
                ", date=" + date +
                '}';
    }

    public Article(int id, String user, int journalId, String summary, Timestamp date) {
        this.id = id;
        this.user = user;
        this.journalId = journalId;
        this.summary = summary;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}

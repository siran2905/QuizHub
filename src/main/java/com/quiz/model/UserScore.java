package com.quiz.model;
import jakarta.persistence.*;

@Entity
@Table(name = "user_scores")
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @Column(name = "selected_level")
    private String selectedLevel;

    private int score;

    @Column(name = "time_taken_seconds")
    private int timeTakenSeconds;

    public UserScore() {}

    public UserScore(String username, String selectedLevel, int score, int timeTakenSeconds) {
        this.username = username;
        this.selectedLevel = selectedLevel;
        this.score = score;
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getSelectedLevel() { return selectedLevel; }
    public int getScore() { return score; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
}

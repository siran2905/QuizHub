package com.quiz;

import com.quiz.model.*;
import com.quiz.util.QuestionTimer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuizApp {
    private static final SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.print("Enter Player Name: ");
            String username = sc.nextLine();

            System.out.println("\nChoose Level: 1. Easy | 2. Medium | 3. Hard");
            System.out.print("Selection (1-3): ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            Class<? extends Question> targetClass;
            String levelName;

            switch (choice) {
                case 1: 
                    targetClass = EasyQuestion.class; 
                    levelName = "Easy"; 
                    break;
                case 2:
                    targetClass = MediumQuestion.class; 
                    levelName = "Medium"; 
                    break;
                case 3:
                    targetClass = HardQuestion.class; 
                    levelName = "Hard"; 
                    break;
                default:
                    System.out.println("Invalid level selected.");
                    return;
            }

            
            List<Question> questionPool = fetchSequential15Questions(targetClass);
            if (questionPool.isEmpty()) {
                System.out.println("No questions found in database for level " + levelName);
                return;
            }

            int score = 0;
            long startTime = System.currentTimeMillis();

            System.out.println("\n==============================================");
            System.out.println(" Starting Quiz! 15 Questions | 25s per Question");
            System.out.println("==============================================");

            for (int i = 0; i < questionPool.size(); i++) {
                Question q = questionPool.get(i);
                System.out.println("\nQuestion " + (i + 1) + "/15: " + q.getQuestionText());
                System.out.println("A) " + q.getOptionA());
                System.out.println("B) " + q.getOptionB());
                System.out.println("C) " + q.getOptionC());
                System.out.println("D) " + q.getOptionD());

                AtomicBoolean answered = new AtomicBoolean(false);
                Thread timerThread = new Thread(new QuestionTimer(25, answered, Thread.currentThread()));
                timerThread.start();

                System.out.print("Your choice (A/B/C/D): ");
                String input = null;

                try {
                    input = sc.nextLine();
                    answered.set(true);
                    timerThread.interrupt();
                } catch (Exception e) {
                    input = null;
                }

                if (input != null && !input.trim().isEmpty()) {
                    char userAnswer = Character.toUpperCase(input.trim().charAt(0));
                    if (userAnswer == q.getCorrectOption()) {
                        System.out.println("✅ Correct!");
                        score++;
                    } else {
                        System.out.println("❌ Incorrect! Correct answer was: " + q.getCorrectOption());
                    }
                } else {
                    System.out.println("Skipped / Out of Time!");
                }
            }

            int totalTimeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);
            saveUserScore(username, levelName, score, totalTimeTaken);
            
            System.out.println("\n==============================================");
            System.out.println(" Quiz Completed! Score: " + score + "/15 | Time: " + totalTimeTaken + "s");
            System.out.println("==============================================");

            showLeaderboard();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            factory.close();
        }
    }

    private static List<Question> fetchSequential15Questions(Class<? extends Question> qes) {
        try (Session session = factory.openSession()) {
            List list = session.createQuery("FROM " + qes.getSimpleName()).list();            
            return list.subList(0, Math.min(15, list.size()));
        }
    }

    private static void saveUserScore(String username, String level, int score, int time) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            UserScore userScore = new UserScore(username, level, score, time);
            session.persist(userScore);
            session.getTransaction().commit();
        }
    }

    private static void showLeaderboard() {
        try (Session session = factory.openSession()) {
            List<UserScore> leaderboard = session.createQuery(
                "FROM UserScore ORDER BY score DESC, timeTakenSeconds ASC", UserScore.class)
                .setMaxResults(5)
                .list();

            System.out.println("\n🏆 TOP PERFORMERS LEADERBOARD 🏆");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-5s %-15s %-10s %-8s %-10s\n", "Rank", "Player", "Level", "Score", "Time(s)");
            System.out.println("--------------------------------------------------");

            int rank = 1;
            for (UserScore s : leaderboard) {
                System.out.printf("%-5d %-15s %-10s %-8d %-10d\n", 
                    rank++, s.getUsername(), s.getSelectedLevel(), s.getScore(), s.getTimeTakenSeconds());
            }
            System.out.println("--------------------------------------------------");
        }
    }
}
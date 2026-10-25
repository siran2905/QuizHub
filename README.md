# 🧠 Java Multi-Level Quiz Application (Hibernate 7 & MySQL)

A robust, console-based multi-level quiz application built in Java. It leverages Hibernate ORM (v7) and Jakarta Persistence with a MySQL backend. The system features dynamic level selection, a strict multi-threaded 25-second timeout mechanism per question, score tracking, and a sorted global leaderboard.

---
## Key Features

* Multi-Level Difficulty: Choose between Easy, Medium, and Hard question pools mapped to dedicated database tables.
* Sequential Quiz Flow: Fetches 15 sequential questions per session.
* Strict Timeout Control: Enforces a strict 25-second countdown per question using Java's ExecutorService. If time runs out, it automatically flags the question as incorrect/skipped and seamlessly moves to the next prompt without freezing.
* Modern Hibernate 7 Configuration: Utilizes Jakarta Persistence (jakarta.persistence.*), modern session.persist() methods, type-safe queries, and auto-dialect detection.
* Performance Leaderboard: Automatically calculates total completion time and ranks players by highest score descending, followed by lowest time taken ascending.

---

## 🛠️ Tech Stack

* Language: Java 17+
* ORM Framework: Hibernate ORM 7.x
* Persistence API: Jakarta Persistence (`jakarta.persistence.*`)
* Database: MySQL (8.0+)
* Build Tool: Maven / Standard Java IDE Compiler

---

📁 Project Structure


src/
├── com/quiz/
│   ├── model/
│   │   ├── Question.java          (MappedSuperclass)
│   │   ├── EasyQuestion.java      (@Entity - easy_questions)
│   │   ├── MediumQuestion.java    (@Entity - medium_questions)
│   │   ├── HardQuestion.java      (@Entity - hard_questions)
│   │   └── UserScore.java         (@Entity - user_scores)
│   └── QuizApp.java               (Main execution & game loop)
└── resources/
    └── hibernate.cfg.xml          (Hibernate & MySQL configuration)

package com.quiz.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class QuestionTimer implements Runnable {
    private final int timeLimitSeconds;
    private final AtomicBoolean answered;
    private final Thread mainThread;

    public QuestionTimer(int timeLimitSeconds, AtomicBoolean answered, Thread mainThread) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.answered = answered;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        try {
            for (int secondsLeft = timeLimitSeconds; secondsLeft > 0; secondsLeft--) {
                if (answered.get()) {
                    return;
                }
                
                if (secondsLeft <= 5) {
                    System.out.print("\r[⚠️ TIME RUNNING OUT: " + secondsLeft + "s remaining!] Your choice: ");
                }

                Thread.sleep(1000);
            }

            if (!answered.get()) {
                System.out.println("\n\n⏰ Time's up for this question!");
                mainThread.interrupt();
            }
        } catch (InterruptedException e) {
            // Timer canceled because answer was submitted in time
        }
    }
}

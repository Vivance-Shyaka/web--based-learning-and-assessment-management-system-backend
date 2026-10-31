package kigali.learning.rw.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "result_feedback")
public class ResultFeedback {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "question_area")
    private String questionArea;

    @Column (name = "comment")
    private String comment;

    @Column (name = "marks")
    private int marks;

    @Column (name = "feedback_date")
    private LocalDate feedbackDate;

    @ManyToOne
    @JoinColumn (name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;

    public UUID getId() {
        return id;
    }

    public String getQuestionArea() {
        return questionArea;
    }

    public void setQuestionArea(String questionArea) {
        this.questionArea = questionArea;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

}

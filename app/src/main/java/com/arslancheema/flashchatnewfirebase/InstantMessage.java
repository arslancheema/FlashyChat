package com.arslancheema.flashchatnewfirebase;

/**
 * Created by Cheema on 2017/09/05.
 */

public class InstantMessage {
    private String message ;
    private String author ;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    // No argument constructor is required by Firebase
    public InstantMessage() {

    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}

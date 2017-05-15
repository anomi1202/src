package com.javarush.task.task37.task3712;

/**
 * Created by Ru on 14.05.2017.
 */
public abstract class Game implements Runnable {

    abstract void prepareForTheGame();

    abstract void playGame();

    abstract void congratulateWinner();

    @Override
    public void run() {
        prepareForTheGame();
        playGame();
        congratulateWinner();

    }
}

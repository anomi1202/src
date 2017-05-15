package com.javarush.task.task37.task3712;

public class Basketball extends Game{
    @Override
    public void prepareForTheGame() {
        System.out.println("Preparing for the Basketball game...");
    }

    @Override
    public void playGame() {
        System.out.println("Playing basketball!");
    }

    @Override
    public void congratulateWinner() {
        System.out.println("Give it up to the NBA finals champions!!!");
    }
}

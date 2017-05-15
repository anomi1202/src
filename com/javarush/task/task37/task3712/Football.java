package com.javarush.task.task37.task3712;

public class Football extends Game{
    @Override
    public void prepareForTheGame() {
        System.out.println("Preparing for the Football game...");
    }

    @Override
    public void playGame() {
        System.out.println("Kickoff!!! Playing football!");
    }

    @Override
    public void congratulateWinner() {
        System.out.println("Here is the new football CHAMPION!!!");
    }
}

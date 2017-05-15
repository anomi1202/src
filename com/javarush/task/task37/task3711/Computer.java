package com.javarush.task.task37.task3711;

/**
 * Created by Ru on 14.05.2017.
 */
public class Computer implements Runnable {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public Computer() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    @Override
    public void run() {
        cpu.calculate();
        memory.allocate();
        hardDrive.storeData();
    }
}

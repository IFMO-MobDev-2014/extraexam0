package ru.ifmo.md.exam1;

/**
 * Created by daria on 30.01.15.
 */
public class Build {
    private String name;
    private int number;
    private int counter;
    private String lastVerdict;


    public Build(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getLastVerdict() {
        return lastVerdict;
    }

    public void setLastVerdict(String lastVerdict) {
        this.lastVerdict = lastVerdict;
    }
}

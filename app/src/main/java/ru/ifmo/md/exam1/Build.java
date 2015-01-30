package ru.ifmo.md.exam1;

/**
 * Created by daria on 30.01.15.
 */
public class Build {
    private String number;
    private int counter;
    private String lastVerdict;


    public Build(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

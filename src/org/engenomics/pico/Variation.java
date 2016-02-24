package org.engenomics.pico;

public class Variation {
    private int position;
    private char base;

    public Variation(int position, char base) {

        this.position = position;
        this.base = base;
    }

    @Override
    public String toString() {
        return "Variation{" +
                "position=" + position +
                ", base=" + base +
                '}';
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public char getBase() {
        return base;
    }

    public void setBase(char base) {
        this.base = base;
    }
}

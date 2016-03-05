package org.engenomics.pico;

import java.util.ArrayList;
import java.util.List;

public class PositionsFrequencyPair {
    private List<Integer> positions = new ArrayList<>();
    private int frequency;

    public PositionsFrequencyPair(int position, int frequency) {
        this.positions.add(position);
        this.frequency = frequency;
    }

    public PositionsFrequencyPair(int position) {
        this.positions.add(position);
        this.frequency = 1;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public int getFrequency() {

        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        this.frequency++;
    }

    public void addPosition(int position) {
        this.positions.add(position);
    }

    @Override
    public String toString() {
        return "PositionsFrequencyPair{" +
                "positions=" + positions +
                ", frequency=" + frequency +
                '}';
    }
}

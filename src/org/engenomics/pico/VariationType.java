package org.engenomics.pico;

import java.util.ArrayList;
import java.util.List;

public class VariationType implements Comparable<VariationType> {
    private Variation variation;
    private long frequency;
    private List<Integer> positions = new ArrayList<>();

    public VariationType(Variation variation, long frequency) {
        this.variation = variation;
        this.frequency = frequency;
    }

    public VariationType(Variation variation, long frequency, List<Integer> positions) {
        this.variation = variation;
        this.frequency = frequency;
        this.positions = positions;

    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "VariationType{" +
                "variation=" + variation +
                ", frequency=" + frequency +
                ", positions=" + positions +
                '}';
    }

    public Variation getVariation() {
        return variation;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(VariationType o) {
        if (o.getFrequency() > this.getFrequency()) {
            return 1;
        } else if (o.getFrequency() < this.getFrequency()) {
            return -1;
        } else {
            return 0;
        }
    }
}

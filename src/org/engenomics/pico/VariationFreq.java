package org.engenomics.pico;

import java.util.ArrayList;
import java.util.List;

public class VariationFreq implements Comparable<VariationFreq> {
    private Variation variation;
    private int freq;
    private int id;
    private List<Integer> positions = new ArrayList<>();

    public VariationFreq(Variation variation, int freq) {
        this.variation = variation;
        this.freq = freq;
    }

    public VariationFreq(Variation variation) {
        this.variation = variation;
        this.freq = 1;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "VariationFreq{" +
                "variation=" + variation.toStringNoPosition() +
                ", freq=" + freq +
                ", id=" + id +
                ", positions=" + positions +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Variation getVariation() {
        return variation;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public void incrementFreq() {
        this.freq++;
    }


    @Override
    public int compareTo(VariationFreq o) {
        return o.getFreq() - this.freq;
    }
}

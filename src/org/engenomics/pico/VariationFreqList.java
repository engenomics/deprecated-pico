package org.engenomics.pico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class VariationFreqList extends ArrayList<VariationFreq> {
    public VariationFreqList(int initialCapacity) {
        super(initialCapacity);
    }

    public VariationFreqList() {
    }

    public VariationFreqList(Collection c) {
        super(c);
    }

    public boolean containsVariation(String variation) {
        for (VariationFreq vf : this) {
            if (Objects.equals(vf.getVariation().getReplacement(), variation)) {
                return true;
            }
        }

        return false;
    }

    public int indexOfVariation(String variation) {
        for (VariationFreq vf : this) {
            if (Objects.equals(vf.getVariation().getReplacement(), variation)) {
                return this.indexOf(vf);
            }
        }

        return -1;
    }
}

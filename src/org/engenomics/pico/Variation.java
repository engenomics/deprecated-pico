package org.engenomics.pico;

public class Variation {
    private int position;
    private char base;
    private String orig;
    private String replacement;
    private int type;
    /*
        type:
            0 - SNP
            1 - insertion
            2 - deletion
     */

    /**
     * For SNPs
     *
     * @param position
     * @param base
     */
    public Variation(int position, char base) {
        this.position = position;
        this.base = base;
        this.type = 0;
    }

    /**
     * For insertions
     *
     * @param position
     * @param replacement
     */
    public Variation(int position, String replacement) {
        this.position = position;
        this.replacement = replacement;
        this.type = 1;
    }

    /**
     * For deletions and/or replacements (e.g. TCG -> TCATAG)
     *
     * @param position
     * @param orig
     * @param replacement
     */
    public Variation(int position, String orig, String replacement) {
        this.position = position;
        this.orig = orig;
        this.replacement = replacement;
        this.type = 2;
    }

    public String getOrig() {
        return orig;
    }

    public void setOrig(String orig) {
        this.orig = orig;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == 0) {
            return "Variation{" +
                    "position=" + position +
                    ", base=" + base +
                    '}';
        }
        if (type == 1) {
            return "Variation{" +
                    "position=" + position +
                    ", replacement=" + replacement +
                    '}';
        }
        if (type == 2) {
            return "Variation{" +
                    "position=" + position +
                    ", orig=" + orig +
                    ", replacement=" + replacement +
                    '}';
        }
        return null;
    }

    public String toStringNoPosition() {
        if (type == 0) {
            return "Variation{" +
                    "base=" + base +
                    '}';
        }
        if (type == 1) {
            return "Variation{" +
                    "replacement=" + replacement +
                    '}';
        }
        if (type == 2) {
            return "Variation{" +
                    "orig=" + orig +
                    ", replacement=" + replacement +
                    '}';
        }
        return null;
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

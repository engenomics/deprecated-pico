package org.engenomics.pico;

import Utils.Constants;

public class Variation {
    private int position;
    private char base;
    private String orig;
    private String replacement;
    private int type;

    /**
     * For SNPs
     *
     * @param position
     * @param base
     */
    public Variation(int position, char base) {
        this.position = position;
        this.base = base;
        this.type = Constants.VARIATION_TYPE_SNP;
    }

    /**
     * For SNPs
     *
     * @param base
     */
    public Variation(char base) {
        this.base = base;
        this.type = Constants.VARIATION_TYPE_SNP;
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
        this.type = Constants.VARIATION_TYPE_INSERTION;
    }

    /**
     * For insertions
     *
     * @param replacement
     */
    public Variation(String replacement) {
        this.replacement = replacement;
        this.type = Constants.VARIATION_TYPE_INSERTION;
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
        this.type = Constants.VARIATION_TYPE_DELETION;
    }

    /**
     * For deletions and/or replacements (e.g. TCG -> TCATAG)
     *
     * @param orig
     * @param replacement
     */
    public Variation(String orig, String replacement) {
        this.orig = orig;
        this.replacement = replacement;
        this.type = Constants.VARIATION_TYPE_DELETION;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variation variation = (Variation) o;

        if (position != variation.position) return false;
        if (base != variation.base) return false;
        if (type != variation.type) return false;
        if (orig != null ? !orig.equals(variation.orig) : variation.orig != null) return false;
        return replacement != null ? replacement.equals(variation.replacement) : variation.replacement == null;

    }

    @Override
    public int hashCode() {
        int result = position;
        result = 31 * result + (int) base;
        result = 31 * result + (orig != null ? orig.hashCode() : 0);
        result = 31 * result + (replacement != null ? replacement.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }

    @Override
    public String toString() {
        if (type == Constants.VARIATION_TYPE_SNP) {
            return "Variation{" +
                    "position=" + position +
                    ", base=" + base +
                    '}';
        }
        if (type == Constants.VARIATION_TYPE_INSERTION) {
            return "Variation{" +

                    "position=" + position +
                    ", replacement=" + replacement +
                    '}';
        }
        if (type == Constants.VARIATION_TYPE_DELETION) {
            return "Variation{" +
                    "position=" + position +
                    ", orig=" + orig +
                    ", replacement=" + replacement +
                    '}';
        }
        return null;
    }

    public String toStringNoPosition() {
        if (type == Constants.VARIATION_TYPE_SNP) {
            return "Variation{" +
                    "base=" + base +
                    '}';
        }
        if (type == Constants.VARIATION_TYPE_INSERTION) {
            return "Variation{" +
                    "replacement=" + replacement +
                    '}';
        }
        if (type == Constants.VARIATION_TYPE_DELETION) {
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

    public Variation cloneWithoutPosition() {
        if (type == Constants.VARIATION_TYPE_SNP) {
            return new Variation(base);
        }
        if (type == Constants.VARIATION_TYPE_INSERTION) {
            return new Variation(replacement);
        }
        if (type == Constants.VARIATION_TYPE_DELETION) {
            return new Variation(orig, replacement);
        }
        return null;
    }
}

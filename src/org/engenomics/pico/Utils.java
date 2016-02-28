package org.engenomics.pico;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Utils {
    // From http://stackoverflow.com/a/326440/2930268
    public static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    // From http://stackoverflow.com/a/326440/2930268
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    // From http://stackoverflow.com/a/2163204/2930268
    public static String removeLineBreaks(String s) {
        return s.replace(System.getProperty("line.separator"), "");
    }

    public static double getValue(char base) {
        switch (base) {
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'T':
                return 3;
            case 'G':
                return 4;
            case 'N':
                return 5;
            default:
                return -1;
        }
    }


    // From http://stackoverflow.com/a/1322548/2930268
    public static int getNextPowerOfTwo(int n) {
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;

        return n;
    }
}

package Utils;

import org.engenomics.pico.Variation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public static boolean containsVariation(List<Variation> variations, Variation v) {
        return getVariation(variations, v) != null;
    }


    public static Variation getVariation(List<Variation> variations, Variation v) {
        if (variations.size() == 0) {
            return null;
        }

        if (variations.contains(v)) {
            return variations.get(variations.indexOf(v));
        }

        if (v.getType() == Constants.VARIATION_TYPE_SNP) {
            for (Variation cVar : variations) {
                if (Objects.equals(cVar.getBase(), v.getBase())) {
                    return cVar;
                }
            }

            return null;

        } else if (v.getType() == Constants.VARIATION_TYPE_INSERTION) {
            for (Variation cVar : variations) {
                if (Objects.equals(cVar.getReplacement(), v.getReplacement())) {
                    return cVar;
                }
            }

            return null;

        } else if (v.getType() == Constants.VARIATION_TYPE_DELETION) {
            for (Variation cVar : variations) {
                if (Objects.equals(cVar.getOrig(), v.getOrig()) && Objects.equals(cVar.getReplacement(), v.getReplacement())) {
                    return cVar;
                }
            }

            return null;

        }

        return null;
    }


    // From http://stackoverflow.com/a/7147052/2930268
    private static <V, K> Map<V, K> invert(Map<K, V> map) {

        Map<V, K> inv = new HashMap<V, K>();

        for (Map.Entry<K, V> entry : map.entrySet())
            inv.put(entry.getValue(), entry.getKey());

        return inv;
    }
}

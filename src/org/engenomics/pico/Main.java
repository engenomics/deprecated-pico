package org.engenomics.pico;

import Utils.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static String DIR =
            System.getProperty("user.dir");

    public static String REL_PATH = //Genome directory (relative to current directory)
            DIR + "../../data/vcf/";

    public static String CHROMOSOME_REL_PATH = //Chromosome directory (in genome directory)
            "chr22/";

    public static String FILE_REL_PATH = //File (in chromosome directory)
            "practice2.vcf";

    public static String FILEPATH = REL_PATH + CHROMOSOME_REL_PATH + FILE_REL_PATH; //The entire file put together


//    public static int LOGBASE2INTERVAL = 4; // log base 2 of number of data points that are in each transformation complex

//    public static int INTERVAL = (int) Math.pow(2, LOGBASE2INTERVAL);

    public static void main(String[] args) throws Exception {
        //Set output
//        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//        System.setOut(out);

        new Main().run();
    }

    private void run() throws Exception {
        List<Variation> variationsSNP = new ArrayList<>();
        List<Variation> variationsInsertions = new ArrayList<>();
        List<Variation> variationsDeletions = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(FILEPATH));

        for (String line : lines) {
            String[] parts = line.split("\t");
            int position = Integer.parseInt(parts[1]);
            String original = parts[3];
            String replacement = parts[4];

            if (replacement.length() > 1) { //Not an SNP
                if (original.length() > 1) { //Deletion
                    variationsDeletions.add(new Variation(position, original, replacement));
                }

                //Otherwise, it's an insertion
                variationsInsertions.add(new Variation(position, replacement));

                continue;
            }

            char newBase = replacement.charAt(0);

            Variation currentVariation = new Variation(position, newBase);
            variationsSNP.add(currentVariation);
        }


        List<VariationType> variationTypeSNPs = makeFrequencyMap(variationsSNP);
        List<VariationType> variationTypeInsertions = makeFrequencyMap(variationsInsertions);
        List<VariationType> variationTypeDeletions = makeFrequencyMap(variationsDeletions);

//        variationTypeSNPs.forEach(System.out::println);
//        variationTypeInsertions.forEach(System.out::println);
//        variationTypeDeletions.forEach(System.out::println);
    }

    List<VariationType> makeFrequencyMap(List<Variation> variations) {
        List<VariationType> variationTypes = new ArrayList<>();

        //Make a frequency map
        Map<Variation, PositionsFrequencyPair> frequencies = new HashMap<>();

        //This loop adds a variation with frequency 1 if it's not there yet; otherwise, it increments the frequency.
        for (Variation v : variations) {
            List<Variation> currentVariations = new ArrayList<>(frequencies.keySet());

            if (Utils.containsVariation(currentVariations, v.cloneWithoutPosition())) { //Ew, I know, sorry
                Variation existingVariation = Utils.getVariation(currentVariations, v.cloneWithoutPosition());
                PositionsFrequencyPair existingPositionsFrequencyPair = frequencies.get(existingVariation);
                existingPositionsFrequencyPair.addPosition(v.getPosition());
                existingPositionsFrequencyPair.incrementFrequency();
                frequencies.put(existingVariation, existingPositionsFrequencyPair);
            } else {
                frequencies.put(v, new PositionsFrequencyPair(v.getPosition()));
            }
        }

        //Now, add all of the Variations and their frequencies into a list of VariationTypes
        for (Variation v : frequencies.keySet()) {
            variationTypes.add(new VariationType(v, frequencies.get(v).getFrequency(), frequencies.get(v).getPositions()));
        }

        //Sort the types of SNP variations
        Collections.sort(variationTypes);

        return variationTypes;
    }
}

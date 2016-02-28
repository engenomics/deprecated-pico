package org.engenomics.pico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {
    public static String DIR =
            System.getProperty("user.dir");

    public static String REL_PATH = //Genome directory (relative to current directory)
            DIR + "../../data/vcf/";

    public static String CHROMOSOME_REL_PATH = //Chromosome directory (in genome directory)
            "chr22/";

    public static String FILE_REL_PATH = //File (in chromosome directory)
            "practice.vcf";

    public static String FILEPATH = REL_PATH + CHROMOSOME_REL_PATH + FILE_REL_PATH; //The entire file put together



    private static List<Variation> variationsSNP = new ArrayList<>();
    private static List<Variation> variationsInsertions = new ArrayList<>();
    private static List<Variation> variationsDeletions = new ArrayList<>();

    private static List<Coords> singleNucleotidePolyphormismsCoords = new ArrayList<>();
    private static List<Coords> insertionCoords = new ArrayList<>();
    private static List<Coords> deletionCoords = new ArrayList<>();


//    public static int LOGBASE2INTERVAL = 4; // log base 2 of number of data points that are in each transformation complex

//    public static int INTERVAL = (int) Math.pow(2, LOGBASE2INTERVAL);

    public static void main(String[] args) throws IOException {
        //Set output
//        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//        System.setOut(out);

        new Main().run();
    }

    private void run() throws IOException {
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

        for (Variation v : variationsInsertions) {
            System.out.println(v.toString());
        }


        /////////////////////////////
        // DEALING WITH SNPS       //
        /////////////////////////////

        //TODO: Do!






        /////////////////////////////
        // DEALING WITH INSERTIONS //
        /////////////////////////////

        VariationFreqList variationFreqListOfInsertionVariations = new VariationFreqList();

        for (Variation v : variationsInsertions) {
            String replacement = v.getReplacement();

            if (variationFreqListOfInsertionVariations.containsVariation(replacement)) { //If it already exists, increment the frequency
                VariationFreq thisVariationFreq = variationFreqListOfInsertionVariations.get(variationFreqListOfInsertionVariations.indexOfVariation(replacement));

                thisVariationFreq.incrementFreq();
                thisVariationFreq.getPositions().add(v.getPosition());
            } else { //Otherwise, add it
                VariationFreq toAdd = new VariationFreq(v);
                toAdd.getPositions().add(v.getPosition());
                variationFreqListOfInsertionVariations.add(toAdd);
            }
        }

        Collections.sort(variationFreqListOfInsertionVariations);

        for (int i = 0; i < variationFreqListOfInsertionVariations.size(); i++) {
            VariationFreq v = variationFreqListOfInsertionVariations.get(i);
            v.setId(i);
        }


        /* SAVE INSERTIONS TO INDEX FILE */
        for (VariationFreq v : variationFreqListOfInsertionVariations) {
            System.out.println("SAVING TO FILE: {insertion: [id:" + v.getId() + "], [replacement:" + v.getVariation().getReplacement() + "]}");
        }

        for (Variation v : variationsDeletions) {
            System.out.println(v.toString());
        }
    }
}

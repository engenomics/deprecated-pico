package org.engenomics.pico;

import Utils.Utils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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


    public static String FILENAME = "genome22";


    public static int practicalGenomeLength;


    FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD); //There's also .UNITARY


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

        practicalGenomeLength = 0;

        List<String> linesForComparisonVCF = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(FILEPATH));

        for (String line : lines) {
            String[] parts = line.split("\t");
            int position = Integer.parseInt(parts[1]);
            String original = parts[3];
            String replacement = parts[4];

            linesForComparisonVCF.add(position + "\t" + original + "\t" + replacement);

            if (position > practicalGenomeLength) { //Basically make practicalGenomeLength equal to the latest changed position in the genome. N comparisons is more effective than storing N large numbers.
                practicalGenomeLength = position;
            }

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

        practicalGenomeLength++;

        List<VariationType> variationTypeSNPs = makeFrequencyMap(variationsSNP);
        List<VariationType> variationTypeInsertions = makeFrequencyMap(variationsInsertions);
        List<VariationType> variationTypeDeletions = makeFrequencyMap(variationsDeletions);

        Map<Integer, VariationType> variationTypeIDs = saveVariationsToMap(variationTypeSNPs, variationTypeInsertions, variationTypeDeletions);

        String picoFile = "";

        for (int i = 0; i < variationTypeIDs.size(); i++) {
            List<Integer> positions = variationTypeIDs.get(i).getPositions();
            picoFile += positions + "\n";
        }




        /* Save stuff! */

        //Save idlist
        saveVariationsToFile(variationTypeSNPs, variationTypeInsertions, variationTypeDeletions, FILENAME + ".idlist");

        //Save pico file
        save(picoFile, FILENAME + ".pico");

        //Save a comparison vcf file
        saveLinesToFile(linesForComparisonVCF, FILENAME + "comparisonVCF.txt");
    }

    private List<VariationType> makeFrequencyMap(List<Variation> variations) {
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
                frequencies.put(v.cloneWithoutPosition(), new PositionsFrequencyPair(v.getPosition()));
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

    private Map<Integer, VariationType> saveVariationsToMap(List<VariationType> snps, List<VariationType> insertions, List<VariationType> deletions) {
        Map<Integer, VariationType> variationTypeIDs = new HashMap<>();

        int id = 0; //Currently ids go from 0 to the sum of the lists' lengths. TODO: Maybe go from -lengthSum/2 to +lengthSum/2?

        for (VariationType snp : snps) {
            variationTypeIDs.put(id, snp);
            id++;
        }

        for (VariationType insertion : insertions) {
            variationTypeIDs.put(id, insertion);
            id++;
        }

        for (VariationType deletion : deletions) {
            variationTypeIDs.put(id, deletion);
            id++;
        }

        return variationTypeIDs;
    }

    private void saveVariationsToFile(List<VariationType> snps, List<VariationType> insertions, List<VariationType> deletions, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");


        int id = 0; //Currently ids go from 0 to the sum of the lists' lengths. TODO: Maybe go from -lengthSum/2 to +lengthSum/2?

        writer.println("<<<<<SNP IDs>>>>>");
        for (VariationType snp : snps) {
            writer.println(id + ":" + snp.getVariation().getBase() + ";");
            id++;
        }

        writer.println("<<<<<Insertion IDs>>>>>");
        for (VariationType insertion : insertions) {
            writer.println(id + ":" + insertion.getVariation().getReplacement() + ";");
            id++;
        }

        writer.println("<<<<<Deletion IDs>>>>>");
        for (VariationType deletion : deletions) {
            writer.println(id + ":" + deletion.getVariation().getOrig() + "|" + deletion.getVariation().getReplacement() + ";");
            id++;
        }


        writer.close();
    }

    private void save(String s, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        writer.print(s);

        writer.close();
    }

    private void saveLinesToFile(List<String> lines, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        for (String s : lines) {
            writer.println(s);
        }

        writer.close();
    }
}

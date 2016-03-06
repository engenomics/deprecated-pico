package org.engenomics.pico;

import Utils.Utils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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



        // Do all of the Fourier stuff now

        // Make an array of the variation IDs. The index of the variation in the array is the position; the value is the ID. Thus, nucleotides identical to the reference genome are recorded as -1.
        double[] variationIDs = new double[Utils.getNextPowerOfTwo(practicalGenomeLength)];
        Arrays.fill(variationIDs, -1);

        int numberOfIDs = variationTypeIDs.size();

        for (int id = 0; id < numberOfIDs; id++) {
            VariationType variationType = variationTypeIDs.get(id);
            List<Integer> positions = variationType.getPositions();

            for (Integer position : positions) {
                variationIDs[position] = id;
            }
        }


        System.out.println("Variations ready. Starting Fourier transform.");

        Complex[] result = fastFourierTransformer.transform(variationIDs, TransformType.FORWARD);

        System.out.println("Saving results to file...");




        /* SAVE STUFF TO FILES */


        // Save the list of IDs to a file in a standardized format
        saveVariationsToFile(variationTypeSNPs, variationTypeInsertions, variationTypeDeletions);

        // Save the FFT result
//        saveTransformToFile(result);

        saveValuesToFile(variationIDs);

        // Make a normal VCF file for comparison
        saveListToFile(linesForComparisonVCF);

        System.out.println("Complete!");
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

    private void saveVariationsToFile(List<VariationType> snps, List<VariationType> insertions, List<VariationType> deletions) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("genome22.idlist", "UTF-8");


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

    private void saveListToFile(List<String> values) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("comparison.vcf", "UTF-8");

        values.forEach(writer::println);

        writer.close();
    }

    private void saveValuesToFile(double[] values) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("genome22.pico", "UTF-8");

        for (double d : values) {
            if (d == -1) {
                writer.print("^");
                continue;
            }
            writer.print("<" + (int)d + ">");
        }

        writer.close();
    }

    private void saveTransformToFile(Complex[] complex) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("genome22.pico", "UTF-8");

        for (Complex c : complex) {
            writer.println(Math.round(c.getReal()) + " " + Math.round(c.getImaginary()));
        }

        writer.close();
    }
}

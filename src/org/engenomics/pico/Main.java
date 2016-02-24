package org.engenomics.pico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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



    private static List<Variation> variations = new ArrayList<>(); //Currently only SNPs


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
            String replacement = parts[4];

            if (replacement.length() > 1) { //Not an SNP
                //Skip it
                continue;
            }

            char newBase = replacement.charAt(0);

            Variation currentVariation = new Variation(position, newBase);
            variations.add(currentVariation);
        }

        for (Variation v : variations) {
            System.out.println(v.toString());
        }
    }
}

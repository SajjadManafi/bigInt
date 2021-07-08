package bigint;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class BigNum{

    private File file;

    private ArrayList<BigInt> bigInts = new ArrayList<>();
    private ArrayList<String> variablesName = new ArrayList<>();

    private Node root;


    public void setRoot(Node root) {
        this.root = root;
    }

    public BigNum(File file) {
        setFile(file);
        setRoot(null);
    }

    public BigNum(String filePath){
        File file = new File(filePath);
        setFile(file);
        setRoot(null);

    }

    private void setFile(File file) {
        this.file = file;

    }

    private File getFile() {
        return file;
    }

    private void ReadFileAndParsing() {
        try {
            Scanner scanner = new Scanner(getFile());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!scanner.hasNextLine()) {

                    // end line
                }
                else {
                    // get vars and big Ints and split and add to arrayLists
                    line = line.replaceAll("\\s+","");
                    String[] splitedLine = line.split("=");
                    variablesName.add(splitedLine[0].replaceAll("\\s+",""));
                    bigInts.add(BigInt.fromString(splitedLine[1].replaceAll("\\s+","")));

                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("404 file not found!: necessary file was not present.");
        }
    }

    private void prioritize(String s) {
        
    }
}

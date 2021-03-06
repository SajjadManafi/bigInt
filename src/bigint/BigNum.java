package bigint;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class BigNum{
    // File
    private File file;
    // array list of BigInts and variable Name
    private static ArrayList<BigInt> bigInts = new ArrayList<>();
    private static ArrayList<String> variablesName = new ArrayList<>();
    // main node -> root
    private Node root;




    public BigNum(File file) {
        setFile(file);
        setRoot(readFileAndParsing());
    }

    public BigNum(String filePath){
        File file = new File(filePath);
        setFile(file);
        setRoot(readFileAndParsing());

    }

    // setters
    public void setRoot(Node root) {
        this.root = root;
    }

    private void setFile(File file) {
        this.file = file;

    }
    // getters
    private File getFile() {
        return file;
    }

    private Node getRoot() {
        return root;
    }

    public ArrayList<BigInt> getBigInts() {
        return bigInts;
    }

    public ArrayList<String> getVariablesName() {
        return variablesName;
    }

    // solve tree
    public BigInt solve() {
        if (root.isLeaf()) {
            return root.getBigInt();
        }
        else {
            return solveBranch(root);
        }
    }
    // solve Branch
    private static BigInt solveBranch(Node node) {
        if (node.isLeaf()) return node.getBigInt();
        else {
            Node left = node.getLeft();
            BigInt leftNumber;
            if (left.isLeaf()) {
                if (left.getBigInt() == null) {
                    if (left.getContents().equals("+") || left.getContents().equals("*") || left.getContents().equals("/") || left.getContents().equals("-"))
                        return null;
                    else {
                        left.setBigInt(bigInts.get(variablesName.indexOf(left.getContents())));
                    }
                }
                leftNumber = left.getBigInt();
            }
            else
                leftNumber = solveBranch(left);

            left.setLeft(null);
            Node right = node.getRight();
            BigInt rightNumber;
            if (right.isLeaf()) {
                if (right.getBigInt() == null) {
                    if (right.getContents().equals("+") || right.getContents().equals("*") || right.getContents().equals("/") || right.getContents().equals("-"))
                        return null;
                    else {
                        right.setBigInt(bigInts.get(variablesName.indexOf(right.getContents())));
                    }
                }
                rightNumber = right.getBigInt();
            }
            else
                rightNumber = solveBranch(right);
            right.setRight(null);

            return switch (node.getContents()) {
                case "+" -> leftNumber.add(rightNumber);
                case "-" -> leftNumber.subtract(rightNumber);
                case "*" -> leftNumber.multiply(rightNumber);
                case "/" -> leftNumber.divide(rightNumber);
                default -> null;
            };




        }
    }

    // read file and parsing
    private Node readFileAndParsing() {
        try {
            Scanner scanner = new Scanner(getFile());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!scanner.hasNextLine()) {

                    // end line
                    String Line = prioritize(line);
                    ArrayList<String> phrase = BigNum.strBuilderArrToStringArr(BigNum.stringToArray(Line));
                    return Node.readTree(phrase);

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
        return null;
    }

    // String to array of StringBuilder
    private static ArrayList<StringBuilder> stringToArray(String s) {
        ArrayList<StringBuilder> phrase = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            StringBuilder temp = new StringBuilder();
            boolean isVar = false , end = false;
            while ((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z' ) || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z' )) {
                temp.append(s.charAt(i));
                isVar = true;
                if (i+1 < s.length()) i++;
                else {
                    end = true;
                    break;
                }

            }
            if (!isVar) temp.append(s.charAt(i));
            else
            if (!end) i--;
            phrase.add(temp);
        }
        return phrase;
    }

    // array of StringBuilder to array of string
    private static ArrayList<String> strBuilderArrToStringArr(ArrayList<StringBuilder> stringBuilder) {
        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < stringBuilder.size(); i++) {
            str.add(stringBuilder.get(i).toString());
        }

        return str;
    }

    // prioritize
    private static String prioritize(String s) {

        ArrayList<StringBuilder> phrase = BigNum.stringToArray(s);
        // find *
        int listSize = phrase.size();
        for (int i = 0; i < listSize; i++) {
            if (phrase.get(i).toString().equals("*")) {
                // i -> * , i - 1 -> A , i + 1 -> B
                StringBuilder temp = new StringBuilder();
                temp.append("(");
                temp.append(phrase.get(i-1).toString());
                temp.append("*");
                temp.append(phrase.get(i+1).toString());
                temp.append(")");

                phrase.set(i-1 , temp);
                phrase.remove(i);
                phrase.remove(i);
                listSize = phrase.size();
                i = -1;
            }
        }

        // find /
        listSize = phrase.size();
        for (int i = 0; i < listSize; i++) {
            if (phrase.get(i).toString().equals("/")) {
                // i -> * , i - 1 -> A , i + 1 -> B
                StringBuilder temp = new StringBuilder();
                temp.append("(");
                temp.append(phrase.get(i-1).toString());
                temp.append("/");
                temp.append(phrase.get(i+1).toString());
                temp.append(")");

                phrase.set(i-1 , temp);
                phrase.remove(i);
                phrase.remove(i);
                listSize = phrase.size();
                i = -1;
            }
        }

        // find +
        listSize = phrase.size();
        for (int i = 0; i < listSize; i++) {
            if (phrase.get(i).toString().equals("+")) {
                // i -> * , i - 1 -> A , i + 1 -> B
                StringBuilder temp = new StringBuilder();
                temp.append("(");
                temp.append(phrase.get(i-1).toString());
                temp.append("+");
                temp.append(phrase.get(i+1).toString());
                temp.append(")");

                phrase.set(i-1 , temp);
                phrase.remove(i);
                phrase.remove(i);
                listSize = phrase.size();
                i = -1;
            }
        }

        // find *
        listSize = phrase.size();
        for (int i = 0; i < listSize; i++) {
            if (phrase.get(i).toString().equals("-")) {
                // i -> * , i - 1 -> A , i + 1 -> B
                StringBuilder temp = new StringBuilder();
                temp.append("(");
                temp.append(phrase.get(i-1).toString());
                temp.append("-");
                temp.append(phrase.get(i+1).toString());
                temp.append(")");

                phrase.set(i-1 , temp);
                phrase.remove(i);
                phrase.remove(i);
                listSize = phrase.size();
                i = -1;
            }
        }
        return phrase.get(0).toString();
    }

}

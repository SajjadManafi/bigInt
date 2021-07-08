package bigint;
import java.util.ArrayList;

public class Node {
     // varName or operator
     private String contents;
     // BigInt
     private BigInt bigInt;
     // left and right Nodes
     private Node left;
     private Node right;

    boolean isLeaf(){
        return (left == null && right == null);
    }

    // Getters and setters
    public String getContents() {
        return contents;
    }

    public BigInt getBigInt() {
        return bigInt;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setBigInt(BigInt bigInt) {
        this.bigInt = bigInt;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    // String to tree
    static Node readTree(ArrayList<String> strArray) {
        Node node = new Node();

        // get next non-whitespace char
        String fCh = strArray.get(0);
        strArray.remove(0);


        if (!fCh.equals("+") && !fCh.equals("-") && !fCh.equals("/") && !fCh.equals("*") && !fCh.equals("(") && !fCh.equals(")")) {
            // leaf node
            node.contents = fCh;
            node.left = null;
            node.right = null;
        } else if (fCh.equals("(")) {
            // an expression
            node.left = readTree(strArray);
            node.contents = strArray.get(0);
            strArray.remove(0);
            node.right = readTree(strArray);
            fCh = strArray.get(0);
            strArray.remove(0);
            if (!fCh.equals(")")) {
                System.out.print("EXPECTED ) - } ASSUMED...");
                throw new IllegalArgumentException("EXPECTED ) - } ASSUMED...");
            }
        } else {
            throw new IllegalArgumentException("EXPECTED ( - CAN'T PARSE");
        }

        return node;
    }

}

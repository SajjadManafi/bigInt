package bigint;

public class Node {
    private char contents;
    private BigInt bigInt;
    private Node left;
    private Node right;

    private boolean isLeaf(){
        return (left == null && right == null);
    }


}

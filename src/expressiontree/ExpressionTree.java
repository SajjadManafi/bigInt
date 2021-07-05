package expressiontree;

import java.util.Scanner;

public class ExpressionTree {
    private class Node{
        private char contents;
        private Node left;
        private Node right;

        private boolean isLeaf(){
            return (left == null && right == null);
        }
    }

    private Node root;
    private Scanner input;

    public ExpressionTree(){
        root = null;
        input = new Scanner(System.in);
    }




}

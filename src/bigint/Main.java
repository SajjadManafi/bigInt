package bigint;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // for test...!
        File file = new File("./testFile.txt");
        BigNum bigNum = new BigNum(file);
        System.out.println(bigNum.solve());
    }
}

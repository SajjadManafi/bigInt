package bigint;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // for test...!
        File directoryPath = new File("./testFile.txt");
        BigNum bigNum = new BigNum(directoryPath);
        System.out.println(bigNum.solve());
    }
}

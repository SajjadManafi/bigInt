package bigint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class BigNum{

    private File filePath;
    private String[] variablesName;
    private BigInt[] bigInts;

    public BigNum(File filePath) {
        setFilePath(filePath);
    }

    private void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    private File getFilePath() {
        return filePath;
    }

    private void ReadFileAndParsing() {

    }
}

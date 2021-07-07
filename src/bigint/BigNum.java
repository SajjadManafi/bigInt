package bigint;

import java.io.File;

public class BigNum{

    private File file;
    private String[] variablesName;
    private BigInt[] bigInts;

    public BigNum(File file) {
        setFile(file);
    }

    public BigNum(String filePath){
        File file = new File(filePath);
        setFile(file);

    }

    private void setFile(File file) {
        this.file = file;

    }

    private File getFile() {
        return file;
    }

    private void ReadFileAndParsing() {

    }
}

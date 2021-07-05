package bignumber;

import java.util.Arrays;

public class bigInt {

    private byte[] digits;
    private sign sign;

    private bigInt(byte[] digits, sign sign) {
        setDigits(digits);
        setSign(sign);
    }

    private void setDigits(byte[] digits) {
        this.digits = digits;
    }

    private void setSign(bignumber.sign sign) {
        this.sign = sign;
    }




}

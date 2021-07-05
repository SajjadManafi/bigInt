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


    bigInt add(bigInt a){

        return bigInt.fromString("+1");
    }

    bigInt subtract(bigInt a){
        return bigInt.fromString("+1");
    }

    bigInt multyply(bigInt a){
        return bigInt.fromString("+1");
    }

    bigInt divide(bigInt a){
        return bigInt.fromString("+1");
    }

    public static bigInt fromString(String s){

        boolean hasSign = false;
        sign sign = bignumber.sign.positive;
        int StrLen = s.length();
        int remove = 1;

        if (s.charAt(0) == '+' || s.charAt(0) == '-'){
            hasSign = true;
            remove = 0;
            StrLen -= 1;

            if (s.charAt(0) == '-')
                sign = bignumber.sign.negative;
        }
        byte[] digits = new byte[StrLen];
        int j = 0;

        for (int i = 0; i < s.length(); i++) {
            if (hasSign && i == 0) continue;
            if (!Character.isDigit(s.charAt(i))) throw new IllegalArgumentException("Bad Number: " + s);
            digits[j++] = (byte) (s.charAt(s.length() -i -remove) - '0');
        }
        return new bigInt(digits , sign);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (sign == bignumber.sign.positive) str.append("+");
        else str.append("-");
        for (int i = digits.length - 1; i >= 0 ; i--) {
            str.append(digits[i]);
        }
        return str.toString();
    }



}

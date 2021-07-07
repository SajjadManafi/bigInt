package bignumber;

import java.lang.Math;
import java.util.Arrays;
import java.math.BigInteger;

public class bigInt {

    // number digits and sign
    private byte[] digits;
    private sign sign;

    // constructor
    private bigInt(byte[] digits, sign sign) {
        setDigits(digits);
        setSign(sign);
    }

    // digits setter
    private void setDigits(byte[] digits) {
        this.digits = digits;
    }

    // sign setter
    private void setSign(bignumber.sign sign) {
        this.sign = sign;
    }

    // Addition of two numbers
    bigInt add(bigInt a) {
        int temp = 0;
        int maxLen = bigInt.max(a, this).digits.length;
        byte[] sumDigits = new byte[maxLen + 1];

        int one, two, sum = 0, j = 0;

        for (int i = 0; i < maxLen; i++) {
            one = 0;
            two = 0;
            if (i < a.digits.length) one = a.digits[i];
            if (i < digits.length) two = digits[i];
            sum = one + two + temp;
            temp = 0;
            if (sum >= 10) {
                temp = 1;
                sum -= 10;
            }


            sumDigits[j++] = (byte) sum;
            if (j == sumDigits.length - 1 && temp == 1) sumDigits[j] = (byte) 1;

        }

        byte[] sumDigitsWZ = bigInt.removeZeroFromEnd(sumDigits);
        return new bigInt(sumDigitsWZ, sign);
    }

    bigInt subtract(bigInt a) {
        bigInt[] sortedBigInts = bigInt.sort(a, this);
        bigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        sign sign = bignumber.sign.positive;
        if (larger == a) sign = bignumber.sign.negative;
        byte[] resDigits = new byte[larger.digits.length];
        int one, two;
        for (int i = 0; i < larger.digits.length; i++) {
            one = larger.digits[i];
            two = 0;
            if (i < smaller.digits.length) two = smaller.digits[i];

            if (one - two < 0) {
                larger.getTempNumberIndex(i);
                one += 10;
            }

            resDigits[i] = (byte) (one - two);


        }
        byte[] resDigitsWZ = bigInt.removeZeroFromEnd(resDigits);
        return new bigInt(resDigitsWZ, sign);
    }

    bigInt multiply(bigInt a) {
        bigInt[] multiRes = new bigInt[digits.length];

        for (int i = 0; i < digits.length; i++) {
            int temp = 0, state = 0;
            byte[] resDigits = new byte[i + a.digits.length + 1];
            for (int j = 0; j < i; j++) {
                resDigits[state++] = (byte) 0;
            }
            for (int d = 0; d < a.digits.length; d++) {
                int sum = digits[i] * a.digits[d] + temp;
                temp = 0;
                int ones;

                if (sum >= 10) {
                    ones = sum % 10;
                    temp = (sum - ones) / 10;
                } else ones = sum;

                resDigits[state++] = (byte) ones;
                if (d == a.digits.length - 1) resDigits[state] = (byte) temp;
            }
            bigInt res = new bigInt(resDigits, bignumber.sign.positive);
            multiRes[i] = res;
        }
        bigInt sum = multiRes[0];
        for (int i = 1; i < multiRes.length; i++) {
            sum = sum.add(multiRes[i]);
        }
        if (a.sign != sign) sum.setSign(bignumber.sign.negative);
        else sum.sign = bignumber.sign.positive;
        return new bigInt(removeZeroFromEnd(sum.digits), sum.sign);
    }

    bigInt divide(bigInt divisor) {
        bigInt dividend = this;
        // Definition final result sign
        final sign resSign = bigInt.signDefinition(divisor.sign, dividend.sign);
        // To solve the problem of unequal signs
        dividend.setSign(bignumber.sign.positive);
        divisor.setSign(bignumber.sign.positive);
        StringBuilder quotient = new StringBuilder();
        StringBuilder tempDividend = new StringBuilder();
        // check divisor != 0 and len of dividend >= divisor
        if (dividend.compareDivisorAndDividend(divisor)) return bigInt.fromString("0");
        for (int i = dividend.digits.length - 1; i >= 0; i--) {
            tempDividend.append(Integer.toString(dividend.digits[i]));
            while (bigInt.fromString(tempDividend.toString()).compareDivisorAndDividend(divisor) && i - 1 >= 0) {
                tempDividend.append(Integer.toString(dividend.digits[--i]));
                quotient.append("0");
            }
            bigInt bigTempDividend = bigInt.fromString(tempDividend.toString());
            String tempQuotient = Integer.toString(bigTempDividend.findTempQuotient(divisor));
            quotient.append(tempQuotient);
            bigTempDividend = bigTempDividend.subtract(bigInt.fromString(tempQuotient).multiply(divisor));
            tempDividend.setLength(0);
            tempDividend.append(bigTempDividend.toString());
        }
        bigInt finalQuotient = bigInt.fromString(quotient.toString());
        finalQuotient.setDigits(bigInt.removeZeroFromEnd(finalQuotient.digits));
        finalQuotient.setSign(resSign);
        return finalQuotient;
    }

    bigInt pow(bigInt number) {
        if(number.sign == bignumber.sign.negative) throw new IllegalArgumentException("Exponent can't be negative");
        bigInt result = bigInt.fromString("1");
        bigInt tempNumber = number;
        while(tempNumber.isGreaterThanWithoutSign(bigInt.fromString("0"))){
            result = result.multiply(this);
            tempNumber = tempNumber.subtract(bigInt.fromString("1"));
        }

        return result;

    }

    bigInt mod(bigInt number) {
        return this.subtract(this.divide(number).multiply(number));
    }

    static bigInt modPow(bigInt base, bigInt exponent, bigInt number) {
        base = base.mod(number);
        bigInt result = bigInt.fromString("1");

        while (exponent.isGreaterThan(bigInt.fromString("0"))) {
            if (!exponent.isEven()) {
                result = (result.multiply(base)).mod(number);
            }

            exponent = exponent.divide(bigInt.fromString("2"));
            base = (base.multiply(base)).mod(number);
        }
        return result;
    }


    // create bigInt from String
    public static bigInt fromString(String s) {

        boolean hasSign = false;
        sign sign = bignumber.sign.positive;
        int StrLen = s.length();
        int remove = 1;

        if (s.charAt(0) == '+' || s.charAt(0) == '-') {
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
            digits[j++] = (byte) (s.charAt(s.length() - i - remove) - '0');
        }
        return new bigInt(digits, sign);
    }

    // to String
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // append sign
        if (sign != bignumber.sign.positive) str.append("-");
        // append digits
        for (int i = digits.length - 1; i >= 0; i--) {
            str.append(digits[i]);
        }
        return str.toString();
    }

    // getting a larger number between two numbers
    private static bigInt max(bigInt a, bigInt b) {
        if (a.digits.length >= b.digits.length) return a;
        else return b;
    }

    // getting a smaller number between two numbers
    private static bigInt min(bigInt a, bigInt b) {
        if (a.digits.length <= b.digits.length) return a;
        else return b;
    }

    // sort larger to smaller
    private static bigInt[] sort(bigInt a, bigInt b) {
        bigInt[] sortedBigInts = new bigInt[2];
        sortedBigInts[0] = a;
        sortedBigInts[1] = b;
        if (a.digits.length > b.digits.length) {
            // nothing
        } else if (b.digits.length > a.digits.length) {
            sortedBigInts[0] = b;
            sortedBigInts[1] = a;
        } else {

            for (int i = a.digits.length - 1; i >= 0; i--) {
                if ((int) a.digits[i] > (int) b.digits[i]) {
                    break;
                } else if ((int) a.digits[i] < (int) b.digits[i]) {
                    sortedBigInts[0] = b;
                    sortedBigInts[1] = a;
                    break;
                }
            }
        }
        return sortedBigInts;
    }

    private void getTempNumberIndex(int currentState) {
        for (int i = currentState + 1; i < digits.length; i++) {
            if ((int) digits[i] > 0) {
                digits[i] -= 1;
                for (int j = i - 1; j > currentState; j--) {
                    digits[j] += 9;
                }
                break;
            }
        }

    }

    private static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    private static byte[] removeZeroFromEnd(byte[] array) {
        int index = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            if ((int) array[i] > 0) {
                index = i + 1;
                break;
            }
        }
        byte[] arrayWithoutZero = new byte[index];
        arrayWithoutZero = Arrays.copyOfRange(array, 0, index);
        return arrayWithoutZero;
    }

    private static bignumber.sign signDefinition(bignumber.sign sign1, bignumber.sign sign2) {
        if (sign1 != sign2) return bignumber.sign.negative;
        else return bignumber.sign.positive;
    }

    private boolean compareDivisorAndDividend(bigInt divisor) {
        bigInt[] sortedBigInts = bigInt.sort(this, divisor);
        bigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        if (!this.equals(larger)) return true;
        if (divisor.equals(bigInt.fromString("0"))) throw new IllegalArgumentException("Argument 'divisor' is 0");
        return false;

    }


    private int findTempQuotient(bigInt divisor) {
        // this -> tempDividend
        int i = 1;
        bigInt multiplyRes = divisor.multiply(bigInt.fromString(Integer.toString(i)));
        bigInt[] sortedBigInts = bigInt.sort(this, multiplyRes);
        while (this.equals(sortedBigInts[0])) {
            i++;
            multiplyRes = divisor.multiply(bigInt.fromString(Integer.toString(i)));
            sortedBigInts = bigInt.sort(this, multiplyRes);
        }

        return i - 1;
    }

    private boolean isGreaterThanWithoutSign(bigInt secondNumber){
        if (Arrays.equals(this.digits , secondNumber.digits)) return false;
        bigInt[] sortedBigInts = bigInt.sort(this, secondNumber);
        bigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        bigInt templarger = larger;
        larger.setSign(bignumber.sign.positive);
        smaller.setSign(bignumber.sign.positive);
        if (larger.equals(smaller)) return false;
        else if (this.equals(templarger)) return true;
        else return false;

    }
    private boolean isGreaterThan(bigInt secondNumber) {
        if (Arrays.equals(this.digits , secondNumber.digits)) return false;
        else if (this.sign == bignumber.sign.positive && secondNumber.sign == bignumber.sign.negative && !this.equals(bigInt.fromString("0")) && !secondNumber.equals(bigInt.fromString("0")))
            return true;
        else if (this.sign == bignumber.sign.negative && secondNumber.sign == bignumber.sign.positive && !this.equals(bigInt.fromString("0")) && !secondNumber.equals(bigInt.fromString("0")))
            return false;
        else return this.isGreaterThanWithoutSign(secondNumber);
    }

    private boolean isEven() {
        return digits[0] == (byte) 0 || digits[0] == (byte) 2 || digits[0] == (byte) 4 || digits[0] == (byte) 6 || digits[0] == (byte) 8;
    }


    public static void main(String[] args) {
        bigInt a = bigInt.fromString("473246187432742884343324823");
        bigInt b = bigInt.fromString("293482384327647323824343434");
        bigInt c = bigInt.fromString("-1388894194617300681343434342594984656586758653897526947358905820952785270794138889419461730068178527079418521936494040048569262182");
        bigInt d = bigInt.fromString("-29348235252389584327647323824343434");


        bigInt base = bigInt.fromString("1994");
        bigInt exponent = bigInt.fromString("30");
        bigInt MODULO = bigInt.fromString("20");
        bigInt res = bigInt.modPow(base , exponent , MODULO);

//        bigInt sum = c.add(d);
//        bigInt alo = b.subtract(a);
//        bigInt zarb = c.divide(d);
//        bigInt Tavan = d.pow(f);
//        System.out.println(sum);
//        System.out.println(alo);
//        System.out.println(zarb);
//        System.out.println(Tavan);
        System.out.println(res);
    }


}
package bignumber;

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
        return new bigInt(sumDigits, sign);
    }

    bigInt subtract(bigInt a) {
        bigInt[] sortedBigInts = bigInt.sort(a, this);
        bigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        sign sign = bignumber.sign.positive;
        if (larger == a) sign = bignumber.sign.negative;
        byte[] resDigits = new byte[larger.digits.length];
        int one, two;
        for (int i  = 0; i < larger.digits.length; i++) {
            one = larger.digits[i];
            two = 0;
            if (i < smaller.digits.length) two = smaller.digits[i];

            if (one - two < 0) {
                larger.getTempNumberIndex(i);
                one += 10;
            }

            resDigits[i] = (byte)(one - two);


        }
        return new bigInt(resDigits , sign);
    }

    bigInt multiply(bigInt a) {
        bigInt[] multysRes = new bigInt[digits.length];

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
                    temp = ( sum - ones ) / 10;
                }
                else ones = sum;

                resDigits[state++] = (byte) ones;
                if (d == a.digits.length -1) resDigits[state] = (byte) temp;
            }
            bigInt res = new bigInt(resDigits , bignumber.sign.positive);
            multysRes[i] = res;
        }
        bigInt sum = multysRes[0];
        for (int i = 1; i < multysRes.length; i++) {
            sum = sum.add(multysRes[i]);
        }
        if (a.sign != sign) sum.setSign(bignumber.sign.negative);
        else sum.sign = bignumber.sign.positive;

        return sum;
    }

    bigInt divide(bigInt a) {
        return bigInt.fromString("+1");
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
        if (sign == bignumber.sign.positive) str.append("+");
        else str.append("-");
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

            for (int i = a.digits.length -1 ; i >= 0; i--) {
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

    private void getTempNumberIndex(int currentState){
        for (int i = currentState + 1; i < digits.length; i++) {
            if ((int) digits[i] > 0) {
                digits[i] -= 1;
                for (int j = i-1; j > currentState; j--) {
                    digits[j] += 9;
                }
                break;
            }
        }

    }


    public static void main(String[] args) {
        bigInt a = bigInt.fromString("473246187432742884343324823");
        bigInt b = bigInt.fromString("293482384327647323824343434");
        bigInt c = bigInt.fromString("473246187432742884343324823");
        bigInt d = bigInt.fromString("-293482384327647323824343434");

        bigInt sum = a.add(b);
        bigInt alo = b.subtract(a);
        bigInt zarb = c.multiply(d);
        System.out.println(sum);
        System.out.println(alo);
        System.out.println(zarb);
    }


}

package bigint;
import java.util.Arrays;



public class BigInt{

    // number digits and sign
    private byte[] digits;
    private sign sign;

    // constructor
    private BigInt(byte[] digits, sign sign) {
        setDigits(digits);
        setSign(sign);
    }

    // digits setter
    private void setDigits(byte[] digits) {
        this.digits = digits;
    }

    // sign setter
    private void setSign(bigint.sign sign) {
        this.sign = sign;
    }

    // Addition of two numbers
    BigInt add(BigInt a) {
        int temp = 0;
        int maxLen = BigInt.max(a, this).digits.length;
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

        byte[] sumDigitsWZ = BigInt.removeZeroFromEnd(sumDigits);
        return new BigInt(sumDigitsWZ, sign);
    }

    BigInt subtract(BigInt a) {
        BigInt[] sortedBigInts = BigInt.sort(a, this);
        BigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        sign sign = bigint.sign.positive;
        if (larger == a) sign = bigint.sign.negative;
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
        byte[] resDigitsWZ = BigInt.removeZeroFromEnd(resDigits);
        return new BigInt(resDigitsWZ, sign);
    }

    BigInt multiply(BigInt a) {
        BigInt[] multiRes = new BigInt[digits.length];

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
            BigInt res = new BigInt(resDigits, bigint.sign.positive);
            multiRes[i] = res;
        }
        BigInt sum = multiRes[0];
        for (int i = 1; i < multiRes.length; i++) {
            sum = sum.add(multiRes[i]);
        }
        if (a.sign != sign) sum.setSign(bigint.sign.negative);
        else sum.sign = bigint.sign.positive;
        return new BigInt(removeZeroFromEnd(sum.digits), sum.sign);
    }

    BigInt divide(BigInt divisor) {
        BigInt dividend = this;
        // Definition final result sign
        final sign resSign = BigInt.signDefinition(divisor.sign, dividend.sign);
        // To solve the problem of unequal signs
        dividend.setSign(bigint.sign.positive);
        divisor.setSign(bigint.sign.positive);
        StringBuilder quotient = new StringBuilder();
        StringBuilder tempDividend = new StringBuilder();
        // check divisor != 0 and len of dividend >= divisor
        if (dividend.compareDivisorAndDividend(divisor)) return BigInt.fromString("0");
        for (int i = dividend.digits.length - 1; i >= 0; i--) {
            tempDividend.append(Integer.toString(dividend.digits[i]));
            while (BigInt.fromString(tempDividend.toString()).compareDivisorAndDividend(divisor) && i - 1 >= 0) {
                tempDividend.append(Integer.toString(dividend.digits[--i]));
                quotient.append("0");
            }
            BigInt bigTempDividend = BigInt.fromString(tempDividend.toString());
            String tempQuotient = Integer.toString(bigTempDividend.findTempQuotient(divisor));
            quotient.append(tempQuotient);
            bigTempDividend = bigTempDividend.subtract(BigInt.fromString(tempQuotient).multiply(divisor));
            tempDividend.setLength(0);
            tempDividend.append(bigTempDividend.toString());
        }
        BigInt finalQuotient = BigInt.fromString(quotient.toString());
        finalQuotient.setDigits(BigInt.removeZeroFromEnd(finalQuotient.digits));
        finalQuotient.setSign(resSign);
        return finalQuotient;
    }

    BigInt pow(BigInt number) {
        if(number.sign == bigint.sign.negative) throw new IllegalArgumentException("Exponent can't be negative");
        BigInt result = BigInt.fromString("1");
        BigInt tempNumber = number;
        while(tempNumber.isGreaterThanWithoutSign(BigInt.fromString("0"))){
            result = result.multiply(this);
            tempNumber = tempNumber.subtract(BigInt.fromString("1"));
        }

        return result;

    }

    BigInt mod(BigInt number) {
        return this.subtract(this.divide(number).multiply(number));
    }

    static BigInt modPow(BigInt base, BigInt exponent, BigInt number) {
        base = base.mod(number);
        BigInt result = BigInt.fromString("1");

        while (exponent.isGreaterThan(BigInt.fromString("0"))) {
            if (!exponent.isEven()) {
                result = (result.multiply(base)).mod(number);
            }

            exponent = exponent.divide(BigInt.fromString("2"));
            base = (base.multiply(base)).mod(number);
        }
        return result;
    }


    // create bigInt from String
    public static BigInt fromString(String s) {

        boolean hasSign = false;
        sign sign = bigint.sign.positive;
        int StrLen = s.length();
        int remove = 1;

        if (s.charAt(0) == '+' || s.charAt(0) == '-') {
            hasSign = true;
            remove = 0;
            StrLen -= 1;

            if (s.charAt(0) == '-')
                sign = bigint.sign.negative;
        }
        byte[] digits = new byte[StrLen];
        int j = 0;

        for (int i = 0; i < s.length(); i++) {
            if (hasSign && i == 0) continue;
            if (!Character.isDigit(s.charAt(i))) throw new IllegalArgumentException("Bad Number: " + s);
            digits[j++] = (byte) (s.charAt(s.length() - i - remove) - '0');
        }
        return new BigInt(digits, sign);
    }

    // to String
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // append sign
        if (sign != bigint.sign.positive) str.append("-");
        // append digits
        for (int i = digits.length - 1; i >= 0; i--) {
            str.append(digits[i]);
        }
        return str.toString();
    }

    // getting a larger number between two numbers
    private static BigInt max(BigInt a, BigInt b) {
        if (a.digits.length >= b.digits.length) return a;
        else return b;
    }

    // getting a smaller number between two numbers
    private static BigInt min(BigInt a, BigInt b) {
        if (a.digits.length <= b.digits.length) return a;
        else return b;
    }

    // sort larger to smaller
    private static BigInt[] sort(BigInt a, BigInt b) {
        BigInt[] sortedBigInts = new BigInt[2];
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

    private static bigint.sign signDefinition(bigint.sign sign1, bigint.sign sign2) {
        if (sign1 != sign2) return bigint.sign.negative;
        else return bigint.sign.positive;
    }

    private boolean compareDivisorAndDividend(BigInt divisor) {
        BigInt[] sortedBigInts = BigInt.sort(this, divisor);
        BigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        if (!this.equals(larger)) return true;
        if (divisor.equals(BigInt.fromString("0"))) throw new IllegalArgumentException("Argument 'divisor' is 0");
        return false;

    }


    private int findTempQuotient(BigInt divisor) {
        // this -> tempDividend
        int i = 1;
        BigInt multiplyRes = divisor.multiply(BigInt.fromString(Integer.toString(i)));
        BigInt[] sortedBigInts = BigInt.sort(this, multiplyRes);
        while (this.equals(sortedBigInts[0])) {
            i++;
            multiplyRes = divisor.multiply(BigInt.fromString(Integer.toString(i)));
            sortedBigInts = BigInt.sort(this, multiplyRes);
        }

        return i - 1;
    }

    private boolean isGreaterThanWithoutSign(BigInt secondNumber){
        if (Arrays.equals(this.digits , secondNumber.digits)) return false;
        BigInt[] sortedBigInts = BigInt.sort(this, secondNumber);
        BigInt larger = sortedBigInts[0], smaller = sortedBigInts[1];
        BigInt templarger = larger;
        larger.setSign(bigint.sign.positive);
        smaller.setSign(bigint.sign.positive);
        if (larger.equals(smaller)) return false;
        else if (this.equals(templarger)) return true;
        else return false;

    }
    private boolean isGreaterThan(BigInt secondNumber) {
        if (Arrays.equals(this.digits , secondNumber.digits)) return false;
        else if (this.sign == bigint.sign.positive && secondNumber.sign == bigint.sign.negative && !this.equals(BigInt.fromString("0")) && !secondNumber.equals(BigInt.fromString("0")))
            return true;
        else if (this.sign == bigint.sign.negative && secondNumber.sign == bigint.sign.positive && !this.equals(BigInt.fromString("0")) && !secondNumber.equals(BigInt.fromString("0")))
            return false;
        else return this.isGreaterThanWithoutSign(secondNumber);
    }

    private boolean isEven() {
        return digits[0] == (byte) 0 || digits[0] == (byte) 2 || digits[0] == (byte) 4 || digits[0] == (byte) 6 || digits[0] == (byte) 8;
    }


    public static void main(String[] args) {
        BigInt a = BigInt.fromString("473246187432742884343324823");
        BigInt b = BigInt.fromString("293482384327647323824343434");
        BigInt c = BigInt.fromString("-1388894194617300681343434342594984656586758653897526947358905820952785270794138889419461730068178527079418521936494040048569262182");
        BigInt d = BigInt.fromString("-29348235252389584327647323824343434");


        BigInt base = BigInt.fromString("1994");
        BigInt exponent = BigInt.fromString("30");
        BigInt MODULO = BigInt.fromString("20");
        BigInt res = BigInt.modPow(base , exponent , MODULO);

        BigInt sum = c.add(d);
        BigInt alo = b.subtract(a);
        BigInt zarb = c.divide(d);
//        BigInt Tavan = d.pow(f);
        System.out.println(sum);
        System.out.println(alo);
        System.out.println(zarb);
//        System.out.println(Tavan);
        System.out.println(res);
    }


}

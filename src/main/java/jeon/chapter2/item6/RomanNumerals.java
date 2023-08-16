package jeon.chapter2.item6;

import java.util.regex.Pattern;

public class RomanNumerals {
 
    private static final Pattern ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})" +
            "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})");
    //compile : 정규표현식을 컴파일하여 'pattern'객체를 생성하고 정규표현식의 유효성도 검사
    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }

    public static void main(String[] args) {
        String[] testStrings = {"III", "IV", "IX", "LVIII", "MCMXCIV", "ABC", "100"};

        for (String test : testStrings) {
            System.out.println(test + " is a Roman numeral? " + isRomanNumeral(test));
        }
    }
    
}

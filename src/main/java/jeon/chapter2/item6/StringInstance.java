package jeon.chapter2.item6;

public class StringInstance {

    String str1 = "Effective Java";
    String str2 = "Effective Java";
    String str3 = new String("Effective Java");
    String str4 = new String("Effective Java");

    public void checkStrings() {
        if(str1 == str2) {
            System.out.println("str1과 str2는 동일");
        } else {
            System.out.println("str1과 str2는 동일하지 않음");
        }

        if(str3 == str4) {
            System.out.println("str3과 str4는 동일");
        } else {
            System.out.println("str3과 str4는 동일하지 않음");
        }
    }

    public static void main(String[] args) {
        StringInstance instance = new StringInstance();
        instance.checkStrings();
    }
}

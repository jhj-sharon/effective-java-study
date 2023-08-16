package jeon.chapter2.item2;

public class Person {
    private final String name;
    private final int age;
    private final String address;
    private final String phone;

    public Person(String name) {
        this(name, 0);
    }

    public Person(String name, int age) {
        this(name, age, "unknown");
    }

    public Person(String name, int age, String address) {
        this(name, age, address, "unknown");
    }

    public Person(String name, int age, String address, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public static void main(String[] args) {


        // 모든 속성을 설정하여 객체 생성
        Person person4 = new Person("sharon", 25, "서울시 중랑구", "555-1234");
    }
}

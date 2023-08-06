package jeon.chapter1.item2;

public class Person2 {
    private String name;
    private int age;

    // 기본 생성자
    public Person2() { }

    // name의 getter와 setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // age의 getter와 setter
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        // 객체 생성
        Person2 person = new Person2();

        // 속성 설정
        person.setName("sharon");
        person.setAge(25);

        // 속성 출력 (옵션)
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());
    }

}


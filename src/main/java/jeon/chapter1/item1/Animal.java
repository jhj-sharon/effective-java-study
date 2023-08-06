package jeon.chapter1.item1;

public interface Animal {

    void sound();

    // 정적 팩토리 메서드
    static Animal createDog() {
        return new Dog();
    }

    static Animal createCat() {
        return new Cat();
    }
}
// 정적 팩토리 메서드를 사용하면 클라이언트 코드에서는 Animal 인터페이스만 알면 되고,
// 구체적인 구현 클래스(Dog, Cat)에 대한 세부 사항은 숨길 수 있다.
class Dog implements Animal {
    @Override
    public void sound() {
        System.out.println("Woof!");
    }
}

class Cat implements Animal {
    @Override
    public void sound() {
        System.out.println("Meow!");
    }
}

// AnimalFactory 클래스는 createAnimal라는 정적 메서드를 제공.
// 문자열 매개변수 type을 받아, 해당 타입에 따라 Dog 또는 Cat 객체를 반환
class AnimalFactory {
    public static Animal createAnimal(String type) {
        if ("dog".equalsIgnoreCase(type)) {
            return new Dog();
        } else if ("cat".equalsIgnoreCase(type)) {
            return new Cat();
        }
        throw new IllegalArgumentException("Unknown animal type: " + type);
    }
}


# **아이템5: 자원을 명시하지 말고 의존 객체 주입을 사용하라**

> 이전에 살펴본 정적 유틸리티 클래스나 싱글턴 방식은 사용하는 자원에 따라 동작이 달라지는 클래스에는 적합하지 않다. 정적 유틸리티 클래스는 상태를 가질 수 없다. 즉 자원에 따라 동작을 변경하는 것이 어렵다. 싱글턴은 전역 상태를 가지게 되므로 여러 자원을 관리하거나 동작을 동적으로 변경하기 어렵게 만든다. 또한 정적 유틸리티 클래스나 싱글턴은 상속을 통한 확장이 어렵고 테스트도 어렵다는 단점이 있다. 따라서 ①클래스가 여러 자원 인스턴스를 지원해야하고, ② 클라이언트가 원하는 자원을 사용해야하는 조건을 만족하려면 **인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식인 의존 객체 주입**이 적합하다.

<br>

### **1\. 의존 객체 주입(Dependency Injection, DI)이란?**

-   의존 객체 주입이란 객체의 의존성을 외부에서 주입해주는 것이다. 이로 인해 객체는 자신의 의존성을 직접 생성하거나 관리할 필요가 없게 되며, 테스트와 재사용성이 향상된다.
-   이러한 방식은 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 작동한다. 또한 불변을 보장하여 같은 자원으로 사용하려는 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있기도 하다.
-   의존 객체 주입은 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다.

<br>


### **2\. 의존 객체 주입(Dependency Injection, DI) 예시**

**1) 모든 데이터베이스 연결을 위한 공통 인터페이스를 정의한다.**

```java
public interface DatabaseConnector {
    void connect();
    void save(String data);
}
```

**2) 다양한 인터페이스 연결을 위한 구현 클래스**

```java
public class MySQLConnector implements DatabaseConnector {
    @Override
    public void connect() {
        // MySQL에 연결하는 코드
    }

    @Override
    public void save(String data) {
        // 데이터를 MySQL에 저장하는 코드
    }
}

public class PostgreSQLConnector implements DatabaseConnector {
    @Override
    public void connect() {
        // PostgreSQL에 연결하는 코드
    }

    @Override
    public void save(String data) {
        // 데이터를 PostgreSQL에 저장하는 코드
    }
}
```

<br>

**3) 데이터를 저장하는 클라이언트 클래스 정의**

-   이 클라이언트 클래스는 생성자를 통해 **DatabaseConnector** 구현체를 주입받는다.

```java
public class DataSaver {
    private DatabaseConnector db;

    public DataSaver(DatabaseConnector dbConnector) {
        this.db = dbConnector;
    }

    public void saveData(String data) {
        db.connect();
        db.save(data);
    }
}
```
<br>


**4) 필요한 데이터베이스 연결을 선택해서 객체를 생성하고 사용**

```java
public class Main {
    public static void main(String[] args) {
        DataSaver mysqlSaver = new DataSaver(new MySQLConnector());
        DataSaver postgresSaver = new DataSaver(new PostgreSQLConnector());

        mysqlSaver.saveData("someDataForMySQL");
        postgresSaver.saveData("someDataForPostgreSQL");
    }
}
```
<br>

### **3\. 팩터리 메서드 패턴(Factory Method pattern)과 Supplier<T>**

**1) 팩터리 메서드 패턴**

-   **팩터리 메서드 패턴은 객체 생성에 관한 책임을 서브클래스로 위임하는 디자인 패턴**이다. 이 패턴은 인터페이스를 통해 객체를 생성하지만, 객체 생성의 실제 구현은 서브클래스에 위임되어 있다.
-   팩터리란 호출할 때마다 특정타입의 인스턴스를 반복해서 만들어주는 객체이다.

> \*\*장점   **   \- 클라이언트 코드는 생성되는 객체의 구체적인 클래스를 알 필요가 없다.   \- 객체 생성 로직을 중앙화하여 코드 중복을 줄일 수 있다.   \- 확장성이 좋아진다. 새로운 객체 유형을 추가하려면 기존 코드를 변경하지 않고 새로운 클래스를 추가하기만 하면 된다.      

<br>

**2) Java8 Supplier<T>**

-   Supplier<T>는 Java8에서 도입된 함수형 인터페이스 중 하나로, 주로 객체 생성이나 연산 결과를 제공하는 데 사용된다. 
-   Supplier는 매개변수를 받지 않고 단순히 무엇인가를 반환하는 추상메서드가 존재한다, 인터페이스의 정의는 다음과 같다.
-   @FunctionalInterface는 해당 인터페이스가 정확히 하나의 추상 메서드만을 가지고 있음을 나타낸다. 이로 인해 람다 표현식과 함께 사용할 수 있게 된다.

```
@FunctionalInterface
public interface Supplier<T> {
    T get();// 매개변수가 없고, 단순히 무엇인가를 반환할 때
}
```

<br>

**2) **팩터리 메서드 패턴과**  Java8 Supplier<T>**

-   Java 8의 Supplier<T> 인터페이스를 사용하면 팩터리 메서드 패턴을 더 유연하고 간결하게 구현할 수 있다.
-   예를 들어, 여러 유형의 동물을 생성하는 팩터리를 생각해보자.
-   아래 코드에서, createAnimal 메서드는 Supplier<Animal>을 매개변수로 받아 해당 공급자를 통해 동물 인스턴스를 생성한다. Dog::new 및 Cat::new는 메서드 참조를 사용하여 해당 클래스의 생성자를 참조한다.

```java
public interface Animal {
    void speak();
}

public class Dog implements Animal {
    @Override
    public void speak() {
        System.out.println("Woof!");
    }
}

public class Cat implements Animal {
    @Override
    public void speak() {
        System.out.println("Meow!");
    }
}

public class AnimalFactory {

    public static Animal createAnimal(Supplier<Animal> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        Animal dog = createAnimal(Dog::new);
        dog.speak();

        Animal cat = createAnimal(Cat::new);
        cat.speak();
    }
}
```

<br>

-   **한정적 와일드카드 타입**은 **Java의 제네릭을 사용할 때 특정 타입의 서브타입만을 허용하도록 제한하는 기능**이다. 이를 통해 제네릭 타입의 유연성을 높일 수 있다.
-   Supplier<T>를 입력으로 받는 메서드에서 한정적 와일드카드 타입을 사용하면, 특정 타입의 서브타입만을 생성하는 Supplier를 받아들일 수 있게 된다.
-   모든 동물을 생성할 수 있는 팩터리가 아니라, 오직 Dog만을 생성할 수 있는 팩터리를 원한다면 다음과 같이 한정적 와일드카드 타입을 사용할 수 있다.

```java
public Animal createDogSupplier(Supplier<? extends Dog> dogSupplier) {
    return dogSupplier.get();
}
```
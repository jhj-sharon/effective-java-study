
## **아이템3: private 생성자나 열거 타입으로 싱글턴임을 보증하라**
<Br>

> 싱글턴(sigleton)이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다. 싱글턴의 전형적인 예로는 함수와 같은 무상태 객체나 설계상 유일해야하는 시스템 컴포넌트를 들 수 있다. 그런데 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다. 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면 싱글턴 인스턴스를 가짜(mock)구현으로 대체할 수 없기 때문이다. 즉, 클래스를 싱글턴으로 만들 경우 해당 클래스의 인스턴스가 시스템 전체에 하나이기 때문에 테스트와 실제 환경에서의 동작이 강하게 결합될 수 있다.  
> 싱글턴을 만드는 방법은 보통 두가지이다. **두 방식 모두 생성자는 private으로 감춰두고 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 만들어 둔다.**
<Br>

<Br>

###  **1. public static final 필드 방식의 싱글턴**

**1) 특징 및 형식**

**(1) Private 생성자** 

-   생성자를 'private'으로 선언하여 외부에서 이 클래스의 인스턴스를 생성할 수 없게 한다.
-   public이나 protected생성자가 없으므로 생성자가 초기화될 대 만들어진 인스턴스가 전체  시스템에서 하나뿐임이 보장된다.
-   **예외적으로 권한이 있는 클라이언트는 리플렉션  API인 AccesibleObject.setAccessible을 사용해 priavate 생성자를 호출 할 수 있다.** 이러한 공격을 방어하려면 생성자를 수정하여 두 번 째 객체가 생성되려할 때 예외를 던지게하면 된다.

' AccessibleObject.setAccessible는 Java의 리플렉션 API에 속하는 메서드이다.리플렉션 API는 프로그램 실행 중에 (런타임에서) Java 클래스의 내부 구조를 검사하거나 수정할 수 있게 해주는 강력한 기능들을 제공한다.   AccessibleObject.setAccessible 메서드는 Field, Method, 그리고 Constructor와 같은 리플렉션 객체의 접근 제어를 우회할 수 있다. '

<Br>

**(2) Public static final  필드**

-   'public static final'로 선언된 'INSTANCE' 필드는 클래스 로드 시 한 번만 초기화 된다. 이 필드는 이 클래스의 유일한 인스턴스를 참조한다.

**(3) Static 초기화**

-   'INSTANCE'는 정적 초기화 블록에서 초기화된다(정적 초기화 블록 static intitialization block은 클래스가 JVM에 의해 처음 로드될 때 한 번만 실행된다.).
-   Java는 정적 초기화가 스레드 안전(Thread-safety)하게 수행되도록 보장 하므로 멀티스레드 환경에서도 안전하다. 즉, 싱글턴 패턴에서는 클래스 로딩 및 초기화 과정을 동기화 하는 JVM의 동작 방식을 활용하기 때문에 인스턴스의 스레드 한전한 초기화를 보장한다.

<Br>

```
import java.lang.reflect.Constructor;

public class Singleton {

    // 자기 자신의 인스턴스를 static final 필드로 생성
    public static final Singleton INSTANCE = new Singleton();
    private static boolean isInstanceCreated = false;

    // private 생성자로 외부에서 인스턴스 생성을 방지
    private Singleton() {
        if (isInstanceCreated) {
            throw new IllegalStateException("Instance already created!");
        }
        isInstanceCreated = true;
    }

    // 필요한 비즈니스 메서드
    public void someMethod() {
        // ...
    }
    
    public class ReflectionAttack {
    public static void main(String[] args) throws Exception {
        // 정상적인 방법으로 싱글턴 인스턴스 획득
        Singleton instance1 = Singleton.INSTANCE;

        // 리플렉션을 사용한 공격
        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true); // private 생성자의 접근 제어 우회
        Singleton instance2 = constructor.newInstance();

        System.out.println(instance1 == instance2); // false
    }
}


//사용방법
Singleton singleton = Singleton.INSTANCE;
singleton.someMethod();
```

**2) 장점**

-   해당 클래스가 싱글턴임이 API에 명확하게 드러난다. public static 필드가 final이므로 절대로 다름 객체를 참조할 수 없다.
-   구현이 간단하다.
-   JVM의 클래스 로딩 메커니즘을 활용하여 스레드 안전성을 보장한다.

**3) 단점**

-   싱글턴 인스턴스가 사용되지 않더라도 클래스가 로드될 때 생성된다. (즉시 로딩, Eager Loading)
-   인스턴스 생성 로직을 변경하거나, 초기화에 추가 작업이 필요한 경우 구현이 복잡해질 수 있다.

<Br>

###  **2. 정적 팩터리 방식의 싱글턴**

#### **1) 특징 및 형식**

**(1) Private 생성자 & **(2) Public static final  필드 동일****

**(3) Public static 메서드**

-   외부에서 이 메서드를 통해 싱글턴 인스턴스에 접근할 수 있다. 처음 호출될 때 싱글턴 인스턴스를 초기화하거나 이미 초기화된 인스턴스를 반환한다.

```
public class Singleton {
    // Private static instance
    private static final Singleton INSTANCE = new Singleton();

    // Private constructor
    private Singleton() {
        // Initialization logic
    }

    // Public static factory method
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    
    // 이 메서드를 추가하여 역직렬화 시 싱글턴 인스턴스를 반환.
    private Object readResolve() {
        return INSTANCE;
    }
}


//사용방법
Singleton singleton = Singleton.getInstance();
```


<Br>

**2) 장점**

-   싱글턴 인스턴스를 반환하는 메서드의 이름을 자유롭게 지정할 수 있다.
-   나중에 싱글턴이 아니게 변경하더라도 API를 변경하지 않고 구현만 변경할 수 있다. 유일한 인스턴스를 반환하던 팩터리 메서드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있다.
-   정적 팩터리 방식을 사용하면, 제너릭 싱글턴을 만들 수 있다.
-   정적 팩터리의 **메서드 참조를 공급자(supplier)**로 사용할 수 있다. 이러한 방식으로, 메서드 참조와 공급자를 함께 사용하면 기존 메서드의 로직을 재사용하면서 함수형 프로그래밍 스타일을 적용할 수 있다.

' **\- 메서드 참조(Method Reference)**   : 람다 표현식의 또 다른 간단한 형태. 기존 메서드의 동작을 재사용하거나 특정 메서드를 함수형 인터페이스의 구현으로 참조하고 싶을 때 사용한다. 메서드 참조는 '::' 연산자를 사용하여 표현한다.      
**\- 공급자(Supplier)**   : 공급자는 매개변수를 받지 않고 값을 반환하는 'get()' 메서드를 제공한다. '

```
Supplier<Singleton> singletonSupplier = Singleton::getInstance;

// 이제 supplier를 사용하여 Singleton 인스턴스를 얻을 수 있다.
Singleton singleton = singletonSupplier.get();
```

<Br>

## **\*\* 싱글턴의 직렬화(Serializable)**


-   싱글턴 클래스가 'Serializable'인터페이스를 구현하면, 객체의 직렬화 및 역직렬화를 통해 새로운 인스턴스를 생성할 수 있다. 이로인해 싱글턴의 기본원칙이 깨지게 된다. 
-   위에서 소개한 둘 중 하나의 방식으로 만든 싱글턴 클래스를 직렬화하려면 단순히 Serializable을 선언하는 것만으로는 부족하다. 모든 인스턴스 필드를 일시적(transient)이라고 선언해야하고 readResolve 메서드를 제공해야한다.
-   readResolve()메서드는 역직렬화 시 싱글턴 인스턴스를 반환하도록 보장할 수 있다. 실제로 readResolve()는 역직렬화 중에 호출되어 대체할 객체를 반환한다.

<Br>

### **3. 열거 타입 방식의 싱글턴 - 바람직한 방법**

<Br>

#### **1) 특징 및 형식**

-   열거 타입 방식의 싱글턴은 싱글턴을 구현하는 가장 바람직한 방법 중에 하나이다. public 필드 방법과 비슷하지만 더 간결하고 추가 노력없이 직렬화 할 수 있다. 
-   또한 리플렉션을 통한 공격에도 안전하다.

```
public enum Singleton {
    INSTANCE;

    public void someMethod() {
        // 싱글턴 객체의 동작을 정의
    }
}

//사용 방법
Singleton singleton = Singleton.INSTANCE;
singleton.someMethod();
```

#### **2) 장점**

-   **간결성**: 코드가 매우 간결하며, 명시적으로 싱글턴임을 나타낸다.
-   **직렬화에 안전**: 열거 타입의 직렬화와 역직렬화가 JVM에 의해 관리되므로, 새로운 객체가 생성되는 문제가 발생하지 않는다.
-   **리플렉션에 안전:** 리플렉션을 통해 싱글턴의 생성자를 호출하는 것이 불가능.
-   **멀티스레딩에 안전:** 열거 타입의 생성은 자바에서 스레드 안전하게 수행된다.

#### **3) 단점**

-   만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.
-   열거 타입이 다른 인터페이스를 구현하도록 선언할 수 는 있다.
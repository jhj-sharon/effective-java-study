
# **아이템6 :  불필요한 객체 생성을 피하라**

> 똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 재사용은 빠르고 세련되다.

### **1\. String Instance의 예시**

**(1) 'new' 키워드를 사용하여 문자열 객체를 명시적으로 생성할 경우**

-   문자열은 heap 영여겡 저장되며 아래 문장이 실행될 때 마다 String 인스턴스를 새로 만든다. 

```java
String s = new String("Effective Java");
```

**(2) String Constant Pool**

-   String Constant Pool은 Java의 메모리 영역 중 하나로, 문자열 리터럴을 저장하는 특별한 영역이다.
-   **Java에서 문자열은 불변(immutable)하기 때문에, 두 개 이상의 동일한 문자열 리터럴은 메모리에 한번만 저장된다.**
-   이 코드는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다. 이 방식을 사용한다면 같은 가상 머신 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용함이 보장된다.

```java
String s = "Effective Java";
```


**(3) 검증 코드**

-   아래 코드에서 str1과 str2는 동일한 문자열 리터럴 "Effective Java"를 참조하므로, 둘은 String Constant Pool에서 같은 메모리 주소를 가르킨다. 따라서 str1 == str2는 true를 반환한다.
-   하지만, new 키워드를 사용하여 문자열 객체를 명시적으로 생성하면, 그 문자열은 힙 영역에 저장되어 String Constant Pool과는 별개의 객체가 된다. 이 경우, str3 == str4는 false를 반환하게 됩니다.
-   str1 == str3도 false

```java
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
```


### **2\. 정적 팩터리를 제공하는 불변 클래스**

-   정적 팩터리 메서드를 사용해 불필요한 객체 생성을 피할 수 있다.
-   Boolean(String) 생성자 대신 Boolean.valueOf(String) 팩터리 메서드를 사용하는 것이 좋다.
-   생성자는 호출할 때마다 새로운 객체를 만들지만, 팩터리 메서드는 전혀 그렇지 않다.
-   불변 객체가 아닌 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용할 수 있다.

```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

### **3\. 비싼 객체는 캐싱해서 쓰기**

-   비싼 객체란 객체를 생성하는 데에 많은 자원(시간, 메모리, CPU 등)이 소모되는 객체를 의미한다다. 예를 들어, 데이터베이스 연결이나 네트워크 리소스를 초기화하는 객체, 복잡한 계산을 수행하는 객체 등이 이에 해당된다.
-   비싼 객체를 매번 새롭게 생성하는 것은 비효율적이다. 따라서 한 번 생성된 객체를 캐시(메모리에 임시로 저장)에 저장해두고, 필요할 때마다 재사용하는 것이 좋다. 

**(1 )String.matches 의 문제점**

-   주어진 문자열이 정규표현식과 일치하는지 확인하는 Java의 내장 메서드. 위 메서드는 다음과 같은 문제점이 있다.
-   **① 성능 문제** : String.matches는 내부적으로 'Pattern'객체를 생성하여 정규표현식의 일치 여부를 검사한다. 문제는 이 메서드가 호출될 때마다 새로운 'Pattern' 객체를 생성한다는 것이다. 따라서 String.matches를 반복적으로 호출하는 상황에서는 매번 새로운 'Pattern'객체가 생성되므로 성능에 부담이 된다.
-   **② 가비지 컬렉션 문제** : String.matches 메서드가 종료되면 내부에서 생성된 Pattern 객체는 더 이상 참조되지 않게 되므로 가비지 컬렉션의 대상이 된다.
-   **③ 유한 상태 머신 (Finite State Machine)** : 정규표현식은 내부적으로 유한 상태 머신으로 변환되어 동작한다. Pattern 객체는 이 유한 상태 머신을 구성하고, 문자열과 일치하는지 검사하기 위해 사용된다. 유한 상태 머신을 생성하는 과정은 비용이 들기 때문에, 이 작업을 반복적으로 수행하는 것은 비효율적이다.

\*\* 유한상태머신 : 주어지는 모든 시간에서 처해 있을 수 있는 유한개의 상태를 가지고 주어지는 입력에 따라 어떤 상태에서 다른 상태로 전환하거나 출력이나 액션이 일어나게 하는 장치 또는 그런 장치를 나타낸 모델

```java
public class RomanNumerals {
    static boolean isRomanNumeral(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" +
                "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})");
    }
}
```

**(2)String.matches의 문제점 해결 : 값비싼 객체를 재사용해 성능을 개선**

-   성능을 개선하려면 필요한 정규표현식을 표현하는 (불변인) Pattern 인스턴스를 클래스 초기화(정적 초기화) 과정에서 직접 생성해 캐싱해두고, 나중에 이 인스턴스를 재사용한다.
-   추가로, 개선 전에서는 존재조차 몰랐던 Pattern 인스턴스를 static final 필드로 끄집어내고 이름을 지어주어 코드의 의미가 훨씬 잘 드러난다.
-   지연 초기화(lazy initialization)로 불필요한 초기화를 없앨 수는 있지만, 권하지는 않는다. 코드는 복잡하게 만들지만 성능은 크게 개선되지 않을 때가 많기 때문이다.(지연초기화 : [https://sharonprogress.tistory.com/223](https://sharonprogress.tistory.com/223))

```java
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
```


### **4\. 불편 객체 사용지 주의해야하는 경우**

-   불변 객체인 경우에 안정하게 재사용하는 것이 매우 명확하다. 하지만 몇몇 경우에 분명하지 않은 경우가 있다. 어댑터를 예로 들면, **어댑터는 인터페이스를 통해서 뒤에 있는 객체로 연결해주는 객체라 여러개 만들 필요가 없다.**
-   Map 인터페이스가 제공하는 keySet은 Map이 뒤에 있는 Set 인터페이스의 뷰를 제공한다. keySet을 호출할 때마다 새로운 객체가 나올거 같지만 사실 같은 객체를 리턴하기 때문에 리턴 받은 Set 타입의 객체를 변경하면, 결국에 그 뒤에 있는 Map 객체를 변경하게 된다.

```java
package jeon.chapter2.item6;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeySetExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println(key);
        }

        // "Two" 키를 Set에서 제거하면 원래의 맵에서도 해당 항목이 제거된다.
        keys.remove("Two");
        System.out.println(map);  // {One=1, Three=3}
    }
}
```

### **5\. 오토박싱 : 불필요한 객체를 생성할 위험**

-   불필요한 객체를 생성하는 또 다른 방법으로 오토박싱이 있다. 오토박싱은 프로그래머가 기본타입과 박스 타입을 섞어 쓸 수 있게 해주고 박싱과 언박싱을 자동으로 해준다.
-   오토박싱은 기본 타입과 박스 타입의 경계가 안보이게 해주지만 그렇다고 그 경계를 없애주진 않는다.
-   의미상으로는 별다를 것 없지만 성능에서 차이가 많이 난다.
-   **따라서 박싱된 기본 타입보다는 기본 타입을 사용하고 의도치 않은 오토박싱이 숨어들지 않도록 해야한다.**

**(1) sum 변수의 타입이 Wrapper클래스 Long일 때** 

-   값은 정상적으로 출력되지만 느리다(약 6초).

```java
package jeon.chapter2.item6;

public class AutoBoxingExample {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Long sum = 0l;
        for (long i = 0 ; i <= Integer.MAX_VALUE ; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }
}
```



**(2)**sum 변수의 타입이 기본형 long일 때**** 

- 1초 미만
```java
package jeon.chapter2.item6;

public class AutoBoxingExample {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long sum = 0l;
        for (long i = 0 ; i <= Integer.MAX_VALUE ; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }
}
```



### **👏 프로그래머의 통찰력이 필요한 부분**

-   이 아이템이 의미하는 바는 객체 생성이 비싸니 무조건 피해야한다는 것이 아니다. 프로그램의 명확성, 간결성, 기능을 위해서는 추가 객체를 만들 수 있다. 이 균형을 유지하는 것은 프로그래머의 몫이다.
-   객체 생성을 효율적으로 해보겠다고 사소한 것들도 다 캐싱하거나 자체 풀(pool)을 만들어서 유지보수하기 어려운 복잡한 프로그램을 만드는 것도 피해야 하는 부분이다 (JVM에게 위임할 부분은 위임해라 가비지 컬렉터를 신뢰하자)
-   \[Item50\]방어적 복사(defensive copy) 와 대비되는 내용이기 때문에 객체 생성을 해야하는 경우와 하지 않고 기존 것을 재사용해야 하는 부분은 개발자의 역량에 달려있다 (혹은 성능 테스트를 직접 해서 비교해보아라)
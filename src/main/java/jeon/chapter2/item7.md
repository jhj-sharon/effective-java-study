
# **아이템6 :  다 쓴 객체 참조를 해제하라**

> C, C++ 언어는 메모리를 직접 관리하지만 자바의 경우 가비지 컬렉터가 이 역할을 해주기 때문에 다 쓴 객체는 알아서 회수된다. 하지만 이 것이 메로리 관리를 신경쓰지 않아도된다는 의미는 아니다. 

<Br>

## **1\. 메모리 누수가 일어나는 위치는 어디인가?**

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

-   위 코드에서는 스택이 커졌다가 줄어들었을 때 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다.
-   스택이 그 객체들의 **다 쓴 참조(obsolete reference)**를 여전히 가지고 있기 때문이다. 다 쓴 참조란 문자 그대로 앞으로 다시 쓰지 않을 참조를 뜻한다.
-    코드에서는 elements 배열의 '활성 영역'밖의 참조들이 모두 여기에 해당한다. 활성 영역은 인덱스가 size보다 작은 원소들로 구성된다.
-   **객체 하참조 하나를 살려두면 가비지 컬렉터는 그 객체 뿐 아니라 그 객체가 참조하는 모든 객체를 회수하지 못한다. 이로 인해 메모리 누수가 일어나게 된다.**

<hr>

**활성영역(Active Region)**   
\- 활성영역은 'elememts' 배열에서 현재 사용 중인, 즉 스택에 실제로 저장된 요소를 의미한다.   
\- 스택에서 'size'변수는 스택의 현재 크기, 즉 저장된 요소의 수를 의미한다. 따라서 'elements'배열에서 인덱스가 0부터 'size-1'까지의 원소들이 활성 영역이다.     
 **다 쓴 참조 (Obsolete Reference)**   
\- 객체에 대한 참조가 더 이상 필요하지 않지만, 참조가 여전히 존재하고 있어 가비지 컬럭터가 해당 객체를 회수하지 못하는 상황을 의미한다.   
\- **'pop' 메서드에서 요소를 스택에서 제거할 때, 실제로는 해당 요소의 참조를 'elements' 배열에서 제거하지 않는다. 대신 'size' 변수만 감소**시켜 '활성 영역'을 줄인다. 이로인해 'size'보다 큰 인덱스에 있는 요소들은 "다 쓴 참조"가 되며, 이 참조들 때문에 해당 객체들이 가비지 컬렉터에 의해 회수되지 않게 된다.
<hr>

<br>

## **2\. 메모리 누수 해결방법**

-   해당 참조를 다 썼을 때 null 처리(참조 해제)하면 된다. 제대로 구현된 pop 메서드는 아래와 같다.

```java
public class Stack {

    // ...

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }
}
```

-   위처럼 다 쓴 참조를 null로 처리하면 null 처리한 참조를 실수로 사용하려 하면 프로그램은 즉시 NullPointerException을 던지며 종료되므로 프로그램 오류를 조기에 발견할 수 있다.
-   그러나 **객체 참조를 null 처리하는 일은 예외적인 경우여야 한다.** 모든 객체를 다 쓰자마자 일일이 null 처리하는 것은 프로그램을 필요 이상으로 지저분하게 만들 뿐이다.
-   **다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 것이다. 변수의 범위를 최소가 되게 정의하자.**
-   아래처럼 수정하면, result 변수는 pop 메서드의 지역 변수이므로 해당 메서드가 종료될 때 자동으로 범위(scope)를 벗어나게 된다. 따라서 result에 저장된 객체 참조는 메서드가 종료되면 자동으로 해제된다.
  
  <br>

```java
public Object pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    
    // 지역 변수로 객체 참조 가져오기
    Object result = elements[size - 1];
    
    // 참조를 해제
    elements[size - 1] = null;
    size--;
    
    // 결과를 반환
    return result;
}
```

**| 그렇다면 null 처리는 언제 해야할까??**

-   일반적으로 **자기 메모리를 직접 관리하는 클래스라면 항시 메모리 누수에 주의해야 한다.** 원소를 다 사용한 즉시 그 원소가 참조한 객체들을 다 null 처리해줘야 한다. stack 클래스 또한 자기 메모리를 elements 배열로 저장소 풀을 만들어 직접 관리하므로 누수에 취약할 수 밖에 벗다.
-   그러므로 프로그래머는 비활성 영역이 되는 순간 null처리해서 해당 객체를 더는 쓰지 않을 것임을 가비지 컬렉터에 알려야한다.

<br><br>

## **3\. 캐시에서의 메모리 누수**

-   **캐시 역시 메모리 누수를 일으키는 주범이다. 객체 참조를 캐시에 넣어두고 이 사실을 까맣게 잊은 채 그 객체를 다쓴 뒤로도 한참 그냥 놔둘 수 도 있다.**
-   캐시 외부에서 키(key)를 참조하는 동안만(값이 아니다) 엔트리가 살아 있는 캐시가 필요한 상황이라면 WeakHashMap을 사용해 캐시를 만들자.
-   다 쓴 엔트리는 그 즉시 자동으로 제거될 것이다. 단, WeakHashMap은 이러한 상황에서만 유용하다.

<hr>

**WeakHashMap와 약한 참조(weak reference)**   
\- 기본적으로 HashMap과 유사하지만 키에 대한 참조가 약한참조(weak reference)로 저장되는 것이 특징이다.   
\- 약한 참조란 WeakHashMap의 키로 사용되는 객체에 대한 유일한 참조가  WeakHashMap 내부에만 있어 일반적인 참조와 달리 가비지 컬렉터에 의해 언제든지 회수될 수 있다는 특징이있다.  
<hr>
<br><br>

```java
import java.util.WeakHashMap;

public class Example {
    public static void main(String[] args) {
        WeakHashMap<Object, String> weakMap = new WeakHashMap<>();

        Object key = new Object();
        weakMap.put(key, "value");

        System.out.println("Before nulling key: " + weakMap);

        key = null; // key에 대한 strong reference를 제거

        System.gc(); // 가비지 컬렉터 호출

        System.out.println("After GC: " + weakMap); // map 제거
    }
}
```

## **4\. 캐시에서의 메모리 누수 해결방법**

-   캐시를 만들 때 보통은 캐시 엔트리의 유효 기간을 정확히 정의하기 어렵다. 따라서, **시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식**을 흔히 사용한다.
-   이런 방식에서는 쓰지 않는 엔트리를 이따금 청소해줘야 한다.
-   ScheduledThreadPoolExecutor 같은 백그라운드 스레드를 활용하거나 캐시에 새 엔트리를 추가할 때 부수 작업으로 수행하는 방법이 있다.
-   LinkedHashMap은 removeEldestEntry 메서드를 써서 후자의 방식으로 처리한다.
-   더 복잡한 캐시를 만들고 싶다면 java.lang.ref 패키지를 직접 활용해야 할 것이다.

<hr>
<span style="font-weight:bold; background-color:#E4E8FF;">LinkedHashMap</span>

 \-HashMap과 유사한 기능을 제공하면서 추가로 연결 목록을 통해 **키-값 쌍의 삽입 순서 또는 액세스 순서를 유지**한다.    
 \- 이러한 순서 유지 기능은 캐시 구현과 같은 특정 유스케이스에서 유용하게 사용된다.      

 **1\. 순서 보장:**   
 \- LinkedHashMap은 키-값 쌍의 삽입 순서를 기억한다.   
 \- 따라서 반복자를 사용하여 맵을 순회할 때 키-값 쌍이 삽입된 순서대로 반환된다.     

  **2.액세스 순서 모드:**   
  \- LinkedHashMap은 액세스 순서 모드를 선택적으로 사용할 수 있다.  
   \- 이 모드에서는 get과 put 연산이 수행될 때마다 해당 항목이 연결 목록의 끝으로 이동한다.   
  \- 이 기능은 최근에 액세스한 항목을 맵의 끝에 유지하는 데 사용될 수 있으므로, **LRU(Least Recently Used**) 캐시 구현과 같은 시나리오에서 유용하다.    

  **3.removeEldestEntry 메서드:**   
  \- 이 메서드는 맵에 새 항목이 추가될 때마다 호출된다.   
  \- 이 메서드를 오버라이드하여 맵의 크기가 특정 임계값을 초과하면 가장 오래된 항목을 자동으로 제거하는 로직을 구현할 수 있다.    

<hr>
<br><br>

```java

import java.util.LinkedHashMap;
import java.util.Map;
// 캐시에 새 엔트리를 추가할 때 부수작업으로 수행하는 방법 예제
public class Example {
    public static void main(String[] args) {
        // LRU 캐시로 동작하는 LinkedHashMap 
        int cacheSize = 5;
        LinkedHashMap<String, String> lruCache = new LinkedHashMap<String, String>(16, 0.75f, true) {
           //true : 액세스 순서 모드(accessOrder) 활성화
           @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > cacheSize;
                //true : 가장 오래된 항목 제거, false : 제거안됨
            }
        };

        for (int i = 1; i <= 10; i++) {
            lruCache.put("key" + i, "value" + i);
        }

        System.out.println(lruCache); // 가장 최근에 추가된 5개의 항목만 출력
    }
}

```

<br>

## **5\. 리스너(listener) 또는 콜백(callback)에서의 메모리 누수**

-   클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면, 뭔가 조치해주지 않는 한 콜백은 계속 쌓여갈 것이다.
-   이럴 때 콜백을 약한 참조(weak reference)로 저장하면 가비지 컬렉터가 즉시 수거해간다. (ex. WeakHashMap에 키로 저장하면 된다.)
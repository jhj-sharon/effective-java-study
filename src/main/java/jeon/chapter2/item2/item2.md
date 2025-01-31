# **아이템2 : 생성자에 매개변수가 많다면 빌더를 고려하라** 

> 정적 팩토리와 생성자에는 똑같은 제약이 하나 있다. 선택적 매개변수가 많을 경우 적절히 대응하기가 어렵다는 것이다. 기존에 매개변수가 많을 경우 고려할 수 있는 방안은 다음과 같다.

</br>

### **대안1. 점층적 생성자 패턴**
+ 점층적 생성자 패턴(telescoping constructor pattern)이란 객체 생성 시 필요한 조합에 따라 다양한 생성자를 제공하는 것을 의미한다.
+ 필수 매개변수를 받는생성자를 먼저 생성하고 매개변수 1개를 추가로 받는 생성자, 선택 매개 변수 2개를 추가로 받는 생성자 등의 형태로 매개변수 개수만큼 생성자를 늘려가는 방식이다.
+ 이 패턴의 주요한 문제는 선택적 매개변수의 수가 많아질수록 생성자의 수가 기하급수적으로 늘어나게 되며, 클라이언트 코드에서 어떤 생성자를 호출해야 할지 혼란스러울 수 있다.
+ 클래스의 생성자를 호출하는 입장에서 해당 매개변수가 맞는지, 매개변수의 개수는 제대로 입력한 것인지 확인해야 하는 불편함이 있다.  

</br>

### **대안2.  자바 빈즈 패턴**
+ 자바 빈즈 패턴은 객체 생성 및 초기화를 위한 프로그래밍 모델이다. 매개변수가 없는 생성자로 객체를 만든 후, setter 메서드들을 호출해 원하는 매개변수의 값을 설정하는 방식이다.
+ 이 방식을 통해 점층적 생성자 패턴의 문제인 코드작성 효율, 가독성 저하 문제를 해결했다.
+ 하지만 객체 하나를 만드려면 메서드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(Consistency)이 무너진 상태에 놓이게 된다.
+  즉, 객체가 불변(immutable)이 아니기 때문에 스레드 안전성 문제가 발생하는 것이다. 마찬가지로 Setter를 통해서 값을 주입받기 때문에 final을 사용할 수 없다.
+  
</br>

### **대안3. :gem: 빌더 패턴(Builder Pattern)**
+ 점층적 생성자 패턴의 안전성과 자바빈즈 패턴의 가독성을 겸비한 빌더 패턴
+ 클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자 혹은 정적 팩토리를 호출해 빌더 객체를 얻는다.
  - 이를 통해 불변식을 적용할 수 있다.
+ 그런 다음 빌더 객체가 제공하는 Setter 메서드들로 원하는 선택 매개변수들을 설정한다.
  - 여러 개의 가변 인자를 받을 수 있다. 
+ 마지막으로 매개변수가 없는 build 메서드를 호출해 객체를 얻는다.
  - **빌더의 setter 메서드들은 빌더 자신을 반환**하기 때문에 연쇄적으로 호출할 수 있다(method chaining).

+ 빌더 패턴의 형식
  - 클래스의 필수 속성들을 final 불변 선언한다.
  - 정적 내부 클래스로 빌더라는 클래스를 정의하여 객체 생성을 위임한다.
  - 빌더 클래스는 빌더가 만드는 객체 클래스의 정적 멤버 클래스로 정의한다.


:exclamation::exclamation:빌더 패턴은 계층적으로 설계된 클래스와 함께 사용할 때 특히 유용하다. 계층적으로 설계된 클래스는 종종 복잡한 생성자를 가지며, 각 계층의 서브 클래스가 추가될 때마다 이 생성자들이 더 복잡해질 수 있다. 이러한 복잡성을 관리하기 위해 빌더 패턴을 사용하면, 클라이언트 코드가 훨씬 깔끔하고 읽기 쉬워진다.

<hr>

$\rightarrow$ 결론적으로 생성자나 정적 팩토리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는게 더 낫다. 매개변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그러다. 빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 갈결하고 자바 빈즈보다 훨씬 안전하다. 


package jeon.chapter2.item2;

public class PersonBuilder {
    private final String name; // 필수
    private final int age;     // 선택적
    private final String address; // 선택적

    private PersonBuilder(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.address = builder.address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public static class Builder {
        // 필수 매개변수
        private final String name;

        // 선택적 매개변수 - 기본값으로 초기화
        private int age = 0;
        private String address = "unknown";

        public Builder(String name) {
            this.name = name;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public PersonBuilder build() {
            return new PersonBuilder(this);
        }
    }

    public static void main(String[] args) {
        PersonBuilder person = new PersonBuilder.Builder("sharon")
                .age(25)
                .address("서울시 중랑구")
                .build();
    }
}


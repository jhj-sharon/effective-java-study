package jeon.chapter2.item1;

public class Member {
    public enum Gender {
        MALE, FEMALE
    }

    private final Gender gender;

    // private 생성자로 외부에서 직접 객체를 생성하지 못하게 함
    private Member(Gender gender) {
        this.gender = gender;
    }

    // "maleMember"라는 이름으로 남성 회원 객체를 생성하는 정적 팩토리 메서드
    public static Member maleMember() {
        return new Member(Gender.MALE);
    }

    // "femaleMember"라는 이름으로 여성 회원 객체를 생성하는 정적 팩토리 메서드
    public static Member femaleMember() {
        return new Member(Gender.FEMALE);
    }

    public Gender getGender() {
        return gender;
    }
}

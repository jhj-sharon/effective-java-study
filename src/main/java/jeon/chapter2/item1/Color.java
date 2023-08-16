package jeon.chapter2.item1;

public class Color {
    private int red;
    private int green;
    private int blue;

    // Private 생성자
    private Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    // 자주 사용하는 기본 색체 객체를 미리 만들어두기
    private static final Color RED = new Color(255, 0, 0);
    private static final Color GREEN = new Color(0, 255, 0);
    private static final Color BLUE = new Color(0, 0, 255);

    // 정적 팩토리 메서드
    public static Color red() {
        return RED;
    }

    public static Color green() {
        return GREEN;
    }

    public static Color blue() {
        return BLUE;
    }
}

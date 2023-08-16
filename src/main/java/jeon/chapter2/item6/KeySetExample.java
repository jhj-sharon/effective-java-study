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

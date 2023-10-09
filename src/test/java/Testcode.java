import org.junit.jupiter.api.Test;

public class Testcode {

    public static void main(String[] args) {
        test();
    }

    @Test
    static void test() {
        Integer a = 10;
        Integer b = a;

        System.out.println(a);
        System.out.println(b);
    }

}

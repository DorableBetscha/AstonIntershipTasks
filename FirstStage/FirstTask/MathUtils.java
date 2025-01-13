package FirstTask;

public final class MathUtils {
    private MathUtils () {}

    public static int addition(int a, int b) {
        return a + b;
    }

    public static int subtraction(int a, int b) {
        return a - b;
    }

    public static int division(int a, int b) {
        if (b == 0) {
            return 0;
        } else {
            return a / b;
        }
    }

    public static boolean isPositive(double number) {
        return number > 0;
    }

    public static void main(String[] args) {
        System.out.println(MathUtils.addition(5, 3));
        System.out.println(MathUtils.subtraction(10, 6));
        System.out.println(MathUtils.division(8, 4));
        System.out.println(MathUtils.isPositive(-1));
    }
}

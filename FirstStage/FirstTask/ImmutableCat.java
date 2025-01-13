package FirstTask;

public final class ImmutableCat {
    private final String name;
    private final int age;

    public ImmutableCat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public static void main(String[] args) {
        ImmutableCat cat = new ImmutableCat("Yogurt", 4);
        System.out.println("Name: " + cat.getName());
        System.out.println("Age: " + cat.getAge());
    }
}

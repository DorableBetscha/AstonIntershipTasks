package ThirdTaskSingleton;

public class Character {
    private String name;
    private int health;
    private int strength;

    public Character(String name, int health, int strength) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя персонажа не может быть пустым");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Здоровье должно быть больше нуля");
        }
        if (strength <= 0) {
            throw new IllegalArgumentException("Сила должна быть больше нуля");
        }

        this.name = name;
        this.health = health;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "Имя: " + name + "\n" + "Здоровье: " + health + "\n" + "Сила: " + strength ;
    }
}

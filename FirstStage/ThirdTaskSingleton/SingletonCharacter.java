package ThirdTaskSingleton;

public class SingletonCharacter {
    private Character character;
    private SingletonCharacter() {}
    private static class SingletonHolder {
        private static final SingletonCharacter instance = new SingletonCharacter();
    }
    public static SingletonCharacter getInstance() {
        return SingletonHolder.instance;
    }

    public void createCharacter (String name, int health, int strength) {
        if (character == null) {
            character = new Character(name, health, strength);
            System.out.println("Персонаж создан: " + "\n" + character.toString());
        } else {
            System.out.println("Персонаж уже существует");
        }
    }
    public Character getCharacter() {
        return character;
    }

    public static void main(String[] args) {
        SingletonCharacter singletonCharacter = new SingletonCharacter().getInstance();
        singletonCharacter.createCharacter("Fellice", 30, 20);
    }
}

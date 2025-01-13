package ThirdTaskBuilder;

public class Character {
    private String name;
    private boolean warrior;
    private boolean wizard;

    private Character(CharacterBuilder characterBuilder) {
        name = characterBuilder.name;
        warrior = characterBuilder.warrior;
        wizard = characterBuilder.wizard;
    }

    public String getName() {
        return name;
    }

    public boolean isWarrior() {
        return warrior;
    }

    public boolean isWizard(boolean wizard) {
        return wizard;
    }

    public void printCharacterInfo() {
        System.out.println("Имя персонажа: " + name);
        System.out.println("Является воином: " + (warrior ? "Да" : "Нет"));
        System.out.println("Является волшебником: " + (wizard ? "Да" : "Нет"));
    }

    public static class CharacterBuilder {
        private String name;
        private boolean warrior;
        private boolean wizard;

        public CharacterBuilder(String name) {
            this.name = name;
        }

        public CharacterBuilder setIsWarrior(boolean warrior) {
            this.warrior = warrior;
            return this;
        }

        public CharacterBuilder setIsWizard(boolean wizard) {
            this.wizard = wizard;
            return this;
        }

        public Character build() {
            Character character = new Character(this);
            return character;
        }
    }

    public static void main(String[] args) {
        Character character = new Character.CharacterBuilder("Fellice").setIsWarrior(false).setIsWizard(true).build();
        character.printCharacterInfo();
    }
}

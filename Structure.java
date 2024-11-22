import javax.swing.*;
import java.util.TreeMap;

public class Structure {

    private String name;
    private String description;
    protected ImageIcon[] skins;
    private int level;
    // [0] = level 1, [1] = level 2, [2] = level 3
    private TreeMap<String, Integer>[] levelCosts;
    private int population;
    private int capacity;
    private JLabel displayLabel;

    public Structure(String name, int capacity, int population, ImageIcon[] skins, TreeMap<String, Integer>[]  levelCosts) {
        this.name = name;
        this.capacity = capacity;
        this.population = population;
        this.skins = skins;
        this.levelCosts = levelCosts;
        this.level = 0;
    }

    public void passTime() {

    }

    public void upgrade() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ImageIcon[] getSkins() {
        return skins;
    }

    public ImageIcon getEquippedIcon() {
        return skins[level];
    }

    public int getLevel() {
        return this.level;
    }

    public TreeMap<String, Integer>[]  getLevelCosts() {
        return levelCosts;
    }

    public JLabel getDisplayLabel() {
        return displayLabel;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPopulation() {
        return population;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSkins(ImageIcon[] skins) {
        this.skins = skins;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setDisplayLabel(JLabel displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String toString() {
        // String name, int capacity, int population, ImageIcon[] skins, TreeMap<String, Integer>[]  LevelCosts
        return "Name: " + name + "\nCapacity: " + capacity + "\nPopulation: " + population
                + "\nLevel " + (level + 1) + " Upgrade Cost: " + levelCosts[level];
    }
}
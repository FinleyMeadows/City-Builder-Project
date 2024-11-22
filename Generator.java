import javax.swing.*;
import java.util.TreeMap;

public class Generator extends Structure {
    private int resourcesPerPerson;
    private String resource;
    private int storedResources;
    private int storageLimit;

    public Generator(String name, int capacity, int population, ImageIcon[] skins, TreeMap<String, Integer>[]  pricePerUpgrade, int resourcesPerPerson, String resource, int storageLimit) {
        super(name, capacity, population, skins, pricePerUpgrade);
        this.resourcesPerPerson = resourcesPerPerson;
        this.resource = resource;
    }

    // TODO: Move this method into each of the subclasses and customize it
    public void setPrices() {
        // level 1
        getLevelCosts()[0] = new TreeMap<String, Integer>();
        getLevelCosts()[0].put("Wood", 100);
        getLevelCosts()[0].put("Money", 100);

        // level 2
        getLevelCosts()[1] = new TreeMap<String, Integer>();
        getLevelCosts()[1].put("Wood", 200);
        getLevelCosts()[1].put("Money", 200);

        // level 3
        getLevelCosts()[2] = new TreeMap<String, Integer>();
        getLevelCosts()[2].put("Wood", 300);
        getLevelCosts()[2].put("Money", 300);

    }

    // generates resources
    @Override
    public void passTime() {
        if (storedResources < storageLimit) {
            storedResources += getResourcesPerDay();
        }
        if (storedResources > storageLimit) {
            storedResources = storageLimit;
        }
    }

    @Override
    public void upgrade() {
        if (getLevel() < getSkins().length - 1) {
            setLevel(getLevel() + 1);
            setCapacity(getCapacity() + 1);
            setResourcesPerPerson(getResourcesPerPerson() + 1);
            getDisplayLabel().setIcon(getEquippedIcon());
        }
        System.out.println("Upgraded " + getName());
        System.out.println(super.toString());
    }

    @Override
    public String getDescription() {
        return "Resource: " + resource + "\nPopulation: " + "0/" + getCapacity() + "\nGeneration Per Person: " +
                resourcesPerPerson + "\nGeneration Per Day: " + getResourcesPerDay() + "/" + getMaxResourcesPerDay();
    }

    public int getResourcesPerPerson() {
        return resourcesPerPerson;
    }

    public int getResourcesPerDay() {
        return resourcesPerPerson * getPopulation();
    }

    public int getMaxResourcesPerDay() {
        return getCapacity() * getResourcesPerPerson();
    }

    public String getResource() {
        return resource;
    }

    public int getStoredResources() {
        return storedResources;
    }

    public void setStoredResources(int storedResources) {
        this.storedResources = storedResources;
    }

    public void setResourcesPerPerson(int resourcesPerPerson) {
        this.resourcesPerPerson = resourcesPerPerson;
    }
}

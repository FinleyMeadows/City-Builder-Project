import javax.swing.*;
import java.util.TreeMap;

public class Village extends Structure {

    private int attractionRate = 1;

    public Village() {
        super("Village", 9, 3, new ImageIcon[1], new TreeMap[3]);
        setPrices();

        getSkins()[0] = new ImageIcon("Pictures/Structures/Villages/Village1.png");
    }

    public void setPrices() {
        getLevelCosts()[0] = new TreeMap<String, Integer>();
        getLevelCosts()[0].put("Wood", 100);
        getLevelCosts()[0].put("Money", 100);

        getLevelCosts()[1] = new TreeMap<String, Integer>();
        getLevelCosts()[1].put("Wood", 100);
        getLevelCosts()[1].put("Money", 100);

        getLevelCosts()[2] = new TreeMap<String, Integer>();
        getLevelCosts()[2].put("Wood", 100);
        getLevelCosts()[2].put("Money", 100);

    }

    @Override
    public String getDescription() {
        return "\nPopulation: " + getPopulation() + "/" + getCapacity() + "\nPeople Added Per Day: " + attractionRate;
    }

    public int getAttractionRate() {
        return attractionRate;
    }


    public void setAttractionRate(int attractionRate) {
        this.attractionRate = attractionRate;
    }
}

import javax.swing.*;
import java.util.TreeMap;

public class WoodMill extends Generator {
    public WoodMill() {
        super("Wood Mill", 3, 0, new ImageIcon[1],
                new TreeMap[3], 5, "Wood", 100);
        setPrices();
        getSkins()[0] = new ImageIcon("Pictures/Structures/Wood Mills/WoodMill1.png");
    }
}

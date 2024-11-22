import javax.swing.*;
import java.util.TreeMap;

public class Farm extends Generator {

    public Farm() {
        super("Farm", 3, 0,
                new ImageIcon[3], new TreeMap[3], 5, "Food", 100);
        setPrices();

        getSkins()[0] = new ImageIcon("Pictures/Structures/Farms/Farm1.2.png");
        getSkins()[1] = new ImageIcon("Pictures/Structures/Farms/Farm2.png");
        getSkins()[2] = new ImageIcon("Pictures/Structures/Farms/Farm3.png");

    }
}

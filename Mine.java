import javax.swing.*;
import java.util.TreeMap;

public class Mine extends Generator {
    public Mine() {
        super("Mine", 3, 0, new ImageIcon[3],
                new TreeMap[3], 5, "Stone", 100);
        setPrices();
        getSkins()[0] = new ImageIcon("Pictures/Structures/Mines/Mine1.png");
        getSkins()[1] = new ImageIcon("Pictures/Structures/Mines/Mine2.png");
        getSkins()[2] = new ImageIcon("Pictures/Structures/Mines/Mine3.png");
    }
}

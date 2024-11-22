
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class CityFrame extends JFrame implements MouseListener, ActionListener {

    // stores all the components in the game
    private JLayeredPane layeredPane = new JLayeredPane();
    private final int SCREEN_WIDTH = 700;
    private final int SCREEN_HEIGHT = 700;
    // stores all the gridLabels displaying the ImageIcons and the structures correlated with them
    private Map<JLabel, Structure> gridMap = new HashMap<JLabel, Structure>();
    // stores all the JLabel grid components
    private JPanel grid = new JPanel();
    // < *resource type* , *amount* >
    public Map<String, Integer> resources = new HashMap<String, Integer>();
    private JLabel sky = new JLabel();
    // makes sure that only one thing can be edited at a time
    private boolean building = false;
    // stores the label that is being edited, edits include: building, upgrading, and destroying
    private JLabel selectedGridLabel = new JLabel();
    // stores all the shop components that get displayed after a grid JLabel has been pressed
    private JPanel shopPanel;
    // this will be used for background animations like clouds moving across a screen
    private Timer gameTimer;
    // stores the time so that background animations can be paced correctly
    private long gameTime = 0;
    // stores all the clouds visible on the screen
    private ArrayList<JLabel> clouds = new ArrayList<JLabel>();
    // stores every type of structure and their name for quick and easy access
    private Map<String, Structure> structures = new HashMap<String, Structure>();
    // tracks the status of the rain cycle
    private boolean raining = false;
    // holds the rain gif
    private JLabel rain;
    // holds the game intro gif
    private JLabel introGif;
    // title screen panel
    private JPanel titleScreen;
    // boolean to
    private boolean gameStarted = false;
    private JPanel selectedBuildOption = new JPanel();
    private Structure selectedBuildStructure;
    private Structure selectedGridStructure;
    private JLabel selectedBuildLabel = new JLabel();
    private ArrayList<JLabel> resourceLabels = new ArrayList<>();
    private JPanel editMenu;
    private boolean editing = false;

    public CityFrame() throws InterruptedException {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("City Builder");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setVisible(true);
        // this.setLocation(1441, 0);

        // sets preferred size to fit snugly after pack()
        layeredPane.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        layeredPane.setLayout(null);
        // adds to JFrame's border layout so that it gets recognized by the pack() method
        this.add(layeredPane, BorderLayout.CENTER);
        this.pack();

        displayGameStudioIntro();


        // stalls until the user presses play
        while (!gameStarted) {
            Thread.sleep(100);
        }

        // sets the sky's location and size
        sky.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
        sky.setLayout(null);
        // adds the sky image
        sky.setIcon(new ImageIcon("Pictures/Nature/Sky/Sky.png"));
        sky.setVisible(true);
        // adds sky to the lowest layer of the layeredPane
        layeredPane.add(sky, Integer.valueOf(0));

        rain = new JLabel(new ImageIcon("Animations/RainGif.gif"));
        rain.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
        layeredPane.add(rain, Integer.valueOf(1));
        rain.setVisible(false);

        // creates the grid of JLabels to be built upon
        createGrid();
        // displays all the resources in the top left of the screen
        createResourceTab();
        // adds all the different kinds of structures to the structure map for future access
        createStructures();
        // creates the shop panel to be displayed in the future
        createBuildWindow();
        // hides the shop panel until a grid JLabel has been clicked
        shopPanel.setVisible(false);

        // semi-randomly generates trees on each edge of the layeredPane
        generateTrees();
        // spawns in clouds to fill the screen using the same algorithm as used to generate them later on
        spawnInitialClouds();
        // starts moving clouds across the screen, spawning in new ones,
        // and deleting old ones that have made it off-screen
        startCloudAnimation();

        this.pack();
    }

    // - - - E N V I R O N M E N T - - - //

    public void createGrid() {
        int numRows = 7;
        int numCols = numRows * 2;
        grid.setBounds(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
        grid.setLayout(new GridLayout(numRows, numCols));
        grid.setBackground(new Color(24, 186, 32));

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                JLabel square = new JLabel();
                square.setName("Label" + col + row);
                square.setSize(grid.getX() / numCols, grid.getX() / numCols);
                square.setIcon(new ImageIcon("Pictures/Nature/Grass/GrassGif.gif"));

                if (col > 1 && col < numCols - 2) {
                    square.addMouseListener(this);
                }
                square.setHorizontalAlignment(SwingConstants.CENTER);
                gridMap.put(square, null);
                grid.add(square);
            }
        }

        layeredPane.add(grid, Integer.valueOf(0));
    }

    public void generateTrees() {
        int offset = 25;
        for (int t = 0; t < 2; t++) {
            for (int i = 14; i >= 0; i--) {
                for (int treeNum = 0; treeNum < 10; treeNum++) {
                    int randNum = (int) (Math.random() * 101);
                    int randYPos = 350 + (i - 1) * 25 + (int) (Math.random() * 25) + 1;
                    // the higher the number the greater the range of x values for generation
                    int randXPos = (int) (Math.random() * (100 - randNum));

                    JLabel tree = new JLabel(getCorrelatedTree(randXPos));
                    tree.setSize(50, 50);

                    if (t == 0) {
                        // trees end at 125
                        tree.setLocation(randXPos - offset, randYPos);
                    } else {
                        // trees start at 575
                        tree.setLocation((100 - randXPos) + 550 + offset, randYPos);
                    }
                    layeredPane.add(tree, Integer.valueOf(2));
                }
            }
        }
    }

    public ImageIcon getCorrelatedTree(int xPos) {
        if (xPos < 10) {
            return new ImageIcon("Pictures/Nature/Trees/Tree5.png");
        }
        else if (xPos < 20) {
            return new ImageIcon("Pictures/Nature/Trees/Tree4.png");
        }
        else if (xPos < 50) {
            return new ImageIcon("Pictures/Nature/Trees/Tree3.png");
        }
        else {
            int randNum = (int) (Math.random() * 2);
            if (randNum == 1) {
                return new ImageIcon("Pictures/Nature/Trees/Tree2.png");
            }
            return new ImageIcon("Pictures/Nature/Trees/Tree1.png");
        }
    }

    public void runRainCycle() {
        if (!raining) {
            int rainChance = (int) (Math.random() * 180) + 1;
            if (rainChance == 111) {
                raining = true;
                gameTime = 0;
                System.out.println("Rain cycle started");
            }

        }
        // TODO: make the rain end in the same random way that it starts (no rush tho)
        else {
            if (gameTime == 75000) {
                sky.setIcon(new ImageIcon("Pictures/Nature/Sky/RainySky.png"));
                for (JLabel grass : gridMap.keySet()) {
                    grass.setIcon(new ImageIcon("Pictures/Nature/Grass/StormyGrassGif.gif"));
                }
            }
            if (gameTime == 80000) {
                rain.setVisible(true);
            }
            if (gameTime > 140000) {
                raining = false;
                rain.setVisible(false);
                gameTime = 0;
                sky.setIcon(new ImageIcon("Pictures/Nature/Sky/Sky.png"));

                for (JLabel grass : gridMap.keySet()) {
                    grass.setIcon(new ImageIcon("Pictures/Nature/Grass/GrassGif.gif"));
                }
            }
        }
    }

    // begins moving clouds across the screen
    public void startCloudAnimation() {
        // creates the timer to control the flow of the clouds
        // runs every gameTimer tick
        gameTimer = new Timer(100, e -> {
            // updates the tracked time
            gameTime += gameTimer.getDelay();
            // has a 50% chance to spawn a cloud every 5 seconds
            if (gameTime % 1000 == 0) {
                runRainCycle();

                // creates the 50% chance
                int randNum = (int) (Math.random() * 3) + 1;
                // if it ends up as a 2 it creates a cloud
                if (randNum == 2) {
                    // creates a JLabel to store the cloud ImageIcon
                    JLabel cloudLabel = new JLabel(getRandomCloud());
                    // sets the size of the cloud JLabel to the largest cloud possible
                    // to accommodate for every possible size
                    cloudLabel.setSize(60, 23);
                    // generates a random value for the y position of the cloud between 25pixels and 224pixels
                    int randYPos = (int) (Math.random() * 150) + 25;
                    // sets the cloud's position to the far right of the screen and the random y position
                    cloudLabel.setLocation(SCREEN_WIDTH, randYPos);
                    // adds the cloud to the second layer of the layered pane
                    layeredPane.add(cloudLabel, Integer.valueOf(2));
                    // adds the cloud to the clouds arrayList to get its x position updated
                    clouds.add(cloudLabel);
                }
            }
            // traverses the clouds arrayList moving clouds across the screen and deleting old clouds
            for (int cloudNum = 0; cloudNum < clouds.size(); cloudNum++) {
                // gets the current cloud
                JLabel cloud = clouds.get(cloudNum);
                cloud.setName("Cloud " + cloudNum);
                // removes clouds after they've fully traveled off-screen
                if (cloud.getX() <= -59) {
                    clouds.remove(cloudNum);
                    layeredPane.remove(cloud);
                    cloudNum--;
                }
                // moves the remaining clouds across the screen at varying speeds
                else {
                    if (clouds.contains(cloud)) {
                        cloud.setLocation(cloud.getX() - 1, cloud.getY());
                    }
                }
            }
        });
        // starts the timer
        gameTimer.start();
    }

    public void spawnInitialClouds() {
        for (int i = 0; i < 70; i++) {
            int randNum = (int) (Math.random() * 3) + 1;
            if (randNum == 2) {
                JLabel cloudLabel = new JLabel(getRandomCloud());
                cloudLabel.setSize(60, 23);
                int randYPos = (int) (Math.random() * 150) + 25;
                cloudLabel.setLocation(i * 10, randYPos);
                layeredPane.add(cloudLabel, Integer.valueOf(2));
                clouds.add(cloudLabel);
            }
        }
    }

    public ImageIcon getRandomCloud() {
        ImageIcon cloud1;
        ImageIcon cloud2;
        ImageIcon cloud3;
        if (raining) {
            cloud1 = new ImageIcon("Pictures/Nature/Clouds/SmallRainCloud.png");
            cloud2 = new ImageIcon("Pictures/Nature/Clouds/MediumRainCloud.png");
            cloud3 = new ImageIcon("Pictures/Nature/Clouds/LargeRainCloud.png");
        } else {
            cloud1 = new ImageIcon("Pictures/Nature/Clouds/SmallCloud.png");
            cloud2 = new ImageIcon("Pictures/Nature/Clouds/MediumCloud.png");
            cloud3 = new ImageIcon("Pictures/Nature/Clouds/LargeCloud.png");
        }
        ImageIcon[] cloudIcons = {cloud1, cloud2, cloud3};
        return cloudIcons[(int) (Math.random() * 3)];
    }


    // - - - U I - - - //

    public void displayGameStudioIntro() {
        introGif = new JLabel();
        introGif.setBounds(0, 0, 700, 700);
        ImageIcon nuke = new ImageIcon("Animations/IntroGif.gif");
        Image scaledImage = nuke.getImage().getScaledInstance(700, 700, Image.SCALE_REPLICATE);
        JLabel gifLabel = new JLabel(new ImageIcon(scaledImage));
        gifLabel.setSize(700, 700);
        introGif.add(gifLabel);
        layeredPane.add(introGif, Integer.valueOf(4));

        Timer introTimer = new Timer(5000, e -> {
            introGif.setVisible(false);
            layeredPane.remove(introGif);
            displayTitleScreen();
        });
        introTimer.setRepeats(false);
        introTimer.start();
    }

    public void displayTitleScreen() {
        titleScreen = new JPanel();
        titleScreen.setLayout(null);
        titleScreen.setSize(700, 700);
        JLabel title = new JLabel("Welcome to Pixel Haven!");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 0, 700, 200);
        titleScreen.add(title);
        JButton start = new JButton("Press To Start");
        start.setName("Start");
        start.setBounds(250, 300, 200, 100);
        start.addActionListener(this);
        start.setBorder(new LineBorder(Color.yellow, 1));
        titleScreen.add(start);
        titleScreen.setVisible(true);
        layeredPane.add(titleScreen, Integer.valueOf(4));
    }

    public void createBuildWindow() {
        selectedGridLabel.setHorizontalAlignment(SwingConstants.CENTER);

        shopPanel = new JPanel();
      /*
         StructureName(50x10 JLabel)
         StructureImage(50x50 ImageIcon) x 2  StructureName(100x10 JLabel)
         Price(50x10 JLabel)                  StructureImage(100x100 ImageIcon)
            x                                 Price(70x10 JLabel) "Buy"(30x10 JButton)
            3                                 ItemDescription(100x40 JTextArea)
      */
        int width = 300;
        int height = 240;
        shopPanel.setBounds((350 - width) / 2 + 350, (350 - height) / 2, width, height);
        shopPanel.setLayout(new BorderLayout());
        shopPanel.setBackground(Color.WHITE);
        shopPanel.setBorder(new LineBorder(Color.BLACK, 1));

        JPanel shopOptions = new JPanel();
        shopOptions.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int numCols = 2;

        for (int i = 0; i < 1; i++) {
            for (Map.Entry<String, Structure> entry : structures.entrySet()) {
                Structure structure = entry.getValue();
                String structureName = entry.getKey();

                JPanel buildOption = new JPanel();
                buildOption.setName("Build Option");
                buildOption.setBackground(Color.WHITE);
                buildOption.setPreferredSize(new Dimension(75, 85));
                buildOption.setBorder(new LineBorder(Color.BLACK, 1));
                buildOption.setLayout(new BorderLayout());
                buildOption.addMouseListener(this);

                JLabel name = new JLabel(structureName);
                name.setFont(new Font("Arial", Font.BOLD, 8));
                name.setHorizontalAlignment(SwingConstants.CENTER);
                name.setSize(75, 10);
                buildOption.add(name, BorderLayout.NORTH);

                JLabel iconHolder = new JLabel(structure.getEquippedIcon());
                iconHolder.setHorizontalAlignment(SwingConstants.CENTER);
                iconHolder.setVerticalAlignment(SwingConstants.CENTER);
                iconHolder.setName(structureName);
                iconHolder.setBackground(Color.WHITE);
                iconHolder.setPreferredSize(new Dimension(75, 75));
                buildOption.add(iconHolder, BorderLayout.CENTER);

                shopOptions.add(buildOption, gbc);

                gbc.gridx++;

                if (gbc.gridx % numCols == 0) {
                    gbc.gridx = 0;
                    gbc.gridy++;
                    gbc.weighty++;
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(shopOptions);
        scrollPane.setPreferredSize(new Dimension(shopPanel.getWidth() / 2 + 20, shopPanel.getHeight()));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(13, shopPanel.getHeight()));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        shopPanel.add(scrollPane, BorderLayout.CENTER);

        selectedBuildLabel = new JLabel(new Village().getEquippedIcon());
        selectedBuildLabel.setName("Village");
        selectBuildOption();
        selectedBuildOption.setBackground(Color.WHITE);
        shopPanel.add(selectedBuildOption, BorderLayout.EAST);

        layeredPane.add(shopPanel, Integer.valueOf(3));
    }

    // - - - S O U N D  E F F E C T S - - - //

    // - - - R E S O U R C E  M A N A G E M E N T - - - //

    public void createResourceTab() {
        resources.put("Money", 1000);
        resources.put("Population", 0);
        resources.put("Food", 0);
        resources.put("Energy", 0);
        resources.put("Stone", 0);
        resources.put("Wood", 1000);
        formatResources();
    }

    public void formatResources() {
        int y = 0;
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            JLabel resourceLabel = new JLabel();
            resourceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            resourceLabel.setLocation(0, y);
            resourceLabel.setText(key + ": " + value);
            resourceLabel.setSize(resourceLabel.getPreferredSize());
            resourceLabel.setVisible(true);
            resourceLabels.add(resourceLabel);
            layeredPane.add(resourceLabel, Integer.valueOf(2));
            y += resourceLabel.getHeight();
        }
    }

    public void updateResourceLabels() {
        for (JLabel label : resourceLabels) {
            String text = label.getText();
            String resource = text.substring(0, text.indexOf(":"));
            int oldAmount = Integer.parseInt(text.substring(text.indexOf(":") + 2));
            boolean increase = resources.get(resource) > oldAmount;

            if (oldAmount != resources.get(resource)) {
                // incrementallyChangeValue(label, oldAmount, resources.get(resource));
                if (increase) {
                    playAnimation(new ImageIcon("Animations/ResourceIncrease.gif"), label.getX() + label.getWidth()
                            ,label.getY(), label.getHeight(), label.getHeight(), 900);
                }
                else {
                    playAnimation(new ImageIcon("Animations/ResourceDecrease.gif"), label.getX()
                                    + label.getWidth(), label.getY(), 24, 24, 900);
                }
                label.setText(resource + ": " + resources.get(resource));
                label.setSize(label.getPreferredSize());
            }
        }
    }

    // TODO: create a method that glides between numbers
    public void incrementallyChangeValue(JLabel label, int startNum, int endNum) {
        int difference = startNum - endNum;
        System.out.println("Difference: " + difference);
        int increment = 1000 / difference;
        System.out.println("Increment: " + increment);
        int incrementsPerSecond = 1000 / Math.abs(increment);
        System.out.println("Tics per second: " + incrementsPerSecond);

        Timer incrementTimer = new Timer(incrementsPerSecond, e -> {
            String text = label.getText();
            int colon = text.indexOf(":");
            int num = Integer.parseInt(text.substring(colon + 2));
            label.setText(text.substring(0, colon + 2) + (num + increment));
            if (label.getPreferredSize() != label.getSize()) {
                label.setSize(label.getPreferredSize());
            }
            System.out.println(num + increment);
        });
        incrementTimer.start();

        Timer oneSecond = new Timer(1000, e -> {
            incrementTimer.stop();
        });
        oneSecond.setRepeats(false);
        oneSecond.start();
    }

    // - - - C H E C K E R S - - - //

    public boolean canBuy(TreeMap<String, Integer> costs) {
        // traverses the cost of an upgrade
        for (Map.Entry<String, Integer> price : costs.entrySet()) {
            String resource = price.getKey();
            int amount = price.getValue();

            // if at any point the user can't afford a resource it returns false
            if (resources.get(resource) - amount < 0) {
                return false;
            }
        }

        return true;
    }

    public boolean canUpgrade() {
        // if the structure hasn't reached max level yet, it returns true
        return selectedGridStructure.getLevel() < selectedGridStructure.getSkins().length - 1
                && canBuy(selectedGridStructure.getLevelCosts()[selectedGridStructure.getLevel() + 1]);
    }

    // - - - M I S C E L L A N E O U S - - - //

    // adds every type of structure to the structures map
    public void createStructures() {
        structures.put("Village", new Village());
        structures.put("Farm", new Farm());
        structures.put("Mine", new Mine());
        structures.put("Wood Mill", new WoodMill());
    }

    public String upgradeCostToString(TreeMap<String, Integer> upgradeCosts) {
        String str = "";
        for (Map.Entry<String, Integer> price : upgradeCosts.entrySet()) {
            String resource = price.getKey();
            int amount = price.getValue();

            if (resource.equals("Money")) {
                str += "($" + amount + ") ";
            }
            else {
                str += "(" + resource + "x" + amount + ") ";
            }
        }

        return str;
    }

    public void selectBuildOption() {
        selectedBuildStructure = structures.get(selectedBuildLabel.getName());

        selectedBuildOption.setPreferredSize(new Dimension(130, 240));
        selectedBuildOption.setLayout(null);

        JLabel name = new JLabel(selectedBuildLabel.getName());
        name.setBounds(0, 0, 130, 20);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 15));
        selectedBuildOption.add(name);

        ImageIcon image = selectedBuildStructure.getEquippedIcon();
        Image scaledImage = image.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        JLabel iconHolder = new JLabel(new ImageIcon(scaledImage));
        iconHolder.setHorizontalAlignment(SwingConstants.CENTER);
        iconHolder.setBounds(15, name.getY() + name.getHeight() + 5, 100, 75);
        selectedBuildOption.add(iconHolder);

        JLabel price = new JLabel(upgradeCostToString(selectedBuildStructure.getLevelCosts()[0]));
        price.setFont(new Font("Arial", Font.PLAIN, 10));
        price.setBounds(0, iconHolder.getY() + iconHolder.getHeight() + 5, 90, 20);
        selectedBuildOption.add(price);

        JLabel buyButton = new JLabel("Buy");
        buyButton.setBounds(90, iconHolder.getY() + iconHolder.getHeight() + 5, 40, 20);
        buyButton.setName("Buy Button");
        buyButton.setFont(new Font("Arial", Font.PLAIN, 10));
        buyButton.addMouseListener(this);
        buyButton.setHorizontalAlignment(SwingConstants.CENTER);
        buyButton.setBorder(new LineBorder(Color.BLACK, 1));
        selectedBuildOption.add(buyButton);

        JTextArea textArea = new JTextArea(selectedBuildStructure.getDescription());
        textArea.setFont(new Font("Arial", Font.PLAIN, 10));
        textArea.setBounds(0, price.getY() + price.getHeight() + 5, 130, 130);
        selectedBuildOption.add(textArea);

        selectedBuildOption.setVisible(true);
    }

    public Structure createStructure(String structureName) {
        // if creating a farm creates a new farm
        if (structureName.contains("Farm")) {
            return new Farm();
        }
        // if creating a village creates a new village
        else if (structureName.contains("Village")) {
            return new Village();
        }
        // if mine a farm creates a new mine
        else if (structureName.contains("Mine")) {
            return new Mine();
        }
        // if wood mill a farm creates a new wood mill
        else if (structureName.contains("Wood Mill")) {
            return new WoodMill();
        }
        // if the name of the structure is invalid returns no new structure
        else {
            return null;
        }
    }

    public void purchaseStructure() {
        // stores the name of the structure being created
        String structureName = selectedBuildLabel.getName();
        // creates the label to be displayed on the grid (Holds the ImageIcon)
        JLabel structureLabel = new JLabel(structures.get(structureName).getEquippedIcon());
        structureLabel.setBounds(selectedGridLabel.getX(), selectedGridLabel.getY() + 350, 50, 50);

        // creates a new structure
        Structure newStructure = createStructure(structureName);
        // sets the label of the newly created structure to the label previously initialized
        newStructure.setDisplayLabel(structureLabel);

        // pairs the label on the grid with the newly created structure
        gridMap.put(selectedGridLabel, newStructure);
        // adds the new label to the window
        layeredPane.add(structureLabel, Integer.valueOf(1));

        // spends resources
        spendResources(structures.get(selectedBuildLabel.getName()));
        // plays the build animation
        playAnimation(new ImageIcon("Animations/Level1Upgrade.gif"), selectedGridLabel.getX(),
                selectedGridLabel.getY() + 350, 50, 50, 550);
        // plays the build sound
        playSound(new File("Sound Effects/UpgradeSoundEffect.wav"), 300);
    }

    public void spendResources(Structure structure) {
        // traverses the object's current cost for upgrading
        for (Map.Entry<String, Integer> price : structure.getLevelCosts()[structure.getLevel()].entrySet()) {
            // saves the type of resource
            String resource = price.getKey();
            // saves the amount of that resource
            int amount = price.getValue();

            // prints out the initial amount of that resource
            System.out.print(resource + ": " + resources.get(resource) + " -> ");
            // subtracts the cost of the current resource
            resources.put(resource, resources.get(resource) - amount);
            // prints out the updated amount of that resource
            System.out.println(resources.get(resource));
        }
        updateResourceLabels();
    }

    public void upgradeStructure() {
        // upgrades the structure behind the scenes
        selectedGridStructure.upgrade();
        // depletes resources and updates resource labels
        spendResources(selectedGridStructure);
        // plays the upgrade animation
        playAnimation(getUpgradeLevelPath(), selectedGridLabel.getX(), selectedGridLabel.getY() + 350
                , 50, 50, 550);
        // plays the upgrade sound
        playSound(new File("Sound Effects/UpgradeSoundEffect.wav"), 300);
    }

    public ImageIcon getUpgradeLevelPath() {
        // level 2 gif
        if (selectedGridStructure.getLevel() == 1) {
            return new ImageIcon("Animations/Level2Upgrade.gif");
        }
        // level 3 gif
        else {
            return new ImageIcon("Animations/Level3Upgrade.gif");
        }
    }

    public void playAnimation(ImageIcon gif, int x, int y, int width, int height, int duration) {
        // does a weird work-around to reset the gif each time a new one is created
        Image rerenderedImage = gif.getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE);
        JLabel animation = new JLabel(new ImageIcon(rerenderedImage));

        animation.setSize(width, height);
        animation.setLocation(x, y);

        // add the animation to the layeredPane
        animation.setVisible(true);
        layeredPane.add(animation, Integer.valueOf(4));

        // creates a timer to control the animation's duration
        Timer animationTimer = new Timer(duration, e -> {
            // stop the animation and clean up
            animation.setVisible(false);
            layeredPane.remove(animation);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        // ensure the timer runs only once
        animationTimer.setRepeats(false);
        animationTimer.start();
    }

    public void playSound(File wavFile, int duration) {
        try {
            // Load the audio file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(wavFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Play the sound
            clip.start();
            Timer timer = new Timer(duration, e2 -> clip.stop());
            timer.setRepeats(false);
            timer.start();

        }
        catch (Exception e) {
            // Catch any exception (IOException, UnsupportedAudioFileException, LineUnavailableException, etc.)
            System.out.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void destroyStructure() {
        selectedGridStructure.getDisplayLabel().setVisible(false);
        gridMap.put(selectedGridLabel, null);
        layeredPane.remove(selectedGridStructure.getDisplayLabel());
        // return 50% of the total resources used to build and upgrade the structure
        salvageResources();
    }

    public void salvageResources() {
        for (Map.Entry<String, Integer> price : getTotalStructureValue().entrySet()) {
            String resource = price.getKey();
            int quantity = price.getValue() / 2;

            System.out.print(resource + ": " + resources.get(resource));
            resources.put(resource, resources.get(resource) + quantity);
            System.out.println(" -> " + resources.get(resource));
        }
        updateResourceLabels();
    }


    public TreeMap<String, Integer> getTotalStructureValue() {
        TreeMap<String, Integer> totalValue = new TreeMap<String, Integer>();

        for (int i = 0; i <= selectedGridStructure.getLevel(); i++) {
            for (Map.Entry<String, Integer> cost : selectedGridStructure.getLevelCosts()[i].entrySet()) {
                String resource = cost.getKey();
                int quantity = cost.getValue();

                if (totalValue.containsKey(resource)) {
                    totalValue.put(resource, totalValue.get(resource) + quantity);
                }
                else {
                    totalValue.put(resource, quantity);
                }
            }
        }
        return totalValue;
    }

    public void displayEditMenu() {
        // creates a new edit menu
        editMenu = new JPanel();
        editMenu.setLayout(null);
        editMenu.setBounds(selectedGridLabel.getX() + 40, selectedGridLabel.getY() + 310, 70, 50);

        // adds an interactable upgrade label to the menu
        JLabel upgrade = new JLabel("Upgrade");
        upgrade.setHorizontalAlignment(SwingConstants.CENTER);
        upgrade.setName("Upgrade");
        upgrade.setBounds(0, 0, 70, 25);
        upgrade.setBorder(new LineBorder(Color.BLACK, 1));
        upgrade.addMouseListener(this);
        editMenu.add(upgrade);

        // adds an interactable destroy label to the menu
        JLabel destroy = new JLabel("Destroy");
        destroy.setHorizontalAlignment(SwingConstants.CENTER);
        destroy.setName("Destroy");
        destroy.setBounds(0, 25, 70, 25);
        destroy.setBorder(new LineBorder(Color.BLACK, 1));
        destroy.addMouseListener(this);
        editMenu.add(destroy);

        // make the edit menu visible and adds it to the window
        editMenu.setVisible(true);
        layeredPane.add(editMenu, Integer.valueOf(5));
        // starts editing
        editing = true;
    }


    // - - - M O U S E  L I S T E N E R  M E T H O D S - - - //

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object object = e.getSource();
        // if the object pressed was a JLabel
        if (object instanceof JLabel temp) {
            // if clicking on something in the edit menu
            if (editing) {
                // if clicking the upgrade label
                if (temp.getName().equals("Upgrade") && canUpgrade()) {
                    upgradeStructure();
                }
                // if clicking the destroy label
                else if (temp.getName().equals("Destroy")) {
                    destroyStructure();
                }
                // if the user clicks on anything while editing the edit menu disappears
                editing = false;
                editMenu.setVisible(false);
                layeredPane.remove(editMenu);
                selectedGridLabel.setBorder(null);
            }
            // if nothing has been clicked yet
            else if (!building) {
                // sets the selectedGridLabel equal to the label that was pressed in the grid
                selectedGridLabel = temp;
                // if that label hasn't been built upon yet, it displays the build shop
                if (gridMap.get(temp) == null) {
                    building = true;
                    shopPanel.setVisible(true);
                }
                // if clicking on a label that HAS something built you can edit that building
                else {
                    displayEditMenu();
                    selectedGridStructure = gridMap.get(selectedGridLabel);
                }
            }
            // if the user has the build shop open
            else if (temp.getName() != null && building) {
                // if clicking the buy button (actually a JLabel)
                if (temp.getName().equals("Buy Button") && canBuy(selectedBuildStructure.getLevelCosts()[0])) {
                    purchaseStructure();
                }
                // closes the build shop
                building = false;
                shopPanel.setVisible(false);
                selectedGridLabel.setBorder(null);
            }
        }
        // if the user is picking on the options in the shop to build
        else if (object instanceof JPanel temp) {
            if (temp.getName() != null && building && temp.getName().equals("Build Option")) {
                // selects the shop option and opens it up for more detail
                selectedBuildLabel = (JLabel) temp.getComponent(1);
                selectedBuildOption.removeAll();
                selectedBuildOption.setVisible(false);
                selectBuildOption();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object object = e.getSource();

        // if the object entered was a JLabel
        if (object instanceof JLabel temp) {
            // if the object entered was the buy button
            if (temp.getName() != null && temp.getName().equals("Buy Button")) {
                // if the user can afford the selected build option
                if (canBuy(selectedBuildStructure.getLevelCosts()[0])) {
                    // sets the border of the buy button to green
                    temp.setBorder(new LineBorder(Color.GREEN, 3));
                }
                // if the user cannot afford the selected build option
                else {
                    // sets the border of the buy button to red
                    temp.setBorder(new LineBorder(Color.RED, 3));
                }
            }
            // if the mouse enters the upgrade JLabel in the edit menu
            else if (temp.getName() != null && editing) {
                if (temp.getName().equals("Upgrade")) {
                    // if the user can afford the upgrade
                    if (canUpgrade()) {
                        // sets the border of the upgrade label to green
                        temp.setBorder(new LineBorder(Color.GREEN, 3));
                    }
                    // if the user cannot afford the upgrade
                    else {
                        // sets the border of the upgrade label to red
                        temp.setBorder(new LineBorder(Color.RED, 3));
                    }
                }
                else if (temp.getName().equals("Destroy")) {
                    temp.setBorder(new LineBorder(Color.YELLOW, 3));
                }
            }
            // if simply entering one of the grid labels
            else if (!building && !editing) {
                // sets the grid label's border to yellow
                temp.setBorder(new LineBorder(Color.YELLOW, 3));
            }
        }
        // if the object entered was a JPanel
        else if (object instanceof JPanel temp) {
            // if that JPanel was a build option
            if (temp.getName() != null && building && temp.getName().equals("Build Option")) {
                // turns the border green if the user can afford the selected build option
                if (canBuy(selectedBuildStructure.getLevelCosts()[0])) {
                    temp.setBorder(new LineBorder(Color.GREEN, 3));
                }
                // turns the border red if the user cannot afford the selected build option
                else {
                    temp.setBorder(new LineBorder(Color.RED, 3));
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object object = e.getSource();

        // if the object exited was a JLabel
        if (object instanceof JLabel temp) {
            // if exiting the upgrade or destroy labels in the edit menu, it sets their borders to black
            if (editing && temp.getName().equals("Upgrade") || temp.getName().equals("Destroy")) {
                temp.setBorder(new LineBorder(Color.BLACK, 1));
            }
            // if exiting a grid label it removes the yellow border
            else if (!building) {
                temp.setBorder(null);
            }
            // if exiting the buy button it sets the border back to black
            else if (temp.getName() != null && building) {
                if (temp.getName().equals("Buy Button")) {
                    temp.setBorder(new LineBorder(Color.BLACK, 1));
                }
            }
        }
        // if the object exited was a JPanel
        else if (object instanceof JPanel temp) {
            // if that JPanel was a build option it sets the border color back to black
            if (temp.getName() != null && building && temp.getName().equals("Build Option")) {
                temp.setBorder(new LineBorder(Color.BLACK, 1));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();

        // if the object interacted with was a JButton
        if (object instanceof JButton temp) {
            // if that button was the start button to the game
            if (temp.getName().equals("Start")) {
                // removes the title screen and starts the game
                titleScreen.setVisible(false);
                layeredPane.remove(titleScreen);
                gameStarted = true;
            }
        }
    }
}
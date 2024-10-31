import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class CityFrame extends JFrame implements MouseListener {
   
   private JLayeredPane layeredPane = new JLayeredPane();
   private final int SCREEN_WIDTH = 700;
   private final int SCREEN_HEIGHT = 700;
   private Map<JLabel, Structure> gridMap = new HashMap<JLabel, Structure>();
   private JPanel grid = new JPanel();
   // < *resource type* , *amount* >
   private Map<String, Double> resources = new HashMap<String, Double>();
   private JLabel sky = new JLabel();
   // makes sure that only one thing can be edited at a time
   private boolean editing = false;
   private JLabel selectedLabel = new JLabel();
   private JScrollPane scrollPane;
   // this will be used for background animations like clouds moving across a screen
   private Timer gameTimer;
   private long gameTime = 0;
   private ArrayList<JLabel> clouds = new ArrayList<JLabel>();

   public CityFrame() {
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setTitle("City Builder");
      this.setLayout(new BorderLayout());
      this.setResizable(false);
      this.setVisible(true);

      layeredPane.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
      layeredPane.setLayout(null);
      this.add(layeredPane, BorderLayout.CENTER);

      // sky
      sky.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
      sky.setLayout(null);
      sky.setIcon(new ImageIcon("Sky.png"));
      sky.setVisible(true);
      layeredPane.add(sky, Integer.valueOf(0));

      createGrid();
      this.pack();
      createResourceTab();
      createBuildWindow();
      scrollPane.setVisible(false);

      generateTrees();
      spawnInitialClouds();
      startCloudAnimation();
   }

   public void startCloudAnimation() {
      // clouds
      gameTimer = new Timer(100, new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            gameTime += gameTimer.getDelay();

            // fun grass movement (just for fun)
            // moveGrass(5);

            // System.out.println(gameTime);
            if (gameTime % 5000 == 0) {
               int randNum = (int) (Math.random() * 2) + 1;
               if (randNum == 2) {
                  System.out.println("Cloud created");
                  JLabel cloudLabel = new JLabel(getRandomCloud());
                  cloudLabel.setSize(60, 23);
                  int randYPos = (int) (Math.random() * 200) + 25;
                  cloudLabel.setLocation(SCREEN_WIDTH, randYPos);
                  layeredPane.add(cloudLabel, Integer.valueOf(1));
                  clouds.add(cloudLabel);
                  System.out.println(clouds.size());
               }
            }
            for (int cloudNum = 0; cloudNum < clouds.size(); cloudNum++) {
               JLabel cloud = clouds.get(cloudNum);
               // removes clouds after they've fully traveled off-screen
               if (cloud.getX() <= -58) {
                  clouds.remove(cloudNum);
                  layeredPane.remove(cloud);
                  System.out.println("Deleted Cloud");
                  System.out.println(clouds.size());
               }
               // moves the remaining clouds across the screen at varying speeds
               else {
                  cloud.setLocation(cloud.getX() - 1, cloud.getY());
               }
            }
         }
      });

      gameTimer.start();
   }

   public void spawnInitialClouds() {
      for (int i = 0; i < 70; i++) {
         int randNum = (int) (Math.random() * 2) + 1;
         if (randNum == 2) {
            JLabel cloudLabel = new JLabel(getRandomCloud());
            cloudLabel.setSize(60, 23);
            int randYPos = (int) (Math.random() * 200) + 25;
            cloudLabel.setLocation(i * 20, randYPos);
            layeredPane.add(cloudLabel, Integer.valueOf(1));
            clouds.add(cloudLabel);
         }
      }
   }

   public void moveGrass(int fps) {
      if (gameTime % (1000 / fps) == 0) {
         for (JLabel label : gridMap.keySet()) {
            label.setIcon(getRandomGrass());
         }
      }
   }

   public ImageIcon getRandomGrassGif() {
      ImageIcon grass = new ImageIcon("GrassGif1.gif");
      ImageIcon grass2 = new ImageIcon("GrassGif2.gif");
      ImageIcon grass3 = new ImageIcon("GrassGif3.gif");
      ImageIcon[] grasses = {grass, grass2, grass3};
      return grasses[(int) (Math.random() * 3)];
   }

   public ImageIcon getRandomCloud() {
      ImageIcon cloud1 = new ImageIcon("SmallCloud.png");
      ImageIcon cloud2 = new ImageIcon("MediumCloud.png");
      ImageIcon cloud3 = new ImageIcon("LargeCloud.png");
      ImageIcon[] cloudIcons = {cloud1, cloud2, cloud3};
      return cloudIcons[(int) (Math.random() * 3)];
   }
   
   public void createGrid() {
      int numRows = 7;
      int numCols = numRows * 2;
      grid.setBounds(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
      grid.setLayout(new GridLayout(numRows, numCols));
      grid.setBackground(new Color(24, 186, 32));
      
      for (int row = 0; row < numRows; row++) {
         for (int col = 0; col < numCols; col++) {
            JLabel square = new JLabel();
            square.setSize(grid.getX() / numCols, grid.getX() / numCols);
            // square.setIcon(getRandomGrass());
            square.setIcon(new ImageIcon("GrassGif4.gif"));
            // square.setIcon(getRandomGrassGif());
            if (col > 2 && col < numCols - 3) {
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
               // JLabel tree = new JLabel(new ImageIcon("Tree.png"));
               JLabel tree = new JLabel(new ImageIcon("MovingTree.gif"));
               tree.setSize(50, 50);
               int randYPos = 350 + (i - 1) * 25 + (int) (Math.random() * 25) + 1;
               // the higher the number the greater the range of x values for generation
               int randXPos = (int) (Math.random() * (100 - randNum));
               if (t == 0) {
                  // trees end at 125
                  tree.setLocation(randXPos - offset, randYPos);
               }
               else {
                  // trees start at 575
                  tree.setLocation((100 - randXPos) + 550 + offset, randYPos);
               }
               layeredPane.add(tree, Integer.valueOf(1));
            }
         }
      }
   }

   public ImageIcon getRandomGrass() {
      ImageIcon grass = new ImageIcon("Grass1.png");
      ImageIcon grass2 = new ImageIcon("Grass2.png");
      ImageIcon grass3 = new ImageIcon("Grass3.png");
      ImageIcon[] grasses = {grass, grass2, grass3};
      return grasses[(int) (Math.random() * 3)];
   }

   public void createResourceTab() {
      resources.put("Money", 0.0);
      resources.put("Population", 0.0);
      resources.put("Energy", 0.0);
      resources.put("Stone", 0.0);
      resources.put("Wood", 0.0);
      formatResources();
   }

   public void formatResources() {
      int y = 0;
      for (Map.Entry<String, Double> entry : resources.entrySet()) {
         String key = entry.getKey();
         double value = entry.getValue();
         JLabel resourceLabel = new JLabel();
         resourceLabel.setFont(new Font("Arial", Font.BOLD, 20));
         resourceLabel.setBounds(0, y, SCREEN_WIDTH / 2, 30);
         if (key.equals("Money")) {
            resourceLabel.setText(key + ": $" + String.format("%,.2f", value));
         }
         else {
            resourceLabel.setText(key + ": " + (int) value);
         }
         resourceLabel.setVisible(true);
         layeredPane.add(resourceLabel, Integer.valueOf(2));
         y += resourceLabel.getHeight();
      }
   }

   public void createBuildWindow() {
      selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);

      JPanel buildWindow = new JPanel();
      buildWindow.setLayout(new GridLayout(12, 1));

      /*
      for (Map.Entry<JLabel, Structure> entry : gridMap.entrySet()) {
         JPanel buildOption = new JPanel();
         buildOption.setSize(50, 60);
         buildOption.setLayout(new BorderLayout());

         Structure structure = entry.getValue();
         JButton button = new JButton(structure.getName());
         // button.setIcon(structure.getEquippedIcon());
         button.setIcon(new ImageIcon("PlaceHolder.png"));
         button.addMouseListener(this);
         buildWindow.add(button);
      }
      */
      for (int i = 0; i < 12; i ++) {
         JButton button = new JButton("Build " + (i + 1));
         button.setPreferredSize(new Dimension(50, 50));
         button.addMouseListener(this);
         // buildOption.add(button, BorderLayout.CENTER);
         JLabel description = new JLabel("");
         buildWindow.add(button);
      }
      
      scrollPane = new JScrollPane(buildWindow);
      scrollPane.setBounds(SCREEN_WIDTH / 2 + 25, 25, 100, 250);
      // scrollPane.setLocation(SCREEN_WIDTH / 2 + 25, 25);
      scrollPane.createVerticalScrollBar();
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      layeredPane.add(scrollPane, Integer.valueOf(2));
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      Object object = e.getSource();
      // if clicking on a panel
      if (object instanceof JLabel && !editing) {
         editing = true;
         selectedLabel = (JLabel) object;
         // if something has already been built it will give you the option to upgrade it
         if (gridMap.get(selectedLabel) != null) {
            // *upgrade menu pop-up*
            // I need to sleep :(
         }
         // otherwise you get the choice to build something there
         else {
            scrollPane.setVisible(true);
         }
      }
      // if clicking on a button
      else if (object instanceof JButton) {
         JButton temp = (JButton) object;
         if (temp.getText().contains("Build")) {
            System.out.println("button got pressed");
            selectedLabel.setIcon(null);
            selectedLabel.setText(temp.getText());
            scrollPane.setVisible(false);
            editing = false;
            selectedLabel.setBorder(null);
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {

   }

   @Override
   public void mouseReleased(MouseEvent e) {

   }

   @Override
   public void mouseEntered(MouseEvent e) {
      Object object = e.getSource();
      if (object instanceof JLabel && !editing) {
         JLabel temp = (JLabel) object;
         temp.setBorder(new LineBorder(Color.YELLOW, 1));
         System.out.println("Entered");
      }
   }

   @Override
   public void mouseExited(MouseEvent e) {
      Object object = e.getSource();
      if (object instanceof JLabel && !editing) {
         JLabel temp = (JLabel) object;
         temp.setBorder(null);
         System.out.println("Exited");
      }
   }
}
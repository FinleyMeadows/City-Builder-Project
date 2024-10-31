import javax.swing.*;

public class Structure {
   private String name;
   private ImageIcon[] icons;
   private ImageIcon equippedIcon;
   private int upgradeIndex;
   private int[] pricePerUpgrade;

   public Structure(String name, ImageIcon[] icons, int[] pricePerUpgrade) {
      this.name = name;
      this.icons = icons;
      this.equippedIcon = icons[0];
      this.upgradeIndex = 0;
      this.pricePerUpgrade = pricePerUpgrade;
   }

   public void upgrade() {
      if (upgradeIndex < icons.length - 1) {
         upgradeIndex++;
         equippedIcon = icons[upgradeIndex];
      }
      else {
         System.out.println("Max upgrade has been reached");
      }
   }

   public String getName() {
      return name;
   }
   public ImageIcon[] getIcons() {
      return icons;
   }
   public ImageIcon getEquippedIcon() {
      return equippedIcon;
   }
   public int getUpgradeIndex() {
      return this.upgradeIndex;
   }
   public int[] getPricePerUpgrade() {
      return pricePerUpgrade;
   }

   public void setName(String name) {
      this.name = name;
   }
   public void setIcons(ImageIcon[] icons) {
      this.icons = icons;
   }
   public void setEquippedIcon(ImageIcon equippedIcon) {
      this.equippedIcon = equippedIcon;
   }
   public void setUpgradeIndex(int upgradeIndex) {
      this.upgradeIndex = upgradeIndex;
   }
   public void setPricePerUpgrade(int[] pricePerUpgrade) {
      this.pricePerUpgrade = pricePerUpgrade;
   }
}
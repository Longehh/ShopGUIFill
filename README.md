# How to compile
Update pom.xml with the relevent dependencies in Maven Repositories or your .m2 cache on your computer.
Dependencies:
- **v1_8_R3** - This can be changed by simply updating the NMS Item Stack to retreive the actual item name.
- **Vault 1.6** - This could also be ported to a higher version by simply updating the methods.

After all the dependencies are updated use do mvn clean install, or build in your IDE.

# How to use
itemBuyGUI:
- "&7Buy price:&c %buy%"
Update the String above in your ShopGUIPlus Configuration to have the colour code before the space, so we can parse the items
price for the 'Fill Inventory' function.

# Update the following:
messages:
  guiTitle: "&8Buying "
 
 To the start of your specific configuration in ShopGUIPlus i.e. if it's "&2Buying " update the value to that.
 After all these steps have been followed you should be good to go, and the Fill Inventory item will be shown within the ShopGUIPlus GUI.

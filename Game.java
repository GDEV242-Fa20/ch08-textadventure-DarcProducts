import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Random;
/**
 *  This class is the main class of the "World of Strange Events" application. 
 *  "World of Strange Events" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Craig Hussey
 * @version 2020.10.24
 */ 

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Item> myInventory;
    private float currentWeight;
    private int currentHealth = 10;
    private static final int MAXHEALTH = 10;
    private int currentStamina = 20;
    private static final int MAXSTAMINA = 20;
    private static final int MAXCARRYWEIGHT = 20;
    private boolean isInBattle = false;
    private String previousDirection = "";
    private Boolean isInCaves = false;
    private Boolean usingFlashlight = false;
    private DecimalFormat f;
    private Random rand;
    private int flashLightUses = 4;
    /**
     * main method to start game
     */
    public static void main(String[] args)
    {
        Game myGame = new Game();
        myGame.play();
    }
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        f =  new DecimalFormat("#0.00");
        rand = new Random();
        createRooms();
        myInventory = new ArrayList();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together, also adds a name as the object name.
     */
    private void createRooms()
    {
        //create the rooms
        Room crashedPod = new Room("crashed pod", "crashedPod");
        Room encampment = new Room("small encampment", "encampment");
        Room acidBog = new Room("acid bog", "acidBog");
        Room foggyCliffs = new Room("cliffs", "foggyCliffs");
        Room plains = new Room("rocky plains", "plains");
        Room thornForest = new Room("forest", "thornForest");
        Room deepCanyon = new Room("canyon", "deepCanyon");
        Room caveEntrance = new Room("cave entrance", "caveEntrance");
        Room leftCavePath = new Room("cave path", "leftCavePath");
        Room middleCavePath = new Room("cave path", "middleCavePath");
        Room rightCavePath = new Room("cave path", "rightCavePath");
        Room largeRoomCave = new Room("cave room", "largeRoomCave");
        Room deepLeftCave = new Room("deep cave", "deepLeftCave");
        Room deepRightCave = new Room("deep cave", "deepRightCave");
        Room deepMiddleCave = new Room("deep cave", "deepMiddleCave");
        Room artifactSite = new Room("artifact site", "artifactSite"); 
        // initialise room exits
        crashedPod.setExit("north", encampment);
        encampment.setExit("south", crashedPod);
        encampment.setExit("west", acidBog);
        encampment.setExit("east", foggyCliffs);
        encampment.setExit("north", plains);

        acidBog.setExit("north", thornForest);
        acidBog.setExit("east", encampment);

        foggyCliffs.setExit("north", deepCanyon);
        foggyCliffs.setExit("west", encampment);

        thornForest.setExit("north", caveEntrance);
        thornForest.setExit("south", acidBog);

        deepCanyon.setExit("north", caveEntrance);
        deepCanyon.setExit("north", foggyCliffs);

        plains.setExit("north", caveEntrance);
        plains.setExit("south", encampment);

        caveEntrance.setExit("north", largeRoomCave);
        caveEntrance.setExit("west", thornForest);
        caveEntrance.setExit("east", deepCanyon);
        caveEntrance.setExit("south", plains);
        
        //once pass cave entrance it collapses
        largeRoomCave.setExit("north", middleCavePath);
        largeRoomCave.setExit("west", leftCavePath);
        largeRoomCave.setExit("east", rightCavePath);

        leftCavePath.setExit("north", deepLeftCave);

        rightCavePath.setExit("north", deepRightCave);

        middleCavePath.setExit("north", deepMiddleCave);

        deepLeftCave.setExit("south", leftCavePath);
        deepLeftCave.setExit("east", deepMiddleCave);

        deepRightCave.setExit("south", rightCavePath);
        deepRightCave.setExit("west", deepMiddleCave);

        deepMiddleCave.setExit("south", middleCavePath);
        deepMiddleCave.setExit("east", deepRightCave);
        deepMiddleCave.setExit("west", deepLeftCave);
        //add something to do at artifact site
        deepMiddleCave.setExit("north", artifactSite);

        //add items
        artifactSite.addItem(new Item("artifact", 10f, 1));

        crashedPod.addItem(new Item("flashlight", 1.2f, 1));  
        crashedPod.addItem(new Item("gun", 2f, 1));
        crashedPod.addItem(new Item("ammo", 0.2f, 8));

        encampment.addItem(new Item("knife", 1.2f, 1));
        encampment.addItem(new Item("sedative", 1.2f, 1));
        encampment.addItem(new Item("first-aid", 1.2f, 1));

        crashedPod.addStoryDescription("You are at the landing site of the crashed pod. There are small fires burning and the area is filled with smoke.");
        encampment.addStoryDescription("You are at a small make-shift encampment. It looks like someone has been here before you.");
        acidBog.addStoryDescription("You are in a bog with acid filled pits. There is smoke with green tint all around.");
        foggyCliffs.addStoryDescription("You are surrounded by fog. From what you can see, there are steep cliffs and sharp rocks in all directions.");
        thornForest.addStoryDescription("There are strange black trees everywhere that are covered in large thorns.");
        deepCanyon.addStoryDescription("");
        caveEntrance.addStoryDescription("");
        largeRoomCave.addStoryDescription("A large dark cave room. The air is cool in here.You can barely see any of the paths ahead.");
        middleCavePath.addStoryDescription("The cave is lined with mushrooms that each produce a small blue glow, illuminating the cave path ahead.");
        leftCavePath.addStoryDescription("");
        rightCavePath.addStoryDescription("");
        deepMiddleCave.addStoryDescription("");
        deepLeftCave.addStoryDescription("");
        deepRightCave.addStoryDescription("");
        artifactSite.addStoryDescription("");
        
        currentRoom = crashedPod;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Strange Events!");
        System.out.println("World of Strange Events is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println(); currentRoom.printLongDescription();
        System.out.println(); printInventory();
        System.out.println(); printHealthInfo();
        System.out.println(); System.out.println("\n----->Commands<-----");
        parser.showCommands(); System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
            System.out.println("I don't understand...");
            break;

            case HELP:
            printHelp();
            break;

            case GO:
            goRoom(command);
            break;

            case LOOK:
            look();
            break;

            case DROP:
            drop(command);
            break;

            case GRAB:
            tryGrab(command);
            break;
            
            case USE:
            tryUse(command);
            break;

            case BACK:
            goBack();
            break;

            case QUIT:
            wantToQuit = quit(command);
            break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are alone. You wander");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        //haven't implemented isInBattle yet
        if (!isInBattle && currentStamina>0)
        {
            if(!command.hasSecondWord()) {
                // if there is no second word, we don't know where to go...
                System.out.println("Go where?");
                return;
            }
            setPreviousDirection(command.getSecondWord());
            String direction = command.getSecondWord();
            // Try to leave current room.
            Room nextRoom = currentRoom.getExit(direction);

            if (nextRoom == null) {
                System.out.println("There is no place to go!");
            }
            else {
                currentRoom = nextRoom;
                //System.out.println(currentRoom.getLongDescription());
                printUponEnter();
                currentStamina--;
            }
        } else System.out.println("You cannot run!");
        
        if (currentRoom.getName().equals("largeRoomCave"))
        {
            if (!isInCaves)
                System.out.println("The entrance to the cave collapses!");
            isInCaves = true;
        }
    }
    
    /**
     * set previous string
     * @method
     */
    private void setPreviousDirection(String myString)
    {
        if (myString.equals("north"))
        previousDirection = "south";
        else if (myString.equals("south"))
        previousDirection = "north";
        else if (myString.equals("east"))
        previousDirection = "west";
        else if (myString.equals("west"))
        previousDirection = "east";
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * prints location exits
     * @method
     */
    private void printLocationExits()
    {
        currentRoom.printExitString();
    }

    /**
     * gets the exits and room description
     * @method
     */
    private void look()
    {
        checkRoomCreatures();
        currentRoom.printLongDescription();
        printInventory();
        printHealthInfo();
        currentRoom.checkWin(myInventory);
    }
        
    /**
     * gets item by name
     */
    private Item getItemByName(String name)
    {
        for (Item eachItem : myInventory)
            if (eachItem.getName().equals(name))
                return eachItem;
        return null;
    }
    
    private void tryShootGun()
    {
        Item myGun = null; Item myAmmo = null;
            for (Item myItem : myInventory)
            {
                if (myItem.getName().equals("gun"))
                myGun = myItem;
                if (myItem.getName().equals("ammo"))
                myAmmo = myItem;
            }
            if (myGun!=null && myAmmo!=null)
            {
                if (currentRoom.getListCreatures().isEmpty())
                {
                    System.out.println("There is nothing to shoot at.");
                    return;
                }
                if (!currentRoom.getListCreatures().isEmpty())
                {
                    if (myAmmo.getItemAmount()>0)
                    {
                        System.out.println("You shoot your gun at " + currentRoom.getListCreatures().get(0).getName());
                        myAmmo.setItemAmount(myAmmo.getItemAmount()-1);
                        myAmmo.setWeight();
                        currentWeight -= myAmmo.getSingleWeight();
                        //shoots first creature in arraylist (subject to change)
                        currentRoom.getListCreatures().get(0).takeDamage(2);
                        if (currentRoom.getListCreatures().get(0).getHealth()<=0)
                        {
                            System.out.println(currentRoom.getListCreatures().get(0).getName() + " screeches in agony and dies!");
                            currentRoom.getListCreatures().remove(0);
                        }
                    }
                    if (myAmmo.getItemAmount()<=0)
                    {
                        myInventory.remove(myAmmo);
                    }
                }
            }
            if (myGun!=null && myAmmo==null)
            {
                System.out.println("You do not have ammo!");
            } 
            else if (myGun==null && myAmmo==null)
                System.out.println("You do not have a gun!");
    }
    
    /**
     * try to use an item
     * @method
     * @param
     */
    private void tryUse(Command command)
    {
        Item tryUseThisItem = getItemByName(command.getSecondWord());
        {
            if (tryUseThisItem!=null)
            {
                if (tryUseThisItem.getName().equals("firstAid"))
                {
                    addHealth(10);
                    currentWeight -= tryUseThisItem.getSingleWeight();
                    myInventory.remove(tryUseThisItem);
                    return;
                }
                else if (tryUseThisItem.getName().equals("gun"))
                {
                    tryShootGun();
                }
                else if (tryUseThisItem.getName().equals("sedative"))
                {
                    if (!currentRoom.getListCreatures().isEmpty())
                    {
                        
                    }
                }
                else if (tryUseThisItem.getName().equals("knife"))
                {
                    if (!currentRoom.getListCreatures().isEmpty())
                    {
                        System.out.println("You have slashed " + currentRoom.getListCreatures().get(0).getName());
                        currentRoom.getListCreatures().get(0).takeDamage(1);
                        if (currentRoom.getListCreatures().get(0).getHealth()<=0)
                        {
                            System.out.println(currentRoom.getListCreatures().get(0).getName() + " dies.");
                            currentRoom.getListCreatures().remove(0);
                        }
                    }
                }
                else if (tryUseThisItem.getName().equals("flashlight"))
                {
                    if (isInCaves)
                    {
                        if (flashLightUses>0)
                        {
                        System.out.println("You take out your flashlight and light up the caves a bit."); 
                        usingFlashlight = true;
                        flashLightUses--;
                        //create reason to use flashlight
                        if (rand.nextInt(101)<51)
                            {
                                currentRoom.addItem(new Item("ammo", 0.2f, 4));
                                System.out.println("You see some ammo on the other side of the cave floor.");
                            }
                        }
                        else 
                        {
                            System.out.println("The flashlight flickers and dies. You toss it as it will no longer be of use.");
                            myInventory.remove(tryUseThisItem);
                            currentWeight-=tryUseThisItem.getTotalWeight();
                        }
                    }
                    else
                        System.out.println("This item does no good here.");
                }
                else 
                    System.out.println("Use what?");
            }
        }
    }
    
    /**
     * drop an item
     * @method
     * @param
     */
    private void drop(Command command)
    {
        String objectToDrop = command.getSecondWord();
        Item thisItem = null;
        if (!myInventory.isEmpty())
            for (Item object : myInventory)
                if (object.getName().equals(objectToDrop))
                    thisItem = object;
        if (thisItem!=null)
        {
            currentRoom.addItem(thisItem);
            myInventory.remove(thisItem);
            currentWeight -= thisItem.getTotalWeight();
            System.out.println("Removed " + thisItem.getName() + " from inventory!");
        }
        else
            System.out.println("Drop what?");
    }

    /**
     * try to grab an item
     * @method
     */
    private void tryGrab(Command command)
    {
        ArrayList<Item> theseObjects = currentRoom.getRoomItems();
        String itemName = command.getSecondWord();
        Item grabbedItem = null;
        if (!theseObjects.isEmpty())
            for (Item object : theseObjects)
                if (object.getName().equals(itemName))
                    grabbedItem = object;
        if (grabbedItem!=null)
            tryAddInventoryItem(grabbedItem);
        else
            System.out.println("Grab what?");
    }
    
    /**
     * checks to see if there are any creatures in the room
     */
    private void checkRoomCreatures()
    {
        if (!currentRoom.getListCreatures().isEmpty())
        {
            System.out.println("\n----->Creatures in area<-----");
            for (Creature myCreature : currentRoom.getListCreatures())
                System.out.println(myCreature.getName());
        } else System.out.println("There are no creatures around.");
    }
    
    /**
     * take 1 point of damage
     * @method
     */
    private void takeDamage(int damage)
    {
        currentHealth -= damage;
        if (currentHealth<=0)
            System.out.println("You are dead!");
    }

    /**
     * prints health info
     */
    private void printHealthInfo()
    {
        System.out.println("Current HEALTH: " + currentHealth + "\t" + "Current STAMINA: " + currentStamina);
    }
    
    /**
     * gain health points
     * @method
     * @param
     */
    private void addHealth(int value)
    {
        if (currentHealth<MAXHEALTH)
        {
            int currentValue = value - currentHealth;
            System.out.print("Healed for " + currentValue);
            currentHealth += value;
        }
        else if (currentHealth>=MAXHEALTH)
        {
            currentHealth = MAXHEALTH;
            System.out.print("You are at full health!");
        }
    }

    /**
     * adds item to inventory
     * @method
     * @param
     */
    private void tryAddInventoryItem(Item item)
    {
        Item myItem = null;
        ArrayList<Item> itemsInRoom = currentRoom.getRoomItems();
        
        for (Item thisItem : itemsInRoom)
            if (thisItem.getName().equals(item.getName()))
                myItem = thisItem;
        
        if (myItem!=null)
        {
            float totalWeight = myItem.getTotalWeight() + currentWeight;
            Item checkInventory = null;
            
            for (Item inventoryItem : myInventory)
                if (inventoryItem.getName().equals(myItem.getName()))
                    checkInventory = inventoryItem;
            
            if (totalWeight+ currentWeight < MAXCARRYWEIGHT && checkInventory!=null)
            {
                currentWeight += myItem.getTotalWeight();                
                checkInventory.setItemAmount(checkInventory.getItemAmount() + myItem.getItemAmount());
                checkInventory.setWeight();
                currentRoom.removeItem(checkInventory);
            }
            else if (totalWeight + currentWeight < MAXCARRYWEIGHT && checkInventory==null)
            {
                myInventory.add(myItem);
                currentWeight += myItem.getTotalWeight();
                currentRoom.removeItem(myItem);
                System.out.println("Added " + myItem.getName() + " to inventory!");
            }
            else if (totalWeight + currentWeight > MAXCARRYWEIGHT)
                System.out.println("Cannot carry any more weight!");
        } else System.out.println("Cannot find " + item.getName() + ".");
    }
  
    /**
     * goes back room if able
     */    
    private void goBack()
    {
        goRoom(new Command(CommandWord.GO, previousDirection));
    }
    
    /**
     * prints location information upon entering room
     * @method
     */
    private void printUponEnter()
    {
     System.out.println("\n----->You have ENTERED<-----");
     currentRoom.printShortDescription();
     currentRoom.printStoryDescription(); 
     currentRoom.trySpawnWorm();
     if (!currentRoom.getListCreatures().isEmpty())
     {
        System.out.println("You get attacked!");
        for (Creature myCreature : currentRoom.getListCreatures())
        {
            System.out.println("A " + myCreature.getName() + " damages you for " + myCreature.getDamage() + " point of damage!");
            takeDamage(myCreature.getDamage());
        }
     }
     System.out.println("HEALTH: " + currentHealth + " : " + "STAMINA:" + currentStamina); 
     printInventory(); 
    }

    /**
     * prints inventory information
     * @method
     */
    private void printInventory()
    {
        System.out.println("\n----->INVENTORY<-----");
        if (!myInventory.isEmpty())
        {
            System.out.println("Name:  Weight:  Amount:");
            for (Item myItem : myInventory)
                System.out.println(myItem.getName() + " / " + f.format(myItem.getTotalWeight()) + " / " + myItem.getItemAmount());
            }
        else System.out.println("There are no items in your inventory!");
        System.out.println("Current weight: " + getCurrentWeight());
    }
    
    /**
     * gets current weight
     * @method
     * @return
     */
    private float getCurrentWeight()
    {
        return currentWeight;
    }
}

import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;

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
    private int myHealth = 10;
    private int myStamina = 10;
    private static final int MAXCARRYWEIGHT = 20;
    private boolean isInBattle = false;
    private String previousDirection = "";
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
        createRooms();
        myInventory = new ArrayList();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room crashedPod, encampment, acidBog, foggyCliffs, plains, 
        thornForest, deepCanyon, caveEntrance, largeRoomCave, leftCavePath, 
        middleCavePath, rightCavePath, deepLeftCave, deepRightCave, 
        deepMiddleCave, artifactSite;

        // create the rooms
        crashedPod = new Room("crashed landing pod");
        encampment = new Room("make-shift encampment");
        acidBog = new Room("acid bog");
        foggyCliffs = new Room("foggy cliffs");
        plains = new Room("desert plains");
        thornForest = new Room("thorny forest");
        deepCanyon = new Room("deep canyon");
        caveEntrance = new Room("cave entrance");
        leftCavePath = new Room("left cave path");
        middleCavePath = new Room("middle cave path");
        rightCavePath = new Room("right cave path");
        largeRoomCave = new Room("large room inside cave");
        deepLeftCave = new Room("deep left cave");
        deepRightCave = new Room("deep right cave");
        deepMiddleCave = new Room("deep middle cave");
        artifactSite = new Room("artifact site");

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
        crashedPod.addItem(new Item("ammo", 1f, 8));

        encampment.addItem(new Item("knife", 1.2f, 1));
        encampment.addItem(new Item("sedative", 1.2f, 1));
        encampment.addItem(new Item("first-aid", 1.2f, 1));

        crashedPod.addStoryDescription("You are at the landing site of the crashed pod.\nThere are small fires burning and the area is filled with smoke.");
        encampment.addStoryDescription("");
        acidBog.addStoryDescription("");
        foggyCliffs.addStoryDescription("");
        
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
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println("\nCurrent WEIGHT: " + currentWeight);
        printHealthInfo();
        System.out.println("----------------------------------------\nCommands:\n");
        parser.showCommands();
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
        if (!isInBattle && myStamina>0)
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
                myStamina--;
            }
        } else System.out.println("You cannot run!");
        if (currentRoom.equals("largeRoomCave"))
        System.out.println("The entrance to the cave collapses!");
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
    public void printLocationExits()
    {
        currentRoom.getExitString();
    }

    /**
     * gets the exits and room description
     * @method
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
        printInventory();
        printHealthInfo();
        if (!currentRoom.getListCreatures().isEmpty())
        {
            System.out.println(":::Creatures in area:::\n");
            for (Creature myCreature : currentRoom.getListCreatures())
            {
                System.out.println(myCreature.getName());
            }
        }
    }
        
    /**
     * try to use an item
     * @method
     * @param
     */
    private void tryUse(Command command)
    {
        if (command.getSecondWord().equals("firstAid"))
        {
            for (Item eachItem : myInventory)
            {
                if (eachItem.getItemInfo().equals("firstAid"))
                {
                    addHealth(10);
                    myInventory.remove(eachItem);
                    return;
                }
            }
        }
        else if (command.getSecondWord().equals("gun"))
        {
            Item myGun = null; Item myAmmo = null;
            for (Item myItem : myInventory)
            {
                if (myItem.getItemInfo().equals("gun"))
                myGun = myItem;
                if (myItem.getItemInfo().equals("ammo"))
                myAmmo = myItem;
            }
            if (myGun!=null && myAmmo!=null)
            {
                if (!currentRoom.getListCreatures().isEmpty())
                {
                    System.out.println("You shoot your gun at " + currentRoom.getListCreatures().get(0).getName() + " , it screaches in agony and dies!" );
                    currentRoom.getListCreatures().remove(0);
                }
            }
            if (myGun!=null && myAmmo==null)
            {
                System.out.println("You do not have ammo!");
            } 
            else 
            System.out.println("You do not have a gun!");
        }
        else if (command.getSecondWord().equals("sedative"))
        {
            
        }
        else if (command.getSecondWord().equals("knife"))
        {
            for (Item myItem : myInventory)
            {
                if (myItem.getItemInfo().equals("knife"))
                {
                    if (!currentRoom.getListCreatures().isEmpty())
                    {
                        System.out.println("You have slashed " + currentRoom.getListCreatures().get(0).getName());
                        currentRoom.getListCreatures().get(0).takeDamage(1);
                        if (currentRoom.getListCreatures().get(0).getHealth()<=0)
                        currentRoom.getListCreatures().remove(0);
                    }
                }
            }
        }
        else if (command.getSecondWord().equals("flashlight"))
        {
            
        }
        else 
        System.out.println("Use what?");
    }
    
    /**
     * drop an item
     * @method
     * @param
     */
    private void drop(Command command)
    {
        ArrayList<Item> theseObjects = myInventory;
        if (theseObjects!=null)
        {
            for (Item object : theseObjects)
            {
                if (command.getSecondWord().equals(object.getItemInfo()))
                {
                    Item myItem = object;
                    currentRoom.addItem(myItem);
                    myInventory.remove(object);
                    currentWeight -= object.getItemWeight();
                    System.out.println("Dropped " + myItem.getItemInfo() + "\nRemoved " + myItem.getItemInfo() + " from inventory!");
                    return;
                } 
                else
                {
                    System.out.println("drop what?");
                    return;
                }
            }
        }
    }

    /**
     * try to grab an item
     * @method
     */
    private void tryGrab(Command command)
    {
        Collection<String> theseObjects = currentRoom.getRoomItemsHashMap().values();
        if (theseObjects!=null)
        {
            for (String object : theseObjects)
            {
                if (object.equals(command.getSecondWord()) && currentRoom.getRoomItemsHashMap().containsValue(object))
                {
                    System.out.println("Grabbed " + object + "\n");
                    tryAddInventoryItem(object);
                    return;
                }
            }
            System.out.println("try grabbing what?");
        }
    }

    /**
     * take 1 point of damage
     * @method
     */
    private void takeDamage(int damage)
    {
        myHealth -= damage;
        if (myHealth<=0)
            System.out.println("You are dead!");
    }

    /**
     * prints health info
     */
    private void printHealthInfo()
    {
        System.out.println("\nCurrent HEALTH: " + myHealth + "\t" + "Current STAMINA: " + myStamina + "\n");
    }
    
    /**
     * gain health points
     * @method
     * @param
     */
    private void addHealth(int value)
    {
        if (myHealth<10)
            myHealth += value;
        else if (myHealth>=10)
        {
            myHealth = 10;
            System.out.print("You are at full health!");
        }
    }

    /**
     * adds item to inventory
     * @method
     * @param
     */
    private void tryAddInventoryItem(String item)
    {
        Item myItem = null;
        Set<Item> itemsInRoom = currentRoom.getRoomItemsHashMap().keySet();
        for (Item thisItem : itemsInRoom)
        {
            if (thisItem.getItemInfo().equals(item))
            {
                myItem = thisItem;
                if (myItem!=null)
                {
                    if (myItem.getItemWeight() + currentWeight < MAXCARRYWEIGHT)
                    {
                        System.out.println("\nAdded " + myItem.getItemInfo() + " to inventory!");
                        myInventory.add(myItem);
                        currentWeight += myItem.getItemWeight();
                        currentRoom.removeItem(myItem.getItemInfo());
                        return;
                    }
                } else System.out.println("cannot pick up " + item);
            }
        }
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
     System.out.print("----------------------------------------\n:::You have ENTERED:::\n" + currentRoom.getShortDescription() + " " + currentRoom.getStoryDescription()); 
     currentRoom.trySpawnWorm();
     if (currentRoom.getListCreatures()!=null)
     {
        for (Creature myCreature : currentRoom.getListCreatures())
        {
            System.out.println("\n\nA " + myCreature.getName() + " damages you for " + myCreature.getDamage() + " point of damage!\n");
            takeDamage(myCreature.getDamage());
        }
     }
     System.out.print("\n" + "HEALTH: " + myHealth + " : " + "STAMINA:" + myStamina + "\n"); 
     printInventory(); 
     currentRoom.getStoryDescription();
    }

    /**
     * prints inventory information
     * @method
     */
    public void printInventory()
    {
        System.out.print("\nINVENTORY:\n");
        if (myInventory!=null)
        for (Item myItem : myInventory)
        System.out.println(myItem.getItemInfo() + " / " + myItem.getItemWeight());
        else System.out.print("There are no items in your inventory!\n");
        System.out.print("Current weight: " + getCurrentWeight() + "\n----------------------------------------\n");
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

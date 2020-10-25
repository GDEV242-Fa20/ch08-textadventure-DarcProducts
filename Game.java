import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
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
    private Room previousRoom = null;
    private HashMap<Item, String> myInventory;

    private int myHealth = 10;
    private int myStamina = 10;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
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
        
        largeRoomCave.setExit("south", caveEntrance);
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
        deepMiddleCave.setExit("north", artifactSite); //add something to do at artifact site
        
        //add items
        crashedPod.addItem(new Item("flashlight", 1.2f), "flashlight");
        crashedPod.addItem(new Item("battery", .8f), "battery");   
        crashedPod.addItem(new Item("gun", .8f), "gun");
        crashedPod.addItem(new Item("ammo", .8f), "ammo");
        
        encampment.addItem(new Item("knife", .8f), "knife");
        
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
                System.out.println("I don't know what you mean...");
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
                
            case EAT:
                tryEat();
                break;
                
            case SHOOT:
                tryShoot();
                break;
                
            case GRAB:
                tryGrab(command);
                break;
                
            case APPLY:
                tryApply();
                break;
                
            case INSPECT:
                inspect();
                break;
                
            case STAB:
                tryStab();
                break;
            case BACK:
                goBack(previousRoom);
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
        System.out.println("You are lost. You are alone. You wander");
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
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no place to go!");
        }
        else {
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            //System.out.println(currentRoom.getLongDescription());
            printUponEnter();
        }
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
    }
    
    /**
     * try to eat an item
     * @method
     */
    private void tryEat()
    {
        
    }
    
    /**
     * try to shoot a gun
     * @method
     */
    private void tryShoot()
    {
        
    }
    
    /**
     * try to grab an item
     * @method
     */
    private void tryGrab(Command command)
    {
        Collection<String> theseObjects = currentRoom.getRoomHashMap().values();
        if (theseObjects!=null)
        {
            for (String object : theseObjects)
            {
                if (object.equals(command.getSecondWord()) && currentRoom.getRoomHashMap().containsValue(object))
                {
                    System.out.println("Grabbed a " + object + "!");
                    currentRoom.removeItem(object);
                    return;
                }
            }
            System.out.println("Cannot find that around!");
        }
    }
    
    /**
     * try to inspect an item
     * @method
     */
    private void inspect()
    {
        
    }
    
    /**
     * try to stab
     * @method
     */
    private void tryStab()
    {
        
    }
    
    /**
     * try to apply something to an object
     * @method
     */
    private void tryApply()
    {
        
    }
    
    /**
     * take 1 point of damage
     * @method
     */
    private void takeDamage()
    {
        myHealth--;
    }
    
    /**
     * gain health points
     * @method
     */
    private void addHealth(int value)
    {
        myHealth += value;
    }
    
    /**
     * adds item to inventory
     * @method
     * @param
     */
    private void addInventoryItem(Item item, String itemString)
    {
        myInventory.put(item, itemString);
    }
    
    /**
     * goes back a room if able
     * @method
     */
    private void goBack(Room to)
    {
                
    }
    
    /**
     * prints location information upon entering
     * @method
     */
    private void printUponEnter()
    {
        System.out.print("\n\n\nYou have ENTERED:\n\n" + currentRoom.getShortDescription() + " " + currentRoom.getStoryDescription() + 
        "\n" + printInventory() + "\n" + printcurrentHealth() + "\n" + printInventory());
    }
    
    /**
     * prints inventory information
     * @method
     */
    private String printInventory()
    {
        if (myInventory!=null)
        return "Your INVENTORY: \n" + myInventory + "\n";
        else return "";
    }
    
    /**
     * prints current health information
     * @method
     * @return
     */
    private String printcurrentHealth()
    {
        return "Your current HEALTH is: \n" + myHealth + "\n" + "Your current STAMINA is: \n" + myStamina + "\n";
    }
}

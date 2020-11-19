import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Strange Events" application. 
 * "World of Strange Events" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Craig Hussey
 * @version 2020.10.24
 */

public class Room 
{
    private String description = null;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> roomItems;
    private String storyDescription;
    private ArrayList<Creature> roomCreatures;
    private Random rand = new Random();
    private String roomName;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "acid bog" or
     * "crashed pod".
     * @param description The room's description.
     */
    public Room(String description, String roomName) 
    {
        this.roomName = roomName;
        this.description = description;
        exits = new HashMap<>();
        roomCreatures = new ArrayList();
        roomItems = new ArrayList();
    }
    
    /**
     * gets creature list
     * @method
     * @return
     */
    public ArrayList<Creature> getListCreatures()
    {
      return roomCreatures;  
    }
    
    /**
     * try spawn a worm creature
     * @method
     * @return
     */
    public void trySpawnWorm()
    {
        if (rand.nextInt(101)<60)
        roomCreatures.add(new Creature("strange worm",1,1));
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * check if win
     * @method
     * @return
     */
    public Boolean checkWin(ArrayList myInventory)
    {
        Boolean isFinished = false;
        for (Item artifact : (ArrayList<Item>)myInventory)
        {
            if (artifact.getName().equals("artifact"))
            {
                System.out.print("\n\n\n\n\nYOU WIN!!!!!\n\n\n\n\n");
                isFinished = true;
            }else isFinished = false;
        }
        return isFinished;
    }

    /**
     *The short description of the room
     * (the one that was defined in the constructor).
     */
    public void printShortDescription()
    {
        System.out.println(description);
    }

    /**
     * Return a description of the room in the form:
     *     You are in the acid bog
     *     Exits: north west
     * @return A long description of this room
     */
    public void printLongDescription()
    {
        
        System.out.println("\n----->Your CURRENT LOCATION is<-----");
        if (description!=null)
            System.out.println(description);
        printStoryDescription();
        if (!roomItems.isEmpty())
        {
            System.out.println("\n----->ROOM ITEMS / WEIGHT / AMOUNT<-----");
            printItemsInRoom();
        }
        printExitString();       
    }
    
    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public void printExitString()
    {
        System.out.println("\n----->EXITS<-----");
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            System.out.println(exit);
        }
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * gets room hashmap
     * @param
     * @return
     */
    public ArrayList getRoomItems()
    {
        return roomItems;
    }
    
    /**
     * adds item to room
     * @param
     */
    public void addItem(Item thisItem)
    {
        roomItems.add(thisItem);
    }
    
    /**
     * removes item from room
     * @param
     */
    public void removeItem(Item item)
    {
        if (!roomItems.isEmpty())
        {
            roomItems.remove(item);
            System.out.println("Removed " + item.getName() + " from " + description + ".");
        }
    }
    
    /**
     * prints items in room if any
     */
    public void printItemsInRoom()
    {
        if (!roomItems.isEmpty())
        {
            for (Item thisItem : roomItems)
            {
                System.out.println(thisItem.getName() + " / " + thisItem.getTotalWeight() + " / " + thisItem.getItemAmount());
            }
        } else System.out.println("There are no items in room!");
    }
    
    /**
     * adds a description to room based on story
     * @param
     */
    public void addStoryDescription(String story)
    {
        storyDescription = story;
    }
    
    /**
     * gets room name
     * @return
     */
    public String getName()
    {
        return roomName;
    }
    
    /**
     * gets a description to room based on story
     * @method
     * @param
     * @return
     */
    public void printStoryDescription()
    {
        System.out.println(storyDescription);
    }
}


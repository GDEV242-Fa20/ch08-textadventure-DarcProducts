import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

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
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<Item, String> roomItems;
    private String storyDescription;
 
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "acid bog" or
     * "crashed pod".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        roomItems = new HashMap<Item, String>();
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
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the acid bog
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        if (roomItems!=null)
        {
            System.out.print("\n----------------------------------------\nITEMS / WEIGHT:\n"
                + "----------------------------------------\n");
            printItemsInRoom();
        }
        return "\n----------------------------------------\nYour CURRENT LOCATION is:\n"
            + "----------------------------------------\n " + description + ".\n\n" + getStoryDescription() + "\n" + getExitString();
            
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "\n----------------------------------------\nEXITS:\n"
            + "----------------------------------------\n";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        } returnString += "\n----------------------------------------";
        return returnString;
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
    public HashMap getRoomItemsHashMap()
    {
        return roomItems;
    }
    
    /**
     * adds item to room
     * @method
     * @param
     */
    public void addItem(Item thisItem)
    {
        roomItems.put(thisItem, thisItem.getItemInfo());
    }
    
    /**
     * removes item from room
     * @method
     * @param
     */
    public void removeItem(String item)
    {
        if (roomItems!=null)
        {
            roomItems.values().remove(item);
            System.out.println("Removed " + item + " from " + description);
        }
    }
    
    /**
     * prints items in room if any
     * @method
     */
    public void printItemsInRoom()
    {
        if (roomItems!=null)
        {
            for (Item thisItem : roomItems.keySet())
            {
                System.out.println(thisItem.getItemInfo() + " / " + thisItem.getItemWeight() + "\n");
            }
        } else System.out.println("No items in room!");
    }
    
    /**
     * adds a description to room based on story
     * @method
     * @param
     */
    public void addStoryDescription(String story)
    {
        storyDescription = story;
    }
    
    /**
     * gets a description to room based on story
     * @method
     * @param
     * @return
     */
    public String getStoryDescription()
    {
        if (storyDescription!=null)
        return storyDescription;
        else
        return "";
    }
}


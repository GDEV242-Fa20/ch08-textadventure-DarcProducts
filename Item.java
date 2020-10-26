
/**
 * an item with description and weight
 *
 * @author Craig Hussey
 * @version 2020.10.24
 */
public class Item
{
    private String nameOfItem;
    private float weight;
    /**
     * @constructor
     * @param
     */
    public Item(String itemName, float mass)
    {
        nameOfItem = itemName;
        weight = mass;
    }
    
    /**
     * gets item info
     * @method
     * @return
     */
    public String getItemInfo()
    {
        return nameOfItem;
    }
    
    /**
     * gets items weight
     * @method
     * @return
     */
    public float getItemWeight()
    {
        return weight;
    }
}

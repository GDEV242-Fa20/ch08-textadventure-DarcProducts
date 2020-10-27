
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
    private int amount;
    /**
     * @constructor
     * @param
     */
    public Item(String itemName, float mass, int amount)
    {
        this.amount = amount;
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
    
    /**
     * gets items amount
     * @method
     * @return
     */
    public int getItemAmount()
    {
        return amount;
    }
}

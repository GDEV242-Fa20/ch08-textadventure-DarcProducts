/**
 * an item with description and weight
 *
 * @author Craig Hussey
 * @version 2020.10.24
 */
public class Item
{
    private String nameOfItem;
    private float singleWeight;
    private float totalWeight;
    private int amount;
    /**
     * @constructor
     * @param
     */
    public Item(String itemName, float itemWeight, int amount)
    {
        this.amount = amount;
        nameOfItem = itemName;
        totalWeight = itemWeight * amount;
        singleWeight = itemWeight;
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
     * updates weight
     */
    public void setWeight()
    {
        totalWeight = singleWeight * amount;
        singleWeight = totalWeight / amount;
    }
    
    /**
     * gets items weight
     * @return
     */
    public float getTotalWeight()
    {
        return totalWeight;
    }
    
    /**
     * gets single items weight
     * @return
     */
    public float getSingleWeight()
    {
        return singleWeight;
    }
    
    /**
     * gets items amount
     * @return
     */
    public void setItemAmount(int amount)
    {
        this.amount = amount;
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

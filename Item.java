
/**
 * an item with description and weight
 *
 * @author Craig Hussey
 * @version 2020.10.24
 */
public class Item
{
    private String description;
    private float weight = 0f;
    /**
     * @constructor
     */
    public Item(String itemInfo, float mass)
    {
        description = itemInfo;
        weight = mass;
    }
    
    /**
     * gets item info
     * @method
     * @return
     */
    public String getItemInfo()
    {
        return description;
    }
}

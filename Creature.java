
/**
 * creates a creature
 *
 * @author Craig Hussey
 * @version 2020.10.26
 */
public class Creature
{
    private String name;
    private int health;
    private int damage;
    
    public Creature(String name, int health, int damage)
    {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }
    
    /**
     * gets creatures name
     * @method
     * @return
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * gets creatures health
     * @method
     * @return
     */
    public int getHealth()
    {
        return health;
    }
    
    /**
     * gets creatures damage
     * @method
     * @return
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * take damage
     * @method
     */
    public void takeDamage(int amount)
    {
        health -= amount;
    }
}

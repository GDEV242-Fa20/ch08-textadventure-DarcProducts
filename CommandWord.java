/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 * 
 * @author  Craig Hussey
 * @version 2020.10.24
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), LOOK("look"), EAT("eat"), GRAB("grab"),
        SHOOT("shoot"), INSPECT("inspect"), APPLY("apply"), STAB("stab"), BACK("back");
    
    // The command string.
    private String commandString;
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}

package domain;

/**
 * The menu is dynamically build by a file. This class represents an action on a menuitem.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 22/10/2012
 */
public class MenuAction {

    private String action;
    private String description;
    
    private int parent, menuActionId;
    
    /**
     * Creates a new instance of the menuaction.
     * 
     * @param action            The action that needs to be performed when executed.
     * @param description       The description of the action.
     * @param parent            The id of the parent menuaction.
     * @param menuActionId      The id of the menuaction.
     */
    public MenuAction(String action, String description, int parent, int menuActionId) {
        this.setAction(action);
        this.setDescription(description);
        this.setParent(parent);
        this.setMenuActionId(menuActionId);
    }

    /**
     * To lazy to create the rest of the javadoc...
     * We should hire a chinese guy for this!
     */
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getMenuActionId() {
        return menuActionId;
    }

    public void setMenuActionId(int menuActionId) {
        this.menuActionId = menuActionId;
    }
}

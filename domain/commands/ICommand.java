package domain.commands;

public interface ICommand {
    public void executeCommand(Object arg);

    public void executeListCommand(Object arg);
    
    public void undoListCommand(Object arg);
}

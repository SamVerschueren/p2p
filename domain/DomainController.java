package domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import persistence.PersistenceController;
import domain.commands.ICommand;
import domain.filesystem.FileRepresentation;
import domain.filesystem.HashingTask;
import domain.filesystem.Md5Generator;
import domain.filesystem.XmlFileRoot;
import domain.listmodel.ChatListModel;
import domain.listmodel.RootFolderListModel;
import domain.listmodel.TransferTableModel;
import domain.listmodel.UserListModel;
import domain.network.NetworkAction;
import domain.network.NetworkController;
import domain.network.NetworkObject;

/**
 * Every call to the domain happens through this controller. This controller is the gate
 * to the domain classes. You shall not pass!!!
 * 
 * @author Sam Verschueren <sam.verschueren@gmail.com>
 * @since 12/11/2012
 */
public class DomainController {

    private Chat chat;
    private XmlFileRoot xmlFileRoot;
    private List<ICommand> commands;
    private java.util.Observer transferInfoObserver;
    private ListModel<Object> rootFolderModel;
    private TableModel transferTableModel;

    private PersistenceController persistenceController;
    private NetworkController networkController;

    private UserRepository userRepository;
    private XmlFileRepository xmlFileRepository;
    
    private TaskManager taskManager;

    /**
     * Creates a new instance of this controller class.
     */
    public DomainController() {
        this.chat = new Chat();
        
        this.persistenceController = new PersistenceController();
        this.userRepository = new UserRepository(this);
        this.xmlFileRepository = new XmlFileRepository();
        this.taskManager = new TaskManager();
        
        this.networkController = new NetworkController(this);
        this.networkController.addMulticastObserver(this.chat);
        this.networkController.addMulticastObserver(this.userRepository);
        this.networkController.addTCPObserver(this.xmlFileRepository);
        this.networkController.addTCPObserver(this.taskManager);
        
        this.transferTableModel = new TransferTableModel(this.taskManager);
        
        this.networkController.addServerObserver(this.taskManager);

        this.commands = new ArrayList<>();
    }

    /**
     * Returns the username of the logged in user.
     * 
     * @return the username
     */
    public String getUserName() {
        return this.userRepository.getUser().getName();
    }

    /**
     * It logs the user in.
     * 
     * @param userName
     *            The username of the user.
     */
    public void login(String userName) {
        this.userRepository.setUser(new User(userName));

        String home = System.getProperty("user.home");
        
        File p2pDirectory = new File(home + "/p2p");
        File xmlFile = new File(p2pDirectory.getAbsolutePath() + "/" + userName + ".xml");
        
        if(xmlFile.exists()) {
        	this.xmlFileRoot = persistenceController.readXml(xmlFile);
        }
        else {
        	if(!p2pDirectory.exists()) {
        		p2pDirectory.mkdirs();
        	}
        	
        	this.xmlFileRoot = new XmlFileRoot(userName);
        	
        	try {
				persistenceController.createXml(this.xmlFileRoot);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        NetworkObject object = new NetworkObject(NetworkAction.NEW_USER);
        xmlFileRepository.add(userName, xmlFileRoot);
        object.setObject(userRepository.getUser());

        this.sendObject(object);
    }

    /**
     * Send a message over the network. All the magic starts here!
     * 
     * @param message
     *            The message to be send.
     */
    public void sendMessage(String message) {
        NetworkObject object = new NetworkObject(NetworkAction.CHAT_MESSAGE);
        object.setObject(new ChatMessage(message, this.userRepository.getUser()));

        this.sendObject(object);
    }

    /**
     * Sends an object over the wire...
     * 
     * @param object
     *            The object to be send.
     */
    public void sendObject(NetworkObject object) {
        networkController.sendObject(object);
    }

    /**
     * Returns a listmodel with the chatmessages.
     * 
     * @return listModel The listmodel of the chatmessages.
     */
    public ListModel<Object> getChatListModel() {
        return new ChatListModel<Object>(chat);
    }

    /**
     * Returns a listmodel with the users.
     * 
     * @return listModel The listmodel of the users.
     */
    public ListModel<Object> getUserListModel() {
        return new UserListModel<Object>(this.userRepository);
    }

    /**
     * Returns a listmodel with all the rootfolders that will be hashed.
     * 
     * @return listModel The listmodel of the rootfolders.
     */
    public ListModel<Object> getRootFolderListModel() {
        if(rootFolderModel == null)
            rootFolderModel = new RootFolderListModel<Object>(xmlFileRoot);

        return rootFolderModel;
    }
    
    public TableModel getTransferInfoTableModel() {
    	return this.transferTableModel;
    }

    /**
     * Calls the persistencecontroller to create the menu. This controller
     * doesn't need to know where the menu come's from. In our case, it comes
     * from a file. But sssshht, keep it quiet ;).
     * 
     * @return defaultMutableTreeNode The root of the menu.
     */
    public DefaultMutableTreeNode loadMenu() {
        return this.persistenceController.loadMenu();
    }

    /**
     * Adds a command to the command list
     * 
     * @param command
     *            Command to be added
     */
    public void addCommand(ICommand command) {
        commands.add(command);
        command.executeListCommand(rootFolderModel);
    }

    /**
     * Executes al queued commands
     */
    public void executeCommands() {
        if (commands.size() > 0) {
            for (ICommand command : commands) {
                command.executeCommand(xmlFileRoot);
            }
            hashFiles();
            clearCommands();
        }
    }
    
    public void undoCommands(){
        for(ICommand command: commands){
            command.undoListCommand(rootFolderModel);
        }
        clearCommands();
    }

    /**
     * Clears the command queue
     */
    public void clearCommands() {
        commands.clear();
    }

    /**
     * Hashes all files in the xmlFileRoot
     */
    public void hashFiles() {
        Md5Generator generator = new Md5Generator(xmlFileRoot.getTotalFileSize());
        HashingTask task = new HashingTask(generator, xmlFileRoot, persistenceController);
 
        generator.addObserver(taskManager);
        taskManager.addTask(generator.getFileTask());

        new Thread(task).start();
    }

    /**
     * Create a TCP connection with the user.
     * 
     * @param username
     *            The username of the user with whom we have to make a
     *            connection.
     * @throws IOException
     *             When an error occurs when connecting to the user.
     */
    public void tcpConnect(String username) throws IOException {
        User u = userRepository.getUserByName(username);

        // If the user is not you
        if (!u.equals(userRepository.getUser())) {
            networkController.createTCPConnection(u);
        }
    }

    public void downloadXml(String userName) throws IOException {
        User u = userRepository.getUserByName(userName);

        // If the user is not you
        if (!u.equals(userRepository.getUser())) {
            networkController.downloadXml(u);
        }
    }
    
    public void downloadFile(String userName, String[] pathToFile) throws IOException{
       User u = userRepository.getUserByName(userName);
       
       if(!u.equals(userRepository.getUser())){
           XmlFileRoot fileRoot = xmlFileRepository.get(userName);
           FileRepresentation file = fileRoot.getFileByName(pathToFile);
           
           networkController.downloadFile(u, file.getMd5());
       }
    }

    public void registerTransferInfoObserver(java.util.Observer o) {
        transferInfoObserver = o;
    }
    
    public void registerXmlFileRepository(java.util.Observer o) {
    	this.xmlFileRepository.addObserver(o);
    }
    
    /**
     * Get the xml file for the specified user
     * @param username Name of the user who's xml file will be fetched
     * @return Xml file
     */
    public XmlFileRoot getXmlFileForUser(String username){
        return xmlFileRepository.get(username);
    }

    public XmlFileRoot getXmlForCurrentUser() {
    	return xmlFileRepository.get(this.userRepository.getUser().getName());
    }
    
    /**
     * When the application exits.
     */
    public void exit() {
        this.sendObject(new NetworkObject(NetworkAction.DELETE_USER, userRepository.getUser()));
    }
}

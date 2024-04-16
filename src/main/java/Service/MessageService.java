package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    /**
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    /**
     * @param message
     * @return The persisted message if the persistence is successful.
     */
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    /**
     * @param id
     * @return The message if exists
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * @param id
     * @return message if deleted
     */
    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

    /**
     * @param id
     * @return message if updated
     */
    public Message updateMessageById(int id, Message message) {
        return messageDAO.updateMessageById(id, message);
    }

    /**
     * @param account_id
     * @return all messages found by account_id
     */
    public List<Message> getAllMessagesByAccount(int account_id) {
        return messageDAO.getAllMessagesByAccount(account_id);
    }
}

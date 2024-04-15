package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor for a AccountService when a accountDAO is provided.
     * This is used for when a mock accountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AcountDAO.
     * There is no need to modify this constructor.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    /**
     * @return all accounts
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }
    /**
     * @param account an account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }
}

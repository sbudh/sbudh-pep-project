package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (isValidRegistration(account)) {
            boolean successfulRegistration = accountDAO.registerAccount(account);
            if (successfulRegistration) {
                return account;
            }
        }
        return null;
    }

    private boolean isValidRegistration(Account account) {
        return !account.getUsername().isBlank() && 
        account.getPassword().length() >= 4 && 
        !accountDAO.isUsernameExists(account.getUsername());
        }

    public Account userLogin(Account account) {
        Account loggedInAcc = accountDAO.userLogin(account);
        return loggedInAcc; 
    }
}



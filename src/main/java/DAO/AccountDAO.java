package DAO;
import DAO.AccountDAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AccountDAO {
    public boolean registerAccount(Account account) {
        try {Connection connection = ConnectionUtil.getConnection();
            if (!isUsernameExists(account.getUsername())) {
                String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        account.setAccount_id(generatedKeys.getInt(1));
                        return true; // Registration successful
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {  
        }
        return false; 
    }

    public boolean isUsernameExists(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Return true if the username exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; 
    }

    public Account userLogin(Account account) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account loggedInAcc = new Account();
                loggedInAcc.setAccount_id(rs.getInt("account_id"));
                loggedInAcc.setUsername(rs.getString("username"));
                loggedInAcc.setPassword(rs.getString("password"));
                return loggedInAcc; 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
        }
        return null; 
    }
    public boolean isUserExists(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }
        return false; 
    }
}

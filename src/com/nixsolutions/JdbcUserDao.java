package com.nixsolutions;

import com.nixsolutions.dao.AbstractJdbcDao;
import com.nixsolutions.dao.UserDao;
import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao extends User implements UserDao {

    private static final String INSERT_USER_SQL = "INSERT INTO user " +
        "VALUES(NULL,?,?,?,?,?,?,?)";

    private static final String UPDATE_USER = "UPDATE user SET login=?," +
        " password=?, email=?, firstName=?, lastName=?, birthday=?," +
        " role_id=? WHERE id=?";

    private static final String DELETE_USER = "DELETE FROM user WHERE id=?";

    private static final String FIND_ALL_USERS = "SELECT * FROM user";
    private static final String FIND_USER_BY_LOGIN =
        "SELECT * FROM user WHERE login =?";
    private static final String FIND_USER_BY_EMAIL =
        "SELECT * FROM user WHERE email =?";

    private static final String ID = "ID";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD = "PASSWORD";
    private static final String EMAIL = "EMAIL";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String ROLE_ID = "ROLE_ID";

    @Override
    public void create(User user) {
        System.out.println(INSERT_USER_SQL);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
            preparedStatement.setLong(7, user.getRoleId());

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
    }

    @Override
    public void update(User user) {
        System.out.println(UPDATE_USER);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
            preparedStatement.setLong(7, user.getRoleId());
            preparedStatement.setLong(7, user.getId());

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }

    }

    @Override
    public void remove(User user) {
        System.out.println(DELETE_USER);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, user.getId());
            System.out.println(preparedStatement);

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }

    }

    @Override
    public List<User> findAll() {
        System.out.println(FIND_ALL_USERS);
        List<User> list = new ArrayList<>();

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
                list.add(user);
            }

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
        return list;


    }

    @Override
    public User findByLogin(String login) {
        System.out.println(FIND_USER_BY_LOGIN);
        User user = null;

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
            }

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        System.out.println(FIND_USER_BY_EMAIL);
        User user = null;

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
            }

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
        return user;
    }
}

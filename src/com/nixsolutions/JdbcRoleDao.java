package com.nixsolutions;

import com.nixsolutions.dao.AbstractJdbcDao;
import com.nixsolutions.dao.RoleDao;
import com.nixsolutions.entity.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcRoleDao extends Role implements RoleDao {

    private static final String INSERT_ROLE_SQL = "INSERT INTO role " +
        "VALUES(NULL,?)";

    private static final String UPDATE_ROLE = "UPDATE role SET name=? WHERE id=?";

    private static final String DELETE_ROLE = "DELETE FROM role WHERE name=?";

    private static final String FIND_ROLE_BY_NAME =
        "SELECT * FROM role WHERE name =?";

    private static final String ROLE_ID = "ROLE_ID";
    private static final String ROLE_NAME = "NAME";

    @Override
    public void create(Role role) {
        System.out.println(INSERT_ROLE_SQL);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROLE_SQL)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }

    }

    @Override
    public void update(Role role) {
        System.out.println(UPDATE_ROLE);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROLE)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }

    }

    @Override
    public void remove(Role role) {
        System.out.println(DELETE_ROLE);

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROLE)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
    }

    @Override
    public Role findByName(String name) {
        System.out.println(FIND_ROLE_BY_NAME);
        Role role = null;

        try (Connection connection = AbstractJdbcDao.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ROLE_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getLong(ROLE_ID));
                role.setName(resultSet.getString(ROLE_NAME));
            }

        } catch (SQLException e) {
            AbstractJdbcDao.printSQLException(e);
        }
        return role;
    }
}

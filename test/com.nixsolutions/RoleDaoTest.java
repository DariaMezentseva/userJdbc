package com.nixsolutions;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.h2.mvstore.DataUtils.UTF8;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.nixsolutions.entity.Role;
import java.io.File;
import javax.sql.DataSource;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleDaoTest {

    @BeforeClass
    public static void createSchema() throws Exception {
        RunScript.execute("jdbc:h2:tcp://localhost/~/lab13;DB_CLOSE_DELAY=-1",
            "sa", "", "tables.sql", UTF8, false);
    }

    @Before
    public void importDataSet() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanlyInsertDataset(dataSet);
    }

     private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new File("findByName.xml"));
    }

    private void cleanlyInsertDataset(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(
            "org.h2.Driver", "jdbc:h2:tcp://localhost/~/lab13;DB_CLOSE_DELAY=-1", "sa", "");
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    @Test
    public void findByName() throws Exception {
        JdbcRoleDao jdbcRoleDao = new JdbcRoleDao(dataSource());
        Role admin = jdbcRoleDao.findByName("admin");

        assertThat(admin.getName(), is("admin"));
    }

    @Test
    public void returnNullWhenRoleCannotBeFoundByName() throws Exception {
        JdbcRoleDao jdbcRoleDao = new JdbcRoleDao(dataSource());
        Role admin = jdbcRoleDao.findByName("notExist");

        assertThat(admin, is(nullValue()));
    }



    @Test
    public void returnsNullWhenPersonCannotBeFoundByFirstName() throws Exception {
        PersonRepository repository = new PersonRepository(dataSource());
        Person person = repository.findPersonByFirstName("iDoNotExist");

        assertThat(person, is(nullValue()));
    }

    private DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost/~/lab13;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}

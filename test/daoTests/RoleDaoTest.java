package daoTests;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.nixsolutions.JdbcRoleDao;
import com.nixsolutions.dao.RoleDao;
import com.nixsolutions.entity.Role;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleDaoTest extends DBUnitConfig {

    private static final String FILL_DATASET = "resources/dataset/role/fillRole.xml";
    private static final String EMPTY_TABLE = "resources/dataset/role/emptyRole.xml";
    private static final String TABLE = "ROLE";
    private static final String[] IGNORE = {"ID"};
    private RoleDao roleDao;
    private Role[] roles;

    @BeforeClass
    public static void setUpTable() throws Exception {
        new DBUnitConfig().createTable();
    }

    @Before
    public void setUp() throws Exception {
        roleDao = new JdbcRoleDao();
        fillTable(FILL_DATASET);
        roles = new Role[4];

        Role role1 = new Role(1L, "aaa");
        Role role2 = new Role(2L, "bbb");
        Role role3 = new Role(3L, "ccc");
        Role role4 = new Role(4L, "ddd");

        roles[0] = role1;
        roles[1] = role2;
        roles[2] = role3;
        roles[3] = role4;
    }

    @Test
    public void testCreate() throws Exception {
        fillTable(EMPTY_TABLE);
        String after = "resources/dataset/role/roleAfterCreating.xml";
        roleDao.create(roles[0]);
        roleDao.create(roles[1]);
        roleDao.create(roles[2]);
        roleDao.create(roles[3]);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);
    }

    @Test
    public void testUpdate() throws Exception {
        String after = "resources/dataset/role/roleAfterUpdating.xml";

        Role role = roles[2];
        role.setName("updateName");
        roleDao.update(role);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);
    }

    @Test
    public void testRemove() throws Exception {
        String after = "resources/dataset/role/roleAfterRemoving.xml";

        roleDao.remove(roles[3]);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);


    }

    @Test
    public void testFindByName() throws Exception {
        Role role = roleDao.findByName(roles[2].getName());
        assertThat(role.getName(), is("ccc"));
    }

}

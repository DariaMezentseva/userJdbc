package daoTests;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.dao.UserDao;
import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDaoTest extends DBUnitConfig {

    private static final String FILL_DATASET = "resources/dataset/user/fillUser.xml";
    private static final String EMPTY_TABLE = "resources/dataset/user/emptyUser.xml";
    private static final String TABLE = "USER";
    private static final String[] IGNORE = {"ID"};
    private UserDao userDao;
    private User[] users;
    private Role[] roles;

    @BeforeClass
    public static void setUpTable() throws Exception {
        new DBUnitConfig().createTable();
    }

    @Before
    public void setUp() throws Exception {
        userDao = new JdbcUserDao();
        fillTable(FILL_DATASET);

        roles = new Role[2];

        Role role1 = new Role(1L, "aaa");
        Role role2 = new Role(2L, "bbb");

        roles[0] = role1;
        roles[1] = role2;

        users = new User[3];

//        GregorianCalendar calendar = new GregorianCalendar(1990, 0, 1);
//        GregorianCalendar calendar2 = new GregorianCalendar(1991, 1, 2);
//        GregorianCalendar calendar3 = new GregorianCalendar(1992, 2, 3);
//
//        Date date1 = new Date(calendar.getTimeInMillis());
//        Date date2 = new Date(calendar2.getTimeInMillis());
//
//        Date date3 = new Date(calendar3.getTimeInMillis());


        java.sql.Date date1 = java.sql.Date.valueOf("1990-01-01");
        java.sql.Date date2 = java.sql.Date.valueOf("1991-02-02");
        java.sql.Date date3 = java.sql.Date.valueOf("1992-03-03");




//        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(sdf.parse(date));

//        Date date1 = new Calendar.DATE("1990-01-01");
//        Date date2 = (Date)simpleDateFormat.parse("1991-02-02");
//        Date date3 = simpleDateFormat.parse("1992-03-03");


        User user1 = new User(1L, "aaa", "aaa", "aaa", "aaa", "aaa", date1, role1.getId());
        User user2 = new User(2L, "bbb", "bbb", "bbb", "bbb", "bbb", date2, role2.getId());
        User user3 = new User(3L, "ccc", "ccc", "ccc", "ccc", "ccc", date3, role1.getId());

        users[0] = user1;
        users[1] = user2;
        users[2] = user3;

    }

    @Test
    public void testCreate() throws Exception {
        fillTable(EMPTY_TABLE);
        String after = "resources/dataset/user/userAfterCreating.xml";
        userDao.create(users[0]);
        userDao.create(users[1]);
        userDao.create(users[2]);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);
    }

    @Test
    public void testUpdate() throws Exception {
        String after = "resources/dataset/user/userAfterUpdating.xml";

        User user = users[1];
        user.setLogin("updateLogin");
        user.setPassword("updatePassword");
        user.setEmail("updateEmail");
        user.setFirstName("updateFirstName");
        user.setLastName("updateLastName");
        user.setRoleId(2L);

        userDao.update(user);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);
    }

    @Test
    public void testRemove() throws Exception {
        String after = "resources/dataset/user/userAfterRemoving.xml";

        userDao.remove(users[2]);

        ITable expected = getExpectedTable(TABLE, after);
        ITable actual = getActualTable(TABLE);

        assertEqualsIgnoreCols(expected, actual, IGNORE);
    }

    @Test
    public void testFindAll() throws Exception {
        fillTable(FILL_DATASET);
        List<User> expected = new ArrayList<>();
        expected.add(users[0]);
        expected.add(users[1]);
        expected.add(users[2]);

        List<User> actual = userDao.findAll();

        assertEquals(expected, actual);

    }

    @Test
    public void testFindByLogin() throws Exception {
        User user = userDao.findByLogin(users[1].getLogin());
        assertThat(user.getLogin(), is("bbb"));
    }

    @Test
    public void testFindByEmail() throws Exception {
        User user = userDao.findByEmail(users[1].getEmail());
        assertThat(user.getEmail(), is("bbb"));
    }



}

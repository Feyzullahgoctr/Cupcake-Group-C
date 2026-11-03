package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    private static UserMapper userMapper;

    @BeforeAll
    static void setUp(){
        userMapper = new UserMapper(connectionPool);
    }

    @Test
    void login_correctEmailAndPassword_returnUser() throws DatabaseException {
        User user = userMapper.login("kunde1@test.dk", "1234");
        assertNotNull(user);
        assertEquals("kunde1@test.dk", user.getEmail());
    }

    @Test
    void login_wrongPassword_thowsExeption() {
        DatabaseException exception = assertThrows(DatabaseException.class, () ->
                userMapper.login("kunde1@test.dk", "forkert"));
        assertEquals("Forkert email eller adgangskode", exception.getMessage());
    }

    @Test
    void login_emailDoesNotExist_throwsException(){
        DatabaseException exception = assertThrows(DatabaseException.class, () ->
                userMapper.login("Findes@ikke.dk", "1234"));
        assertEquals("Forkert email eller adgangskode", exception.getMessage());
    }
}
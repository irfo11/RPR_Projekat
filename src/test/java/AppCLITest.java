import ba.rpr.AppCLI;
import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.MicronutrientDao;
import ba.rpr.dao.PresenceDao;
import ba.rpr.dao.SourceDao;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppCLITest {
    private static SourceDao sourceDaoMock = mock(SourceDao.class);
    private static MicronutrientDao micronutrientDaoMock = mock(MicronutrientDao.class);
    private static PresenceDao presenceDaoMock = mock(PresenceDao.class);
    private static MockedStatic<DaoFactory> daoFactoryMockedStatic = mockStatic(DaoFactory.class);

    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @BeforeAll
    public static void initilizeMocks() {
        daoFactoryMockedStatic.when(DaoFactory::sourceDao).thenReturn(sourceDaoMock);
        daoFactoryMockedStatic.when(DaoFactory::micronutrientDao).thenReturn(micronutrientDaoMock);
        daoFactoryMockedStatic.when(DaoFactory::presenceDao).thenReturn(presenceDaoMock);
    }
    @AfterAll
    public static void closeStaticMocks() {
        daoFactoryMockedStatic.close();
    }
    @Test
    public void addTest() throws DaoException {
        AppCLI.main(new String[]{"-a", "-s", "testclass"});
        verify(sourceDaoMock).add(Mockito.any(Source.class));
        AppCLI.main(new String[]{"-a", "-m", "testclass", "role", "vitamin"});
        verify(micronutrientDaoMock).add(Mockito.any(Micronutrient.class));
        AppCLI.main(new String[]{"-a", "-p", "micro", "source", "42"});
        verify(presenceDaoMock).add(Mockito.any(Presence.class));
    }

    @Test
    public void updateTest() throws DaoException {
        AppCLI.main(new String[]{"-u", "-s", "1", "testclass"});
        verify(sourceDaoMock).update(eq(1), Mockito.any(Source.class));
        AppCLI.main(new String[]{"-u", "-m", "1", "testclass", "role", "vitamin"});
        verify(micronutrientDaoMock).update(eq(1), Mockito.any(Micronutrient.class));
        AppCLI.main(new String[]{"-u", "-p", "1", "micro", "source", "42"});
        verify(presenceDaoMock).update(eq(1), Mockito.any(Presence.class));
    }

    @Test
    public void deleteTest() throws DaoException {
        AppCLI.main(new String[]{"-d", "-s", "1"});
        verify(sourceDaoMock).delete(eq(1));
        AppCLI.main(new String[]{"-d", "-m", "1"});
        verify(micronutrientDaoMock).delete(eq(1));
        AppCLI.main(new String[]{"-d", "-p", "1"});
        verify(presenceDaoMock).delete(eq(1));
    }

    @Test
    public void getAllTest() throws DaoException {
        AppCLI.main(new String[]{"-g", "-s"});
        verify(sourceDaoMock).getAll();
        AppCLI.main(new String[]{"-g", "-m"});
        verify(micronutrientDaoMock).getAll();
        AppCLI.main(new String[]{"-g", "-p"});
        verify(presenceDaoMock).getAll();
    }

    @Test
    public void badParametersTest() {
        outContent.reset();
        AppCLI.main(new String[]{"-a", "-s"});
        assertEquals("Number of parameters should be 1", outContent.toString());
        outContent.reset();
        AppCLI.main(new String[]{"-a", "-p", "1param"});
        assertEquals("Number of parameters should be 3", outContent.toString());
        outContent.reset();
        AppCLI.main(new String[]{"-a", "-m", "name", "role", "notvitamin"});
        assertEquals("Enter vitamin or mineral", outContent.toString());
    }

    @Test
    public void optionRequiredTest() {
        outContent.reset();
        AppCLI.main(new String[]{"-a"});
        assertEquals("Domain type needed for given action.", outContent.toString());
        outContent.reset();
        AppCLI.main(new String[]{"-u"});
        assertEquals("Domain type needed for given action.", outContent.toString());
        outContent.reset();
        AppCLI.main(new String[]{"-som", "somename", "-s"});
        assertEquals("When using -som option you cannot have other options.", outContent.toString());
    }

}
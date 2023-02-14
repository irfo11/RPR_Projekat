package ba.rpr.business;

import ba.rpr.dao.exceptions.DaoException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

class SourceManagerTest {
    private SourceManager sourceManagerMock = mock(SourceManager.class);

    @Test
    public void validateNameTest() throws DaoException {
        String goodName = "apple";
        Mockito.doCallRealMethod().when(sourceManagerMock).validateName(goodName);
        try {
            sourceManagerMock.validateName(goodName);
        } catch(DaoException e) {
            e.printStackTrace();
            fail();
        }
        Mockito.doCallRealMethod().when(sourceManagerMock).validateName(null);
        assertThrows(DaoException.class,
                () -> {sourceManagerMock.validateName(null);},
                "Source must have name length between 3 to 45 characters.");
        String shortName = "em";
        Mockito.doCallRealMethod().when(sourceManagerMock).validateName(shortName);
        assertThrows(DaoException.class,
                () -> {sourceManagerMock.validateName(shortName);},
                "Source must have name length between 3 to 45 characters.");
        String badName = "ret#5";
        Mockito.doCallRealMethod().when(sourceManagerMock).validateName(badName);
        assertThrows(DaoException.class,
                () -> {sourceManagerMock.validateName(badName);},
                "Source name can only contain letters and whitespaces.");
    }
}
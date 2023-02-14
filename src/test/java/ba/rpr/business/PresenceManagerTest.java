package ba.rpr.business;

import ba.rpr.dao.exceptions.DaoException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

class PresenceManagerTest {
    private PresenceManager presenceManagerMock = mock(PresenceManager.class);

    @Test
    public void validateAmountTest() throws DaoException{
        double good = 12.4;
        Mockito.doCallRealMethod().when(presenceManagerMock).validateAmount(good);
        try {
            presenceManagerMock.validateAmount(good);
        } catch(DaoException e) {
            e.printStackTrace();
            fail();
        }
        double bad = -23.4;
        Mockito.doCallRealMethod().when(presenceManagerMock).validateAmount(bad);
        assertThrows(DaoException.class,
                ()->{presenceManagerMock.validateAmount(bad);},
                "Amount cannot be zero or negative.");
    }
}
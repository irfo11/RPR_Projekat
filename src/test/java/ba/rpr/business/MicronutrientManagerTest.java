package ba.rpr.business;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.MicronutrientDao;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class MicronutrientManagerTest {
    private MicronutrientManager micronutrientManagerMock = mock(MicronutrientManager.class);
    private MicronutrientDao micronutrientDaoMock = mock(MicronutrientDao.class);
    private Micronutrient micronutrient;

    @Test
    public void validateNameTest() throws DaoException {
        String goodName = "B12";
        Mockito.doCallRealMethod().when(micronutrientManagerMock).validateName(goodName);
        try {
            micronutrientManagerMock.validateName(goodName);
        } catch(DaoException e) {
            e.printStackTrace();
            fail();
        }
        Mockito.doCallRealMethod().when(micronutrientManagerMock).validateName(null);
        assertThrows(DaoException.class,
                () -> {micronutrientManagerMock.validateName(null);},
                "Micronutrient must have name length between 1 to 45 characters");
        String badName = "r32#!";
        Mockito.doCallRealMethod().when(micronutrientManagerMock).validateName(badName);
        assertThrows(DaoException.class,
                () -> {micronutrientManagerMock.validateName(badName);},
                "Micronutrient name can only contain alphanumerical characters");
        String containsVitamin = "vitamin C";
        Mockito.doCallRealMethod().when(micronutrientManagerMock).validateName(containsVitamin);
        assertThrows(DaoException.class,
                () -> {micronutrientManagerMock.validateName(containsVitamin);},
                "Micronutrient does not need to have 'vitamin' in its name");
    }

    @Test
    public void addMicronutrientThatAlreadyExist() throws DaoException{
        MockedStatic<DaoFactory> daoFactoryMockedStatic = Mockito.mockStatic(DaoFactory.class);
        daoFactoryMockedStatic.when(DaoFactory::micronutrientDao).thenReturn(micronutrientDaoMock);
        micronutrient = new Micronutrient();
        micronutrient.setName("alreadyExist");
        daoFactoryMockedStatic.when(()->DaoFactory.micronutrientDao().add(micronutrient)).thenThrow(new DaoException("Duplicate entry"));
        Mockito.doCallRealMethod().when(micronutrientManagerMock).add(micronutrient);
        assertThrows(DaoException.class,
                () -> {micronutrientManagerMock.add(micronutrient);},
                "Cannot add micronutrient, because Micronutrient with the same name already exists");
        daoFactoryMockedStatic.verify(DaoFactory::micronutrientDao, times(2));
        verify(micronutrientDaoMock).add(micronutrient);
    }

}
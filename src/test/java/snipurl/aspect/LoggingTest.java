package snipurl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static snipurl.utils.Constants.SIGNATURE_NAME;

@ExtendWith(MockitoExtension.class)
class LoggingTest {
    @Mock
    Logger logger;
    @Mock
    JoinPoint joinPoint;
    @Mock
    Signature signature;
    @InjectMocks
    Logging logging;


    @BeforeEach
    void setUp() {
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn(SIGNATURE_NAME);

    }

    @Test
    void beforeControllerTest() {
        logging.beforeController(joinPoint);
        Mockito.verify(logger, Mockito.times(1)).info(anyString(), anyString());
    }

    @Test
    void beforeServiceTest() {
        logging.beforeService(joinPoint);
        Mockito.verify(logger, Mockito.times(1)).info(anyString(), anyString());
    }
}
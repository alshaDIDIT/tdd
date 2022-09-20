package se.alshadidi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.alshadidi.repo.IAppUserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    IAppUserRepository userRepository;

    Login login;

    @BeforeEach
    void setUp() {
        login = new Login(userRepository);
    }

    @ParameterizedTest
    @CsvSource(value = {"anna, losen, YW5uYQ==", "berit, 123456, YmVyaXQ=", "kalle, password, a2FsbGU=", "kalle, passapo, a2FsbDU="})
    public void login_with_mock(String username, String password, boolean expected) {
        when(userRepository.findAll()).thenReturn(List.of(
                new AppUser(1,"anna", "losen"),
                new AppUser(2,"berit", "123456"),
                new AppUser(3,"kalle", "password"),
                new AppUser(3,"kalle", "passsadf")
                )
        );

        boolean result = login.validate(username, password);

        assertEquals(expected, result);
    }

    @Test
    public void login_with_token() {

    }

}

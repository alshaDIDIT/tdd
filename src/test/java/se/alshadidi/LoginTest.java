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

import static org.junit.jupiter.api.Assertions.*;
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
    @CsvSource(value = {"anna, losen, YW5uYQ==", "berit, 123456, YmVyaXQ=", "kalle, password, a2FsbGU="})
    public void login_with_mock(String username, String password, String expected) {
        when(userRepository.findAll()).thenReturn(List.of(
                new AppUser(1,"anna", "losen"),
                new AppUser(2,"berit", "123456"),
                new AppUser(3,"kalle", "password")
                )
        );

        String result = login.validate(username, password);

        assertEquals(expected, result);
    }

    @Test
    public void login_unhappy_path() {
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(1,"anna", "losen")
                )
        );
        String username = "anna";
        String password = "lololol";

        assertThrows(InvalidCredentialsException.class, () -> login.validate(username, password));
    }

    @ParameterizedTest
    @CsvSource(value = {"YW5uYQ==, true", "YmVyaXQ=, false"})
    public void login_with_token(String token, boolean expected) {
        // given
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(1,"anna", "losen")
                )
        );

        // when
        boolean result = login.validateToken(token);

        // then
        assertEquals(expected, result);
    }

}

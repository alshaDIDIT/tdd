package se.alshadidi;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.alshadidi.repo.IAppUserRepository;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                        new AppUser(1, "anna", "losen", "ADMIN"),
                        new AppUser(2, "berit", "123456", "TEACHER"),
                        new AppUser(3, "kalle", "password", "STUDENT")
                )
        );

        String result = login.validate(username, password);

        assertEquals(expected, result);
    }

    @Test
    public void login_unhappy_path() {
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(1, "anna", "losen", "ADMIN")
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
                        new AppUser(1, "anna", "losen", "ADMIN")
                )
        );

        // when
        boolean result = login.validateToken(token);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void login_with_jwt_token_anna() {
        // given
        when(userRepository.findRole("anna")).thenReturn("ADMIN");
        String username = "anna";
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhbm5hIiwiUm9sZSI6IkFETUlOIn0.KyBpzBcEOBQdhJlxD0aQIW8pVy-jwiNTBIdeTzyb1tPRQVi1HkGmu53xlRDYn0Dj";

        // when
        String result = login.createJwtToken(username);

        // then
        assertEquals(token, result);
    }

    @Test
    public void login_with_jwt_token_berit() {
        // given
        when(userRepository.findRole("berit")).thenReturn("TEACHER");
        String username = "berit";
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiZXJpdCIsIlJvbGUiOiJURUFDSEVSIn0.MrPaAaygKMUvPdOdgdU4Khy9BVHrhCZ-f5n7yevF2_bWxjXfFekXvHnS7fmE30Wg";

        // when
        String result = login.createJwtToken(username);

        // then
        assertEquals(token, result);
    }

    @Test
    public void login_with_jwt_token_kalle() {
        // given
        when(userRepository.findRole("kalle")).thenReturn("STUDENT");
        String username = "kalle";
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJrYWxsZSIsIlJvbGUiOiJTVFVERU5UIn0.wubfKhTSs_uSPIqkM1xkuuSl0J4zr1nj3U8fMMyP1VKOADXjBVjVqfR5oz1rEJrJ";

        // when
        String result = login.createJwtToken(username);

        // then
        assertEquals(token, result);
    }

    @Test
    public void return_role_test_anna() {
        when(userRepository.findRole("anna")).thenReturn("ADMIN");
        String username = "anna";
        String expected = "ADMIN";

        String result = login.returnRole(username);

        assertEquals(expected, result);
    }

    @Test
    public void return_role_test_berit() {
        when(userRepository.findRole("berit")).thenReturn("TEACHER");
        String username = "berit";
        String expected = "TEACHER";

        String result = login.returnRole(username);

        assertEquals(expected, result);
    }

    @Test
    public void return_role_test_kalle() {
        when(userRepository.findRole("kalle")).thenReturn("STUDENT");
        String username = "kalle";
        String expected = "STUDENT";

        String result = login.returnRole(username);

        assertEquals(expected, result);
    }

}

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

    @ParameterizedTest
    @CsvSource(value = {
            "1, eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhbm5hIiwiUm9sZSI6IkFETUlOIn0.KyBpzBcEOBQdhJlxD0aQIW8pVy-jwiNTBIdeTzyb1tPRQVi1HkGmu53xlRDYn0Dj",
            "2, eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiZXJpdCIsIlJvbGUiOiJURUFDSEVSIn0.MrPaAaygKMUvPdOdgdU4Khy9BVHrhCZ-f5n7yevF2_bWxjXfFekXvHnS7fmE30Wg",
            "3, eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJrYWxsZSIsIlJvbGUiOiJTVFVERU5UIn0.wubfKhTSs_uSPIqkM1xkuuSl0J4zr1nj3U8fMMyP1VKOADXjBVjVqfR5oz1rEJrJ"})
    public void login_with_jwt_token(int id, String expected) {
        // given
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(1, "anna", "losen", "ADMIN"),
                        new AppUser(2, "berit", "123456", "TEACHER"),
                        new AppUser(3, "kalle", "password", "STUDENT")
                )
        );

        // when
        String result = login.createJwtToken(id);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void verify_token_test() {
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(1, "anna", "losen", "ADMIN")
                )
        );
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhbm5hIiwiUm9sZSI6IkFETUlOIn0.KyBpzBcEOBQdhJlxD0aQIW8pVy-jwiNTBIdeTzyb1tPRQVi1HkGmu53xlRDYn0Dj";

        List<Map<String, String>> result = login.verifyToken(jwtToken);

        List<Map<String, String>> expected = new ArrayList<>();
        expected.add(1, Map.of("GRADING", "READ"));
        expected.add(2, Map.of("GRADING", "WRITE"));
        expected.add(3, Map.of("COURSE_FEEDBACK", "READ"));
        expected.add(4, Map.of("COURSE_FEEDBACK", "WRITE"));

        assertIterableEquals(expected, result);
    }

    @Test
    public void verify_token_test_two() {
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(2, "berit", "123456", "TEACHER")
                )
        );
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiZXJpdCIsIlJvbGUiOiJURUFDSEVSIn0.MrPaAaygKMUvPdOdgdU4Khy9BVHrhCZ-f5n7yevF2_bWxjXfFekXvHnS7fmE30Wg";

        List<Map<String, String>> result = login.verifyToken(jwtToken);

        List<Map<String, String>> expected = new ArrayList<>();
        expected.add(1, Map.of("GRADING", "READ"));
        expected.add(2, Map.of("GRADING", "WRITE"));
        expected.add(3, Map.of("COURSE_FEEDBACK", "READ"));

        assertIterableEquals(expected, result);
    }

    @Test
    public void verify_token_test_three() {
        when(userRepository.findAll()).thenReturn(List.of(
                        new AppUser(3, "kalle", "password", "STUDENT")
                )
        );
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJrYWxsZSIsIlJvbGUiOiJTVFVERU5UIn0.wubfKhTSs_uSPIqkM1xkuuSl0J4zr1nj3U8fMMyP1VKOADXjBVjVqfR5oz1rEJrJ";

        List<Map<String, String>> result = login.verifyToken(jwtToken);

        List<Map<String, String>> expected = new ArrayList<>();
        expected.add(1, Map.of("GRADING", "READ"));
        expected.add(3, Map.of("COURSE_FEEDBACK", "READ"));
        expected.add(3, Map.of("COURSE_FEEDBACK", "WRITE"));

        assertIterableEquals(expected, result);
    }

}

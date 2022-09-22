package se.alshadidi;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import se.alshadidi.repo.IAppUserRepository;

import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Login {

    private final IAppUserRepository userRepository;

    public Login(IAppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validate(String username, String password) {
        if (userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .get().getPassword().equals(password)) {
            byte[] usernameAsBytes = username.getBytes();
            byte[] usernameAsBase64 = Base64.getEncoder().encode(usernameAsBytes);
            return new String(usernameAsBase64);
        }
        throw new InvalidCredentialsException("Wrong password or username");
    }

    public boolean validateToken(String token) {
        byte[] backAsBase64Bytes = token.getBytes();
        byte[] backAsBytes = Base64.getDecoder().decode(backAsBase64Bytes);
        String backAsOriginal = new String(backAsBytes);

        return userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(backAsOriginal));
    }

    public String createJwtToken(int id) {
        AppUser user = userRepository.findAll().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow();

        Key key = Keys.hmacShaKeyFor("DethärÄrEnSuperKompliceradTextSomIngenKommerÅt".getBytes());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of("Role", user.getRole()))
                .signWith(key)
                .compact();
    }

}

package se.alshadidi;

import se.alshadidi.repo.IAppUserRepository;

import java.util.Base64;

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
            String byte64String = new String(usernameAsBase64);
            return byte64String;
        }
        throw new InvalidCredentialsException("Wrong password or username");
    }

    public boolean validateToken(String token) {
        byte[] backAsBase64Bytes = token.getBytes();
        byte[] backAsBytes = Base64.getDecoder().decode(backAsBase64Bytes);
        String backAsOriginal = new String(backAsBytes);

        return (userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(backAsOriginal))
                .findFirst().get().getUsername().equals(backAsOriginal));
    }
}

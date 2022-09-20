package se.alshadidi;

import se.alshadidi.repo.IAppUserRepository;

public class Login {

    private final IAppUserRepository userRepository;

    public Login(IAppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validate(String username, String password) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .get().getPassword().equals(password);
    }
}

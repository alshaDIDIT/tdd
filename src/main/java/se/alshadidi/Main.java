package se.alshadidi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.alshadidi.repo.IAppUserRepository;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    IAppUserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new AppUser(1, "anna", "losen"));
        userRepository.findAll().forEach(appUser -> System.out.println(appUser));
    }
}

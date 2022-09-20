package se.alshadidi;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser {

    @Id
    private int id;
    private String username;
    private String password;
}

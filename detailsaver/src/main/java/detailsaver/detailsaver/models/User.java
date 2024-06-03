package detailsaver.detailsaver.models;

import jakarta.persistence.*;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Getter
    private Integer userId;
    @Getter
    private String email;
    @Getter
    private String password;


}

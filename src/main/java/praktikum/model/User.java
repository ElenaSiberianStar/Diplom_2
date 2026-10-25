package praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;    // Электронная почта
    private String password; // Пароль[cite: 1]
    private String name;     // Имя пользователя[cite: 1]
}

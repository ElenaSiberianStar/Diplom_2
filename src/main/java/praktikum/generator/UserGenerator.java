package praktikum.generator;

import praktikum.model.User;

public class UserGenerator {

    // Генерируем случайного уникального пользователя для тестов
    public static User getRandom() {
        // Используем текущее время в миллисекундах, чтобы email всегда был уникальным
        String email = "elena_qa_" + System.currentTimeMillis() + "@yandex.ru";
        return new User(email, "password123", "Elena");
    }
}

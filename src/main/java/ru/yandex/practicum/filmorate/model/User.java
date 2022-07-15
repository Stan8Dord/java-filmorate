package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private long id;
    @NotNull @Email
    private final String email;
    @NotNull @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
    //private Map<Long, Integer> friends = new HashMap<>();
    private Set<Long> friends = new HashSet<>();

    public User(@JsonProperty("email") String email, @JsonProperty("login") String login,
                @JsonProperty("name") String name, @JsonProperty("birthday") LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}

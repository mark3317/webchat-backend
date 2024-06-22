package ru.markn.webchat.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false, unique = true)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "name_role",
            joinColumns = @JoinColumn(name = "name_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )

    private Set <Role> roles;
}
package br.com.bejacob.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "Users")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
// @Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    public User(
            Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;

    @UpdateTimestamp
    @Column(name = "create_at", updatable = false) // só dizendo que no banco o nome é com underline e não é atualizavel
    private LocalDateTime createdAt;

    public User(
            String name,
            String email,
            String password,
            String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getLogin() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Temporario até implementação das classes filhas cliente e admin
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    @NonNull
    public String getUsername() {
        return this.email;
    }
}

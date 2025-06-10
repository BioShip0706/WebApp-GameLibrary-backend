package it.BioShip.GameLibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.BioShip.GameLibrary.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;




@Entity
@Table(name = "user")
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends CreationUpdate implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false, length = 15, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authorities",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")}
    )
    private Set<Authority> authorities = new HashSet<>(); //creo la N a N


    /* Inherited methods from org.springframework.security.core.userdetails.UserDetails */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(role ->
                new SimpleGrantedAuthority(role.getAuthorityName())
        ).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && enabled == user.enabled &&  Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, enabled, authorities);
    }
}



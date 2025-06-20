package it.BioShip.GameLibrary.payload.response;



import it.BioShip.GameLibrary.entity.Authority;
import it.BioShip.GameLibrary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserResponse {

    private int id;
    private String username;
    private LocalDateTime createdAt;
    List<String> authorities;


    static public UserResponse fromUserDetailsToUserResponse(User user)
    {
        List<String> authoritiesNames = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(); //mi serve ROLE_ADMIN per far comparire i dev button nelle Cards

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt(),
                authoritiesNames
        );
    }

}

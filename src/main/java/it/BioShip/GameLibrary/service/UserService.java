package it.BioShip.GameLibrary.service;



import it.BioShip.GameLibrary.entity.Authority;
import it.BioShip.GameLibrary.entity.User;
import it.BioShip.GameLibrary.exception.ResourceNotFoundException;
import it.BioShip.GameLibrary.payload.response.UserResponse;
import it.BioShip.GameLibrary.repository.AuthorityRepository;
import it.BioShip.GameLibrary.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;


    // cambio ruolo
    // cambio password
    // get me

    @Transactional
    public ResponseEntity<?> updateAuthorities(int id, Set<String> authorities) {
        // verifica esistenza utente
        User u = userRepository.findById(id).orElseThrow( (() -> new ResourceNotFoundException("User", "id", id)));
        // trasformare Set<String> in Set<Authority>
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);
        if(auths.isEmpty())
            return new ResponseEntity("Authorities not found", HttpStatus.NOT_FOUND);
        // settare il Set<Authority> sullo user e salvare
        u.setAuthorities(auths);
        userRepository.save(u);
        return new ResponseEntity("Authorities added", HttpStatus.OK);
    }


    public ResponseEntity<?> getMe(UserDetails userDetails) {
        UserResponse u = UserResponse.fromUserDetailsToUserResponse((User) userDetails);
        return new ResponseEntity<>(u,HttpStatus.OK);
    }


}

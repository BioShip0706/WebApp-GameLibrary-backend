package it.BioShip.GameLibrary.controller;


import it.BioShip.GameLibrary.service.AuthorityService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authority")
@Validated
@RequiredArgsConstructor
public class AuthorityController {

    // aggiungere ruolo
    // togliere visibilita' al ruolo
    // cambiare il ruolo di default

    private final AuthorityService authorityService;

    // aggiungere ruolo
    @Secured("ROLE_ADMIN")
    @PostMapping("/{newAuthority}")
    public ResponseEntity<?> addAuthority(@PathVariable @Size(max = 30, min = 7) @NotEmpty String newAuthority) {
        return authorityService.addAuthority(newAuthority);
    }

    // togliere visibilita' al ruolo
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<?> switchVisibility(@PathVariable @Min(1) byte id) {
        return authorityService.switchVisibility(id);
    }

    // cambiare il ruolo di default
    @Secured("ROLE_ADMIN")
    @PutMapping("/default/{id}")
    public ResponseEntity<?> setNewDefault(@PathVariable @Min(1) byte id) {
        return authorityService.setNewDefault(id);

    }

}

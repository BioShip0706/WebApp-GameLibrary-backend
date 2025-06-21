package it.BioShip.GameLibrary.controller;


import it.BioShip.GameLibrary.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("platform")
@Validated
public class PlatformController
{

    private final PlatformService platformService;


    @GetMapping("/getAllPlatforms")
    public ResponseEntity<?> getAllPlatforms()
    {
        return platformService.getAllPlatforms();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addNewPlatform")
    public ResponseEntity<?> addNewPlatform(@RequestParam String name)
    {
        return platformService.addNewPlatform(name);
    }
}

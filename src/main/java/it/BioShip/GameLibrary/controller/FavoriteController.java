package it.BioShip.GameLibrary.controller;

import it.BioShip.GameLibrary.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("favorite")
@Validated
public class FavoriteController
{
    private final FavoriteService favoriteService;
    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente
    @GetMapping("/getAllFavoriteGamesIds")
    public ResponseEntity<?> getAllFavoriteGamesIds(@RequestParam int userId)
    {
        return favoriteService.getAllFavoriteGamesIds(userId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente
    @PostMapping("/addNewFavoriteGame")
    public ResponseEntity<?> addNewFavoriteGame(@RequestParam int userId, @RequestParam long gameId)
    {
        return favoriteService.addNewFavoriteGame(userId,gameId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente
    @DeleteMapping("/deleteFavoriteGame")
    public ResponseEntity<?> deleteFavoriteGame(@RequestParam int userId, @RequestParam long gameId)
    {
        return favoriteService.deleteFavoriteGame(userId,gameId);
    }

}

package it.BioShip.GameLibrary.controller;

import it.BioShip.GameLibrary.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
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
    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente (nessun mismatch tra l'id utente passato e l'id estratto dal token ricevuto nell'header
    @GetMapping("/getAllFavoriteGamesIds")
    public ResponseEntity<?> getAllFavoriteGamesIds(@RequestParam int userId, HttpServletRequest request)
    {
        return favoriteService.getAllFavoriteGamesIds(userId, request);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente
    @PostMapping("/addNewFavoriteGame")
    public ResponseEntity<?> addNewFavoriteGame(@RequestParam int userId, @RequestParam long gameId, HttpServletRequest request)
    {
        return favoriteService.addNewFavoriteGame(userId,gameId,request);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')") //controllare anche che sia io l'utente
    @DeleteMapping("/deleteFavoriteGame")
    public ResponseEntity<?> deleteFavoriteGame(@RequestParam int userId, @RequestParam long gameId, HttpServletRequest request)
    {
        return favoriteService.deleteFavoriteGame(userId,gameId, request);
    }

}

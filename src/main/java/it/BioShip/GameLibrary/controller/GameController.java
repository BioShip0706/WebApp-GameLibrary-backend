package it.BioShip.GameLibrary.controller;

import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@Validated
public class GameController
{

    private final GameService gameService;


    @GetMapping("/getAllGames")
    public ResponseEntity<?> getAllGames()
    {
        return gameService.getAllGames();
    }

    @GetMapping("/getGameById/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable long gameId)
    {
        return gameService.getGameById(gameId);
    }

    @GetMapping("/getAllFavoriteGames")
    public ResponseEntity<?> getAllFavoriteGames(@RequestParam int userId)
    {
        return gameService.getAllFavoriteGames(userId);
    }


    @PostMapping("/addNewGame")
    public ResponseEntity<?> addNewGame(@RequestBody GameRequest gameRequest)
    {
        return gameService.addNewGame(gameRequest);
    }

    @GetMapping("/filterGames")
    public ResponseEntity<?> filterGames(@RequestParam(required = false) List<Long> genreIds, @RequestParam(required = false) List<Long> platformIds)
    {
        return gameService.filterGames(genreIds,platformIds);
    }

    @GetMapping("/searchGame/{lettere}")
    public ResponseEntity<?> searchGame(@PathVariable String lettere)
    {
        return gameService.searchGame(lettere);
    }

}

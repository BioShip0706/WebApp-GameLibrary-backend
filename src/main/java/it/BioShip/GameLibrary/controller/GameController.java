package it.BioShip.GameLibrary.controller;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@Validated
public class GameController
{

    private final GameService gameService;
    private final GameRepository gameRepository;


    /*@GetMapping("/getAllGames")
    public ResponseEntity<?> getAllGames()
    {
        return gameService.getAllGames();
    }*/

    @GetMapping("/getAllGameCardsByPage")
    public ResponseEntity<?> getAllGameCardsByPage(@RequestParam int page, @RequestParam int perPage)
    {
        return gameService.getAllGameCardsByPage(page,perPage);
    }

    @GetMapping("/getCountOfAllGames")
    public ResponseEntity<?> getCountOfAllGames()
    {
        return gameService.getCountOfAllGames();
    }

    @GetMapping("/getGameById/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable long gameId)
    {
        return gameService.getGameById(gameId);
    }

    /*@GetMapping("/getAllFavoriteGames")
    public ResponseEntity<?> getAllFavoriteGames(@RequestParam int userId)
    {
        return gameService.getAllFavoriteGames(userId);
    }*/

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("deleteGameById")
    public ResponseEntity<?> deleteGameById(@RequestParam long gameId)
    {
        return gameService.deleteGameById(gameId);

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addNewGame")
    public ResponseEntity<?> addNewGame(@RequestBody GameRequest gameRequest)
    {
        return gameService.addNewGame(gameRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/editGameById")
    public ResponseEntity<?> editGameById(@RequestParam long gameId, @RequestBody GameRequest gameRequest)
    {
        return gameService.editGameById(gameId, gameRequest);
    }

    @GetMapping("getGameIdByTitle")
    public ResponseEntity<?> getGameIdByTitle(@RequestParam String title)
    {
        return gameService.getGameIdByTitle(title);
    }

    @GetMapping("/filterGames")
    public ResponseEntity<?> filterGames(@RequestParam(required = false) List<Long> genreIds,
                                         @RequestParam(required = false) List<Long> platformIds,
                                         @RequestParam(required = false) String scoreOrder,
                                         @RequestParam(required = false) String releaseDateOrder,
                                         @RequestParam(required = false) List<Long> favoriteIds,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int perPage) //size
    {
        return gameService.filterGames(genreIds,platformIds, scoreOrder, releaseDateOrder, favoriteIds, page, perPage);
    }

    @GetMapping("/searchGame/{lettere}")
    public ResponseEntity<?> searchGame(@PathVariable String lettere)
    {
        return gameService.searchGame(lettere);
    }

}

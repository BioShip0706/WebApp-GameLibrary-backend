package it.BioShip.GameLibrary.controller;

import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.service.GameService;
import it.BioShip.GameLibrary.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("genre")
@Validated
public class GenreController
{

    private final GenreService genreService;


    @GetMapping("/getAllGenres")
    public ResponseEntity<?> getAllGenres()
    {
        return genreService.getAllGenres();
    }

    @PostMapping("/addNewGenre")
    public ResponseEntity<?> addNewGenre(@RequestParam String name)
    {
        return genreService.addNewGenre(name);
    }
}

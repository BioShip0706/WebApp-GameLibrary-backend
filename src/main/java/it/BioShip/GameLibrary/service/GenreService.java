package it.BioShip.GameLibrary.service;

import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.repository.GenreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService
{
    private final GenreRepository genreRepository;

    public ResponseEntity<?> getAllGenres()
    {
        //return new ResponseEntity<>(genreRepository.findAll(), HttpStatus.OK); //RITORNARE IN ORDINE ALFABETICO

        return new ResponseEntity<>(genreRepository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addNewGenre(String name)
    {
        if(genreRepository.existsByName(name))
        {
            return new ResponseEntity<>("Genre already exists",HttpStatus.CONFLICT);
        }

        Genre newGenre = new Genre();
        newGenre.setName(name);


        return new ResponseEntity<>(newGenre,HttpStatus.OK);
    }
}

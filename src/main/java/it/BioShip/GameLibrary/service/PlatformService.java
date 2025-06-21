package it.BioShip.GameLibrary.service;

import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import it.BioShip.GameLibrary.repository.GenreRepository;
import it.BioShip.GameLibrary.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatformService
{
    private final PlatformRepository platformRepository;

    public ResponseEntity<?> getAllPlatforms()
    {
        //return new ResponseEntity<>(platformRepository.findAll(), HttpStatus.OK); //RITORNARE IN ORDINE ALFABETICO

        return new ResponseEntity<>(platformRepository.findAllByOrderByNameAsc(),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> addNewPlatform(String name)
    {
        if(platformRepository.existsByName(name))
        {
            return new ResponseEntity<>("Platform already exists",HttpStatus.CONFLICT);
        }

        Platform newPlatform = new Platform();
        newPlatform.setName(name);


        return new ResponseEntity<>(newPlatform,HttpStatus.OK);
    }
}

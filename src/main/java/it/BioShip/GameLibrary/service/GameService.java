package it.BioShip.GameLibrary.service;


import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.repository.GenreRepository;
import it.BioShip.GameLibrary.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService
{
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final GenreRepository genreRepository;


    @Transactional
    public ResponseEntity<?> getAllGames()
    {
        //List<Game> allGames = gameRepository.findAll(); //gameRepository.findAll() mi servono
        //return new ResponseEntity(allGames, HttpStatus.OK);

        List<GameCardResponse> gameCards = gameRepository.findAllGameCards();
        return new ResponseEntity<>(gameCards,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getAllFavoriteGames(@RequestParam int userId)
    {
        //List<Game> allGames = gameRepository.findAll(); //gameRepository.findAll() mi servono
        //return new ResponseEntity(allGames, HttpStatus.OK);

        List<GameCardResponse> getAllFavoriteGames = gameRepository.findAllFavoriteGames(userId);
        return new ResponseEntity<>(getAllFavoriteGames,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addNewGame(GameRequest gameRequest)
    {
        Game newGame = new Game();

        newGame.setTitle(gameRequest.getTitle());
        newGame.setDescription(gameRequest.getDescription());
        newGame.setDeveloper(gameRequest.getDeveloper());
        newGame.setPublisher(gameRequest.getPublisher());
        newGame.setReleaseDate(gameRequest.getReleaseDate());
        newGame.setScore(gameRequest.getScore());
        newGame.setImageURL(gameRequest.getImageURL());

        List<Platform> platformList = platformRepository.findAllById(gameRequest.getPlatformsIds()); //Game vuole oggetti Platform, che ottengo dal repository passandogli tutti gli id messi nella request
        newGame.setPlatforms(platformList);

        List<Genre> genreList = genreRepository.findAllById(gameRequest.getGenresIds());
        newGame.setGenres(genreList);


        return new ResponseEntity<>(newGame,HttpStatus.OK);
    }


    public ResponseEntity<?> filterGames(List<Long> genreIds, List<Long> platformIds)
    {

        if (genreIds != null && platformIds != null)
        {
            //return new ResponseEntity<>(gameRepository.findByGenres_IdInAndPlatforms_IdIn(genreIds, platformIds),HttpStatus.OK);
            return new ResponseEntity<>(gameRepository.findByGenresIdsAndPlatformsIds(genreIds, platformIds, genreIds.size(), platformIds.size()),HttpStatus.OK);
        }
        else if (genreIds != null)
        {
            //return new ResponseEntity<>(gameRepository.findByGenres_IdIn(genreIds),HttpStatus.OK);
            return new ResponseEntity<>(gameRepository.findByGenresIds(genreIds, genreIds.size()),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(gameRepository.findByPlatformsIds(platformIds, platformIds.size()),HttpStatus.OK);

        }

    }


    public ResponseEntity<?> getGameById(long gameId)
    {
        if(!gameRepository.existsById(gameId))
        {
            return new ResponseEntity<>("Game not found!",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(gameRepository.findById(gameId),HttpStatus.OK);
    }

    public ResponseEntity<?> searchGame(String lettere)
    {
        List<Game> games = gameRepository.findTop10ByTitleStartingWith(lettere);

        /*if(!games.isEmpty())
        {
            return new ResponseEntity<>(games,HttpStatus.OK);
        }

        return new ResponseEntity<>(games,HttpStatus.NOT_FOUND);*/

        return new ResponseEntity<>(games,HttpStatus.OK);
    }
}

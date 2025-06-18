package it.BioShip.GameLibrary.service;


import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import it.BioShip.GameLibrary.payload.response.GameFullResponse;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.repository.GenreRepository;
import it.BioShip.GameLibrary.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        Set<Platform> platformList = (Set<Platform>) platformRepository.findAllById(gameRequest.getPlatformsIds()); //Game vuole oggetti Platform, che ottengo dal repository passandogli tutti gli id messi nella request
        newGame.setPlatforms(platformList);

        Set<Genre> genreList = (Set<Genre>) genreRepository.findAllById(gameRequest.getGenresIds());
        newGame.setGenres(genreList);


        return new ResponseEntity<>(newGame,HttpStatus.OK);
    }


    public ResponseEntity<?> filterGames(List<Long> genreIds, List<Long> platformIds, String scoreOrder, String releaseDateOrder, List<Long> favoriteids )
    {

        /*if (genreIds != null && platformIds != null)
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

        }*/

        List<Game> filteredGames = gameRepository.findFilteredGames(genreIds,platformIds,scoreOrder,releaseDateOrder,favoriteids);
        return new ResponseEntity<>(filteredGames,HttpStatus.OK);

    }


    @Transactional
    public ResponseEntity<?> getGameById(long gameId)
    {
        if(!gameRepository.existsById(gameId))
        {
            return new ResponseEntity<>("Game not found!",HttpStatus.NOT_FOUND);
        }

        Game gameWithGenres = gameRepository.findByIdFullFetchGenres(gameId);
        Game gameWithPlatforms = gameRepository.findByIdFullFetchPlatforms(gameId);

        gameWithPlatforms.setGenres(gameWithGenres.getGenres());

        return new ResponseEntity<>(gameWithPlatforms,HttpStatus.OK);

        //return new ResponseEntity<>(gameWithPlatforms,HttpStatus.OK);
        //GameFullResponse game = gameRepository.getFullGameInfoById(gameId);

        //Game game = gameRepository.findById(gameId).orElseThrow();
        //return new ResponseEntity<>(game,HttpStatus.OK);
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

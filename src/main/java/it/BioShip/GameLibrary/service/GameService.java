package it.BioShip.GameLibrary.service;


import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import it.BioShip.GameLibrary.payload.request.GameRequest;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import it.BioShip.GameLibrary.payload.response.GameFullResponse;
import it.BioShip.GameLibrary.payload.response.GameSearchResponse;
import it.BioShip.GameLibrary.repository.FavoriteRepository;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.repository.GenreRepository;
import it.BioShip.GameLibrary.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameService
{
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final GenreRepository genreRepository;
    private final FavoriteRepository favoriteRepository;


    /*@Transactional
    public ResponseEntity<?> getAllGames()
    {
        //List<Game> allGames = gameRepository.findAll(); //gameRepository.findAll() mi servono
        //return new ResponseEntity(allGames, HttpStatus.OK);

        List<GameCardResponse> gameCards = gameRepository.findAllGameCards();
        return new ResponseEntity<>(gameCards,HttpStatus.OK);
    }*/

    /*@Transactional
    public ResponseEntity<?> getAllFavoriteGames(@RequestParam int userId)
    {
        //List<Game> allGames = gameRepository.findAll(); //gameRepository.findAll() mi servono
        //return new ResponseEntity(allGames, HttpStatus.OK);

        List<GameCardResponse> getAllFavoriteGames = gameRepository.findAllFavoriteGames(userId);
        return new ResponseEntity<>(getAllFavoriteGames,HttpStatus.OK);
    }*/

    @Transactional
    public ResponseEntity<?> addNewGame(GameRequest gameRequest)
    {

        if(gameRepository.existsByTitle(gameRequest.getTitle()))
        {
            return new ResponseEntity<>("A game with this name already exists!", HttpStatus.BAD_REQUEST);
        }

        Game newGame = new Game();

        newGame.setTitle(gameRequest.getTitle());
        newGame.setDescription(gameRequest.getDescription());
        newGame.setDeveloper(gameRequest.getDeveloper());
        newGame.setPublisher(gameRequest.getPublisher());
        newGame.setReleaseDate(gameRequest.getReleaseDate());
        newGame.setScore(gameRequest.getScore());
        newGame.setImageURL(gameRequest.getImageURL());

        Set<Platform> platformList = new HashSet<>(platformRepository.findAllById(gameRequest.getPlatformsIds())); //Game vuole oggetti Platform, che ottengo dal repository passandogli tutti gli id messi nella request
        newGame.setPlatforms(platformList);

        Set<Genre> genreList = new HashSet<>(genreRepository.findAllById(gameRequest.getGenresIds()));
        newGame.setGenres(genreList);

        gameRepository.save(newGame);


        return new ResponseEntity<>(newGame.getId(),HttpStatus.OK);
    }


    public ResponseEntity<?> filterGames(List<Long> genreIds, List<Long> platformIds, String scoreOrder, String releaseDateOrder, List<Long> favoriteids, String title, int page, int perPage )
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

        //List<Game> filteredGames = gameRepository.findFilteredGames(genreIds,platformIds,scoreOrder,releaseDateOrder,favoriteids);
        Pageable pageable = PageRequest.of(page - 1,perPage);
        Page<GameCardResponse> result = gameRepository.findFilteredGames(genreIds,platformIds,scoreOrder,releaseDateOrder,favoriteids,title,pageable);
        //return new ResponseEntity<>(filteredGames,HttpStatus.OK);
        return new ResponseEntity<>(result,HttpStatus.OK);

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
        //List<Game> games = gameRepository.findTop10ByTitleStartingWith(lettere);

        /*if(!games.isEmpty())
        {
            return new ResponseEntity<>(games,HttpStatus.OK);
        }


        return new ResponseEntity<>(games,HttpStatus.NOT_FOUND);*/

        Pageable tenResults = PageRequest.of(0, 10);
        Page<GameSearchResponse> games = gameRepository.searchGamesStartingWith(lettere, tenResults);

        return new ResponseEntity<>(games.getContent(),HttpStatus.OK);
    }

    public ResponseEntity<?> getCountOfAllGames()
    {
        return new ResponseEntity<>(gameRepository.count(),HttpStatus.OK);
    }

    public ResponseEntity<?> getAllGameCardsByPage(int page, int perPage)
    {
        Pageable pageable = PageRequest.of(page - 1,perPage); //parti da 1 (0) e voglio 30 giochi per pagina
        return new ResponseEntity<>(gameRepository.getAllGameCardsByPage(pageable),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> deleteGameById(long gameId)
    {
        if(favoriteRepository.existsByFavoriteIdGameId(gameId))
        {
            favoriteRepository.deleteByGameId(gameId);
        }
        if(gameRepository.existsById(gameId))
        {
            gameRepository.deleteById(gameId);
            return new ResponseEntity<>("Game deleted successfully!",HttpStatus.OK);
        }

        return new ResponseEntity<>("Game not found / Impossible to delete",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> editGameById(long gameId, GameRequest gameRequest)
    {
        /*if(gameRepository.existsByTitle(gameRequest.getTitle()))
        {
            return new ResponseEntity<>("A game with this name already exists!", HttpStatus.BAD_REQUEST);
        }*/

        if(gameRepository.existsByTitleAndIdNot(gameRequest.getTitle(),gameId)) //Se edito Zelda cambiando anche solo il titolo posso farlo. A patto che l'id del gioco che sto editando sia quello
        {
            return new ResponseEntity<>("A game with this name already exists!", HttpStatus.BAD_REQUEST);
        }

        if(gameRepository.existsById(gameId))
        {
            Game specificGame = gameRepository.findById(gameId).orElseThrow();

            specificGame.setTitle(gameRequest.getTitle());
            specificGame.setDescription(gameRequest.getDescription());
            specificGame.setDeveloper(gameRequest.getDeveloper());
            specificGame.setPublisher(gameRequest.getPublisher());
            specificGame.setReleaseDate(gameRequest.getReleaseDate());
            specificGame.setScore(gameRequest.getScore());
            specificGame.setImageURL(gameRequest.getImageURL());

            Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(gameRequest.getPlatformsIds()));
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(gameRequest.getGenresIds()));


            specificGame.setPlatforms(platforms);
            specificGame.setGenres(genres);

            gameRepository.save(specificGame);

            return new ResponseEntity<>("Game successfully edited!",HttpStatus.OK);

        }

        return new ResponseEntity<>("Game doesn't exist", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getGameIdByTitle(String title)
    {
        if(!gameRepository.existsByTitle(title))
        {
            return new ResponseEntity<>("Game not existing", HttpStatus.BAD_REQUEST);
        }

        Long existingGameId = gameRepository.getGameIdByTitle(title); //prendo il primo, non mi servono gli altri

        return new ResponseEntity<>(existingGameId,HttpStatus.OK);
    }

}

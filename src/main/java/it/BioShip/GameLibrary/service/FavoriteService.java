package it.BioShip.GameLibrary.service;

import it.BioShip.GameLibrary.entity.Favorite;
import it.BioShip.GameLibrary.entity.FavoriteId;
import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.User;
import it.BioShip.GameLibrary.repository.FavoriteRepository;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteService
{
    private  final FavoriteRepository favoriteRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getAllFavoriteGamesIds(int userId)
    {
        Set<Long> favoriteGamesIds = favoriteRepository.findByUserId(userId);

        return  new ResponseEntity<>(favoriteGamesIds, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addNewFavoriteGame(int userId, long gameId)
    {
        Favorite newFavorite = new Favorite();
        if(gameRepository.existsById(gameId) && userRepository.existsById(userId))
        {
            Game specificGame = gameRepository.findById(gameId).orElseThrow();
            User specificUser = userRepository.findById(userId).orElseThrow();
            newFavorite.setFavoriteId(new FavoriteId(specificGame,specificUser));

            return new ResponseEntity<>("Gioco aggiunto ai preferiti!",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Impossibile aggiungere ai preferiti", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> deleteFavoriteGame(int userId, long gameId)
    {

    }
}

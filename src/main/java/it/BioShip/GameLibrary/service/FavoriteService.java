package it.BioShip.GameLibrary.service;

import it.BioShip.GameLibrary.entity.Favorite;
import it.BioShip.GameLibrary.entity.FavoriteId;
import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.User;
import it.BioShip.GameLibrary.repository.FavoriteRepository;
import it.BioShip.GameLibrary.repository.GameRepository;
import it.BioShip.GameLibrary.repository.UserRepository;
import it.BioShip.GameLibrary.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteService
{
    private  final FavoriteRepository favoriteRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseEntity<?> getAllFavoriteGamesIds(int userId, HttpServletRequest request)
    {
        String token = jwtService.extractTokenFromRequest(request);
        int tokenUserId = jwtService.extractUserId(token);
        Set<Long> favoriteGamesIds = new HashSet<>();
        if(tokenUserId == userId) //non necessario, potevo solo passare l'id ottenuto dal token. (Per allenamento)
        {
            favoriteGamesIds = favoriteRepository.findByUserId(userId);
            return  new ResponseEntity<>(favoriteGamesIds, HttpStatus.OK);
        }

        return new ResponseEntity<>(favoriteGamesIds,HttpStatus.BAD_REQUEST); //"Mismatch between user and token bearer" meglio restituire un set vuoto


    }

    @Transactional
    public ResponseEntity<?> addNewFavoriteGame(int userId, long gameId, HttpServletRequest request)
    {

        String token = jwtService.extractTokenFromRequest(request);
        int tokenUserId = jwtService.extractUserId(token);

        if(tokenUserId == userId)
        {

            if (gameRepository.existsById(gameId) && userRepository.existsById(userId))
            {
                Favorite newFavorite = new Favorite();
                Game specificGame = gameRepository.findById(gameId).orElseThrow();
                User specificUser = userRepository.findById(userId).orElseThrow();
                newFavorite.setFavoriteId(new FavoriteId(specificGame, specificUser));

                favoriteRepository.save(newFavorite);
                return new ResponseEntity<>("Gioco added to favorites!", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Adding to favorites failed!", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Mismatch between user and token bearer",HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public ResponseEntity<?> deleteFavoriteGame(int userId, long gameId, HttpServletRequest request)
    {
        String token = jwtService.extractTokenFromRequest(request);
        int tokenUserId = jwtService.extractUserId(token);

        if(tokenUserId == userId)
        {

            if (gameRepository.existsById(gameId) && userRepository.existsById(userId))
            {
                Game specificGame = gameRepository.findById(gameId).orElseThrow();
                User specificUser = userRepository.findById(userId).orElseThrow();
                FavoriteId favoriteId = new FavoriteId(specificGame, specificUser);

                if (favoriteRepository.existsById(favoriteId)) {
                    favoriteRepository.deleteById(favoriteId);
                    return new ResponseEntity<>("Game removed from favorites!", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Removal from favorites failed!", HttpStatus.OK);
                }
            }
            else
            {
                return new ResponseEntity<>("Removal from favorites failed!", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Mismatch between user and token bearer",HttpStatus.BAD_REQUEST);
    }
}

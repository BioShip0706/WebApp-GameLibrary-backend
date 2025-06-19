package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import it.BioShip.GameLibrary.payload.response.GameFullResponse;
import it.BioShip.GameLibrary.payload.response.GameSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long>, GameRepositoryCustom
{

    List<Game> findAllByOrderByIdAsc();


    @Query("SELECT g FROM Game g JOIN g.genres gg JOIN g.platforms gp WHERE gg.id IN :genreIds AND gp.id IN :platformIds GROUP BY g.id HAVING COUNT(DISTINCT gg.id) = :genreCount AND COUNT(DISTINCT gp.id) = :platformCount")
    List<Game> findByGenresIdsAndPlatformsIds(List<Long> genreIds, List<Long> platformIds, long genreCount, long platformCount);


    @Query("SELECT g FROM Game g JOIN g.genres gg WHERE gg.id IN :genreIds GROUP BY g.id HAVING COUNT(DISTINCT gg.id) = :genreCount")
    List<Game> findByGenresIds(List<Long> genreIds, long genreCount);

    @Query("SELECT g FROM Game g JOIN g.platforms gp WHERE gp.id IN :platformIds GROUP BY g.id HAVING COUNT(DISTINCT gp.id) = :platformCount")
    List<Game> findByPlatformsIds(List<Long> platformIds, long platformCount);




    //Optional findByTitleLike(String lettere);

    //@Query("SELECT g FROM Game g WHERE g.title LIKE CONCAT(:lettere, '%') LIMIT 10")
    //List<Game> findByTitleLikeLettersLimited(String lettere);

    List<Game> findTop10ByTitleStartingWith(String lettere);

    @Query("SELECT new it.BioShip.GameLibrary.payload.response.GameSearchResponse(g.id,g.title,g.developer,g.releaseDate,g.score) FROM Game g WHERE g.title LIKE CONCAT(:lettere, '%')")
    Page<GameSearchResponse> searchGamesStartingWith(String lettere, Pageable tenResults);

    @Query("SELECT new it.BioShip.GameLibrary.payload.response.GameCardResponse(g.id,g.title,g.developer,g.releaseDate,g.imageURL,g.score) FROM Game g")
    List<GameCardResponse> findAllGameCards();

    @Query("SELECT new it.BioShip.GameLibrary.payload.response.GameCardResponse(g.id,g.title,g.developer,g.releaseDate,g.imageURL,g.score) FROM Game g JOIN Favorite f ON g.id = f.favoriteId.game.id AND f.favoriteId.user.id = :userId")
    List<GameCardResponse> findAllFavoriteGames(int userId);


    @Query("SELECT new it.BioShip.GameLibrary.payload.response.GameFullResponse(g.id,g.title,g.description,g.developer,g.publisher,g.releaseDate,g.score,g.imageURL) " +
            "FROM Game g WHERE g.id = :gameId")
    GameFullResponse getFullGameInfoById(long gameId);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.genres WHERE g.id = :gameId")
    Game findByIdFullFetchGenres(long gameId);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.platforms WHERE g.id = :gameId")
    Game findByIdFullFetchPlatforms(long gameId);


    @Query("SELECT new it.BioShip.GameLibrary.payload.response.GameCardResponse(g.id,g.title,g.developer,g.releaseDate,g.imageURL,g.score) FROM Game g")
    Page<GameCardResponse> getAllGameCardsByPage(Pageable pageable);
}

package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long>
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

}

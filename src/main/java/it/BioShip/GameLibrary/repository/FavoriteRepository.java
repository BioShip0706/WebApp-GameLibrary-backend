package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Favorite;
import it.BioShip.GameLibrary.entity.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId>
{

    @Query("SELECT f.favoriteId.game.id from Favorite f WHERE f.favoriteId.user.id = :userId")
    Set<Long> findByUserId(long userId);

    boolean existsByFavoriteIdGameId(long gameId);

    @Modifying
    @Query("DELETE FROM Favorite f where f.favoriteId.game.id = :gameId")
    void deleteByGameId(long gameId);
}

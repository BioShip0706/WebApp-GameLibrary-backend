package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameRepositoryCustom
{
    /*List<Game> findFilteredGames(List<Long> genreIds,
                                 List<Long> platformIds,
                                 String scoreOrder,
                                 String releaseDateOrder,
                                 List<Long> favoriteGameIds);*/

    Page<GameCardResponse> findFilteredGames(List<Long> genreIds,
                                             List<Long> platformIds,
                                             String scoreOrder,
                                             String releaseDateOrder,
                                             List<Long> favoriteGameIds,
                                             String title,
                                             Pageable pageable);
}

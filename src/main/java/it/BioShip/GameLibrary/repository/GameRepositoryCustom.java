package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;

import java.util.List;

public interface GameRepositoryCustom
{
    List<Game> findFilteredGames(List<Long> genreIds,
                                 List<Long> platformIds,
                                 String scoreOrder,
                                 String releaseDateOrder,
                                 List<Long> favoriteGameIds);
}

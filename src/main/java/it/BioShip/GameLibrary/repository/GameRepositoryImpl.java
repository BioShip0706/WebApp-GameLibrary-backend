package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameRepositoryImpl implements GameRepositoryCustom
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Game> findFilteredGames(List<Long> genreIds, List<Long> platformIds, String scoreOrder, String releaseDateOrder, List<Long> favoriteGameIds)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);

        Root<Game> root = query.from(Game.class);

        root.fetch("platforms", JoinType.LEFT);
        root.fetch("genres", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if(genreIds != null && !genreIds.isEmpty())
        {
            Join<Game, Genre> genreJoin = root.join("genres");
            predicates.add(genreJoin.get("id").in(genreIds));
        }

        if(platformIds != null && !platformIds.isEmpty())
        {
            Join<Game, Platform> platformJoin = root.join("platforms");
            predicates.add(platformJoin.get("id").in(platformIds));
        }

        if(favoriteGameIds != null && !favoriteGameIds.isEmpty())
        {
            predicates.add(root.get("id").in(favoriteGameIds));
        }

        query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).distinct(true);

        List<Order> orders = new ArrayList<>();

        if("asc".equalsIgnoreCase(scoreOrder))
        {
            orders.add(criteriaBuilder.asc(root.get("score")));
        }
        else if("desc".equalsIgnoreCase(scoreOrder))
        {
            orders.add(criteriaBuilder.desc(root.get("score")));
        }

        if("asc".equalsIgnoreCase(releaseDateOrder))
        {
            orders.add(criteriaBuilder.asc(root.get("releaseDate")));
        }
        else if("desc".equalsIgnoreCase(releaseDateOrder))
        {
            orders.add(criteriaBuilder.desc(root.get("releaseDate")));
        }

        if(!orders.isEmpty())
        {
            query.orderBy(orders);
        }

        return  entityManager.createQuery(query).getResultList();
    }

}

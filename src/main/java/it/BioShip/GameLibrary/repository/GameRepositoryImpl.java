package it.BioShip.GameLibrary.repository;

import it.BioShip.GameLibrary.entity.Game;
import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import it.BioShip.GameLibrary.payload.response.GameCardResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameRepositoryImpl implements GameRepositoryCustom
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<GameCardResponse> findFilteredGames(List<Long> genreIds,List<Long> platformIds,String scoreOrder,String releaseDateOrder,List<Long> favoriteGameIds, String title, Pageable pageable)
    {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Query per ottenere i dati paginati
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);
        root.fetch("platforms", JoinType.LEFT);
        root.fetch("genres", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, genreIds, platformIds, favoriteGameIds, title);
        query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).distinct(true);

        List<Order> orders = buildOrders(criteriaBuilder, root, scoreOrder, releaseDateOrder, title);
        if(!orders.isEmpty())
        {
            query.orderBy(orders);
        }

        List<Game> games = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        // Chiama metodo per contare totale risultati senza paginazione
        long total = countFilteredGames(genreIds, platformIds, favoriteGameIds, title);

        // Converti la lista di Game in GameCardResponse (implementa tu questa conversione)
        List<GameCardResponse> content = convertToResponse(games);

        return new org.springframework.data.domain.PageImpl<>(content, pageable, total);
    }

    private long countFilteredGames(List<Long> genreIds, List<Long> platformIds, List<Long> favoriteGameIds, String title)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Game> root = countQuery.from(Game.class);

        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, genreIds, platformIds, favoriteGameIds, title);

        countQuery.select(criteriaBuilder.countDistinct(root));
        countQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Game> root,
                                            List<Long> genreIds, List<Long> platformIds, List<Long> favoriteGameIds, String title)
    {
        List<Predicate> predicates = new ArrayList<>();

        if (genreIds != null && !genreIds.isEmpty()) {
            Join<Game, Genre> genreJoin = root.join("genres");
            predicates.add(genreJoin.get("id").in(genreIds));
        }

        if (platformIds != null && !platformIds.isEmpty()) {
            Join<Game, Platform> platformJoin = root.join("platforms");
            predicates.add(platformJoin.get("id").in(platformIds));
        }

        if (favoriteGameIds != null && !favoriteGameIds.isEmpty()) {
            predicates.add(root.get("id").in(favoriteGameIds));
        }

        //qua title
        /*if (title != null && !title.isBlank()) //breath of
        {
            List<String> ignoreWords = List.of("the", "of", "an", "a");

            String[] splittedWords = title.toLowerCase().trim().split("\\s+"); //splitta per gli spazi (anche molteplici) //diventa ["Breath","of"]

            Expression<String> spacedTitle = criteriaBuilder.concat(" ", criteriaBuilder.concat(criteriaBuilder.lower(root.get("title")), " ")); //Prende title da Game e diventa "spazio title spazio" esempio " Metal gear solid V "

            List<Predicate> titlePredicates = new ArrayList<>();

            for (String word : splittedWords)
            {


                if (ignoreWords.contains(word))
                {
                    // La stop word deve essere solo all'inizio del titolo
                    titlePredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), word + " %")); //es. per "The" deve SOLO iniziare con "The"
                }
                else
                {
                    // Match parola intera o inizio parola nel titolo (con spazi prima)
                    titlePredicates.add(criteriaBuilder.like(spacedTitle, "% " + word + "%")); //es. per "Zelda" non rientra nella parole da ignorare quindi è "qualsiasicosa  Zelda qualisasicosa"
                }
            }

            predicates.add(criteriaBuilder.and(titlePredicates.toArray(new Predicate[0])));
        }*/

        if (title != null && !title.isBlank())
        {
            String[] splittedWords = title.toLowerCase().trim().split("\\s+");
            Expression<String> lowerTitle = criteriaBuilder.lower(root.get("title"));
            Expression<String> spacedTitle = criteriaBuilder.concat(" ", criteriaBuilder.concat(lowerTitle, " "));


            List<Predicate> titlePredicates = new ArrayList<>();

            for (String word : splittedWords)
            {
                word = word.trim();

                Predicate startsWith = criteriaBuilder.like(lowerTitle, word + "%");
                Predicate endsWith = criteriaBuilder.like(lowerTitle, "% " + word);
                Predicate containsSpaced = criteriaBuilder.like(spacedTitle, "% " + word + " %");

                Predicate spacedContainsEverything = criteriaBuilder.like(spacedTitle, "% " + word + "% "); //per includere roba tipo "Methaphor:"  Oppure "Zelda: breath"

                /*Predicate containsColon = criteriaBuilder.like(lowerTitle, "% " + word + ":%");
                Predicate containsPoint = criteriaBuilder.like(lowerTitle, "% " + word + ".%");
                Predicate containsComma = criteriaBuilder.like(lowerTitle, "% " + word + ",%");
                Predicate containsExclamationMark = criteriaBuilder.like(lowerTitle, "% " + word + "!%");*/

                titlePredicates.add(criteriaBuilder.or(startsWith, endsWith, containsSpaced, spacedContainsEverything));
            }

            predicates.add(criteriaBuilder.and(titlePredicates.toArray(new Predicate[0])));
        }



        return predicates;
    }

    private List<Order> buildOrders(CriteriaBuilder criteriaBuilder, Root<Game> root, String scoreOrder, String releaseDateOrder, String title)
    {
        List<Order> orders = new ArrayList<>();

        if ("asc".equalsIgnoreCase(scoreOrder))
        {
            orders.add(criteriaBuilder.asc(root.get("score")));
        }
        else if ("desc".equalsIgnoreCase(scoreOrder))
        {
            orders.add(criteriaBuilder.desc(root.get("score")));
        }

        if ("asc".equalsIgnoreCase(releaseDateOrder))
        {
            orders.add(criteriaBuilder.asc(root.get("releaseDate")));
        }
        else if ("desc".equalsIgnoreCase(releaseDateOrder))
        {
            orders.add(criteriaBuilder.desc(root.get("releaseDate")));
        }

        if (title != null && !title.isBlank())
        {
            Expression<String> lowerTitle = criteriaBuilder.lower(root.get("title"));
            String firstWord = title.toLowerCase().trim().split("\\s+")[0];

            Order orderByRelevance = criteriaBuilder.asc(
                    criteriaBuilder.selectCase()
                            .when(criteriaBuilder.like(lowerTitle, firstWord + "%"), 1)
                            .when(criteriaBuilder.like(lowerTitle, "% " + firstWord + "%"), 2)
                            .otherwise(3)
            );

            orders.add(orderByRelevance);
        }



        return orders;
    }

    private List<GameCardResponse> convertToResponse(List<Game> games)
    {
        List<GameCardResponse> responses = new ArrayList<>();

        for(Game game : games)
        {
            GameCardResponse response = new GameCardResponse();
            response.setId(game.getId());
            response.setTitle(game.getTitle());
            response.setDeveloper(game.getDeveloper());
            response.setReleaseDate(game.getReleaseDate());
            response.setImageURL(game.getImageURL());
            response.setScore(game.getScore());
            responses.add(response);
        }

        return responses;

    }

}

package it.BioShip.GameLibrary.payload.response;

import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameFullResponse
{
    private long id;
    private String title;
    private String description;
    private String developer;
    private String publisher;
    private LocalDate releaseDate;
    private Double score;
    private String imageURL;
    private List<Platform> platforms;
    private List<Genre> genres;

    public GameFullResponse(long id, String title, String description, String developer, String publisher, LocalDate releaseDate, Double score, String imageURL)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.score = score;
        this.imageURL = imageURL;
    }
}

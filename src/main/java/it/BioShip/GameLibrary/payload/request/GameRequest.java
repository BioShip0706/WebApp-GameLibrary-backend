package it.BioShip.GameLibrary.payload.request;

import it.BioShip.GameLibrary.entity.Genre;
import it.BioShip.GameLibrary.entity.Platform;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

@Getter
public class GameRequest
{

    @NotBlank @Length(min = 1, max = 150)
    private String title;

    @NotBlank @Length(min = 10, max = 300)
    private String description;

    @NotBlank @Length(min = 1, max = 100)
    private String developer;

    @NotBlank @Length(min = 1, max = 100)
    private String publisher;

    @NotNull
    private LocalDate releaseDate;


    @NotBlank @Length(min = 1, max = 10)
    private Double score;

    private String imageURL;

    private List<Long> platformsIds; //lista di Long perchè serve solo l'id della piattaforma o del genere

    private List<Long> genresIds;
}

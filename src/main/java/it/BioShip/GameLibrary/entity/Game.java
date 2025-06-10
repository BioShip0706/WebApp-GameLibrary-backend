package it.BioShip.GameLibrary.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Game
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 150, unique = true)
    private String title;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false, length = 100)
    private String developer;

    @Column(nullable = false, length = 100)
    private String publisher;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @NotNull @Min(1) @Max(10)
    private Double score;

    private String imageURL;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Platform> platforms;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Genre> genres;


}

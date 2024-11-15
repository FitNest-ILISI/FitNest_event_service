package org.ilisi.event.entities;
import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor

@NoArgsConstructor
@Entity @Getter
@Setter
public class SportCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String iconPath;
    @Column(columnDefinition = "boolean default false")
    private boolean requiresRoute = false;



}

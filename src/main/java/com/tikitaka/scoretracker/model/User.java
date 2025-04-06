package com.tikitaka.scoretracker.model;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @ElementCollection
    private List<String> favoriteTeams = new ArrayList<>();
}package com.tikitaka.scoretracker.model;

import jakarta.persistence.*;
        import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    // Option 1: Keep simple list (no additional features)
    @ElementCollection
    @CollectionTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "team_id")
    private List<String> favoriteTeams = new ArrayList<>();

    /*
    // Option 2: Convert to entity relationship (advanced features)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavorite> favorites = new ArrayList<>();
    */
}
package com.cloud.kinopoisk;

import com.cloud.kinopoisk.dto.AddWatched;
import com.cloud.kinopoisk.entity.MovieEntity;
import com.cloud.kinopoisk.entity.UserEntity;
import com.cloud.kinopoisk.entity.UserMovieEntity;
import com.cloud.kinopoisk.entity.composite.EnrollmentId;
import com.cloud.kinopoisk.repository.MovieRepository;
import com.cloud.kinopoisk.repository.UserMovieRepository;
import com.cloud.kinopoisk.repository.UserRepository;
import com.cloud.kinopoisk.service.UserMovieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("local")
public class UserMovieServiceTests {
    @Autowired
    private UserMovieService userMovieService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMovieRepository userMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void initData() {
        UserEntity user = new UserEntity();
        user.setLogin("user");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        userRepository.save(user);

        MovieEntity movie1 = new MovieEntity();
        movie1.setPosterUrl("http://localhost:9000/bucket/movie1.png");
        movie1.setAuthor("author1");
        movie1.setTitle("title1");
        movie1.setRating("10");

        MovieEntity movie2 = new MovieEntity();
        movie1.setPosterUrl("http://localhost:9000/bucket/movie2.png");
        movie1.setAuthor("author2");
        movie1.setTitle("title2");
        movie1.setRating("10");

        movieRepository.save(movie1);
        movieRepository.save(movie2);

        UserMovieEntity um1 = new UserMovieEntity();
        EnrollmentId enrollmentId1 = new EnrollmentId(user.getId(), movie1.getId());
        um1.setIsWatched(false);
        um1.setMovie(movie1);
        um1.setUser(user);
        um1.setId(enrollmentId1);

        UserMovieEntity um2 = new UserMovieEntity();
        EnrollmentId enrollmentId2 = new EnrollmentId(user.getId(), movie2.getId());
        um2.setIsWatched(true);
        um2.setMovie(movie2);
        um2.setUser(user);
        um2.setId(enrollmentId2);

        userMovieRepository.save(um1);
        userMovieRepository.save(um2);
    }

    @AfterEach
    void clearData() {
        userMovieRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void handleIsWatchedSuccessfully() {
        UserEntity user = userRepository.findAll().get(0);
        MovieEntity movie = movieRepository.findAll().get(0);
        
        AddWatched addWatched = new AddWatched();
        addWatched.setMovieId(movie.getId().toString());
        addWatched.setUserId(user.getId().toString());

        assertDoesNotThrow(() -> userMovieService.handleIsWatched(addWatched));
    }
    
}

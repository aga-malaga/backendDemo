package com.example.backendDemo.github.domain;

import com.example.backendDemo.github.GithubFacade;
import com.example.backendDemo.github.dto.FavouriteRepoForm;
import com.example.backendDemo.github.dto.GithubRepoDto;
import com.example.backendDemo.github.dto.SearchRepoForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
class GithubController {

    private static final class Routes {
        private static final String ROOT = "api/repositories";
        private static final String SEARCH = ROOT + "/search";
        private static final String FAVOURITES = ROOT + "/favourites";
    }

    private final GithubFacade githubFacade;

    @GetMapping(Routes.SEARCH)
    GithubRepoDto findRepositories(@Valid @RequestBody SearchRepoForm searchForm) {
        return githubFacade.findRepositories(searchForm);
    }

    @GetMapping(Routes.FAVOURITES)
    Collection<GithubRepoDto.Item> findFavourites() {
        return githubFacade.findFavourites();
    }

    @PostMapping(Routes.FAVOURITES)
    void addToFavourite(@Valid @RequestBody FavouriteRepoForm favouriteRepoForm) {
        githubFacade.addToFavourite(favouriteRepoForm);
    }
}
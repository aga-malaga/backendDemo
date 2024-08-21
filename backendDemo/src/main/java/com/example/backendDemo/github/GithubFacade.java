package com.example.backendDemo.github;

import com.example.backendDemo.github.dto.FavouriteRepoForm;
import com.example.backendDemo.github.dto.GithubRepoDto;
import com.example.backendDemo.github.dto.SearchRepoForm;

import java.util.Collection;

public interface GithubFacade {
    GithubRepoDto findRepositories(SearchRepoForm searchForm);
    Collection<GithubRepoDto.Item> findFavourites();
    void addToFavourite(FavouriteRepoForm favouriteRepoForm);
}
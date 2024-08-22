package com.example.backendDemo.github.domain;

import com.example.backendDemo.exception.ClientException;
import com.example.backendDemo.github.GithubFacade;
import com.example.backendDemo.github.dto.FavouriteRepoForm;
import com.example.backendDemo.github.dto.GithubRepoDto;
import com.example.backendDemo.github.dto.SearchRepoForm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.backendDemo.github.domain.GithubConstants.*;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
class GithubServiceImpl implements GithubFacade {

    private final RestClient restClient;
    private final Map<Integer, GithubRepoDto.Item> database = new HashMap<>();

    @Override
    public GithubRepoDto findRepositories(SearchRepoForm searchForm) {
        String query = createQuery(searchForm.createdAt(), searchForm.language());
        return findRepositories(query, searchForm.perPage());
    }

    @Override
    public void addToFavourite(FavouriteRepoForm favouriteRepoForm) {
        GithubRepoDto.Item repoItem = createItem(favouriteRepoForm);
        database.put(repoItem.id(), repoItem);
    }

    @Override
    public Collection<GithubRepoDto.Item> findFavourites() {
        return database.values();
    }

    private GithubRepoDto findRepositories(String query, Integer perPage) {
        URI uri = buildUri(query, perPage);
        return getRequest(uri);
    }

    private GithubRepoDto getRequest(URI uri) {
        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ClientException(response.getStatusCode().value(), response.getStatusText());
                })
                .body(new ParameterizedTypeReference<>() {});
    }

    private URI buildUri(String query, Integer perPage) {
        return UriComponentsBuilder
                .fromUriString(SEARCH_REPOSITORIES)
                .queryParam(SORT, STARS)
                .queryParam(ORDER, DESC)
                .queryParamIfPresent(PER_PAGE, Optional.ofNullable(perPage))
                .queryParam("q", query)
                .build()
                .toUri();
    }

    private String createQuery(LocalDate createdAt, String language) {
        StringBuilder query = new StringBuilder();
        query.append(CREATED_FORMAT.formatted(createdAt));

        return nonNull(language)
                ? appendLanguage(language, query)
                : query.toString();
    }

    private String appendLanguage(String language, StringBuilder query) {
        return query.append(PLUS)
                .append(LANGUAGE_FORMAT.formatted(language))
                .toString();
    }

    private GithubRepoDto.Item createItem(FavouriteRepoForm favouriteRepoForm) {
        return new GithubRepoDto.Item(
                favouriteRepoForm.id(),
                favouriteRepoForm.name(),
                favouriteRepoForm.language(),
                favouriteRepoForm.stargazers_count(),
                favouriteRepoForm.created_at());
    }
}
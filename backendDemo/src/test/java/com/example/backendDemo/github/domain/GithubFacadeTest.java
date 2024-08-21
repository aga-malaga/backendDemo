package com.example.backendDemo.github.domain;

import com.example.backendDemo.github.GithubFacade;
import com.example.backendDemo.github.dto.FavouriteRepoForm;
import com.example.backendDemo.github.dto.GithubRepoDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.mock;

class GithubFacadeTest {

    private final RestClient restClient = mock();
    private final GithubFacade systemUnderTest = new GithubFacadeConfiguration().githubFacade(restClient);

    private final HashMap<Integer, GithubRepoDto.Item> database = new HashMap<>();

    @Nested
    class AddToFavourite {

        @Test
        void shouldAddItemToFavourite() {
            //given
            ReflectionTestUtils.setField(systemUnderTest, "database", database);
            LocalDateTime createdAt = LocalDateTime.of(2024, 8, 21, 13, 0);
            FavouriteRepoForm form = new FavouriteRepoForm(1, "name", "java", 123,
                    createdAt);

            //when
            systemUnderTest.addToFavourite(form);

            //then
            assertThat(database.values())
                    .extracting(GithubRepoDto.Item::id,
                            GithubRepoDto.Item::name,
                            GithubRepoDto.Item::language,
                            GithubRepoDto.Item::stargazers_count,
                            GithubRepoDto.Item::created_at)
                    .containsExactly(
                            tuple(1, "name", "java", 123, createdAt));
        }
    }

    @Nested
    class FindFavourites {

        @Test
        void shouldReturnEmptyCollectionWhenNoFavouriteRepositories() {
            //given/when
            Collection<GithubRepoDto.Item> actual = systemUnderTest.findFavourites();

            //then
            assertThat(actual)
                    .isEmpty();
        }

        @Test
        void shouldReturnGithubRepoDtoItemsWhenRepositoriesAddedToFavourites() {
            //given
            ReflectionTestUtils.setField(systemUnderTest, "database", database);
            database.put(1, createItem(1, "name", "java"));
            database.put(2, createItem(2, "name2", "go"));
            database.put(3, createItem(3, "name3", "python"));

            //when
            Collection<GithubRepoDto.Item> actual = systemUnderTest.findFavourites();

            //then
            assertThat(actual)
                    .extracting(GithubRepoDto.Item::id,
                            GithubRepoDto.Item::name,
                            GithubRepoDto.Item::language,
                            GithubRepoDto.Item::stargazers_count,
                            GithubRepoDto.Item::created_at)
                    .containsExactlyInAnyOrder(
                            tuple(1, "name", "java", 2, LocalDateTime.MAX),
                            tuple(2, "name2", "go", 2, LocalDateTime.MAX),
                            tuple(3, "name3", "python", 2, LocalDateTime.MAX));
        }

        static GithubRepoDto.Item createItem(int id, String name, String language) {
            return new GithubRepoDto.Item(id, name, language, 2, LocalDateTime.MAX);
        }
    }
}
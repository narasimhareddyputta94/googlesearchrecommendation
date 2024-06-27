package com.example.googlesearch_recommendation.repository;

import com.example.googlesearch_recommendation.model.SearchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SearchQueryRepositoryTest {

    @Autowired
    private SearchQueryRepository searchQueryRepository;

    @Test
    public void testFindByQueryStartingWith() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setQuery("test");
        searchQueryRepository.save(searchQuery);

        List<SearchQuery> results = searchQueryRepository.findByQueryStartingWith("te");

        assertEquals(1, results.size());
        assertEquals("test", results.get(0).getQuery());
    }

    @Test
    public void testFindTop10ByOrderByCountDesc() {
        for (int i = 1; i <= 10; i++) {
            SearchQuery searchQuery = new SearchQuery();
            searchQuery.setQuery("query" + i);
            searchQuery.setCount((long) i);
            searchQueryRepository.save(searchQuery);
        }

        List<SearchQuery> results = searchQueryRepository.findTop10ByOrderByCountDesc();

        assertEquals(10, results.size());
        assertEquals("query10", results.get(0).getQuery());
    }
}

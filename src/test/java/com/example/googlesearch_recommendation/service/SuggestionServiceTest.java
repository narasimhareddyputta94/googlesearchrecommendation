package com.example.googlesearch_recommendation.service;

import com.example.googlesearch_recommendation.model.SearchQuery;
import com.example.googlesearch_recommendation.repository.SearchQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SuggestionServiceTest {

    @Mock
    private SearchQueryRepository searchQueryRepository;

    @InjectMocks
    private SuggestionService suggestionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSuggestions() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setQuery("test");
        when(searchQueryRepository.findByQueryStartingWith(anyString())).thenReturn(Collections.singletonList(searchQuery));

        List<String> suggestions = suggestionService.getSuggestions("te");

        assertEquals(1, suggestions.size());
        assertEquals("test", suggestions.get(0));
    }

    @Test
    public void testLogQueryNewQuery() {
        when(searchQueryRepository.findByQueryStartingWith(anyString())).thenReturn(Collections.emptyList());

        suggestionService.logQuery("test");

        verify(searchQueryRepository, times(1)).save(any(SearchQuery.class));
    }

    @Test
    public void testLogQueryExistingQuery() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setQuery("test");
        searchQuery.setCount(1L);
        when(searchQueryRepository.findByQueryStartingWith(anyString())).thenReturn(Collections.singletonList(searchQuery));

        suggestionService.logQuery("test");

        verify(searchQueryRepository, times(1)).save(any(SearchQuery.class));
        assertEquals(2L, searchQuery.getCount());
    }
}

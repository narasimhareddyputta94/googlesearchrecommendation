package com.example.googlesearch_recommendation.controller;

import com.example.googlesearch_recommendation.service.SuggestionService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestionControllerTest {

    @Mock
    private RestHighLevelClient client;

    @Mock
    private SuggestionService suggestionService;

    @InjectMocks
    private SuggestionController suggestionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(suggestionController).build();
    }

    @Test
    public void testGetSuggestions() throws Exception {
        when(client.search(any(), eq(RequestOptions.DEFAULT))).thenReturn(mock(SearchResponse.class));

        mockMvc.perform(get("/suggest").param("query", "test"))
                .andExpect(status().isOk());

        verify(client, times(1)).search(any(), eq(RequestOptions.DEFAULT));
    }

    @Test
    public void testLogQuery() throws Exception {
        doNothing().when(suggestionService).logQuery(anyString());

        mockMvc.perform(get("/log").param("query", "test"))
                .andExpect(status().isOk());

        verify(suggestionService, times(1)).logQuery(anyString());
    }
}

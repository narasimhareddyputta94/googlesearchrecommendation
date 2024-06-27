package com.example.googlesearch_recommendation.controller;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SuggestionController {

    @Autowired
    private RestHighLevelClient client;

    @Cacheable("suggestions")
    @GetMapping("/suggest")
    public List<String> getSuggestions(@RequestParam String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest("search_suggestions");
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
                .completionSuggestion("query")
                .prefix(query)
                .size(10)
                .skipDuplicates(true);
        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion("suggest_query", completionSuggestionBuilder);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<String> suggestions = new ArrayList<>();
        searchResponse.getSuggest().getSuggestion("suggest_query").getEntries().forEach(entry ->
                entry.getOptions().forEach(option -> suggestions.add(option.getText().string()))
        );

        return suggestions;
    }
}

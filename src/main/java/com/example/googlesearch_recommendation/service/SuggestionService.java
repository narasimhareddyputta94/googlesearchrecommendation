package com.example.googlesearch_recommendation.service;



import com.example.googlesearch_recommendation.model.SearchQuery;
import com.example.googlesearch_recommendation.repository.SearchQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    @Autowired
    private SearchQueryRepository searchQueryRepository;

    public List<String> getSuggestions(String query) {
        return searchQueryRepository.findByQueryStartingWith(query).stream()
                .map(SearchQuery::getQuery)
                .collect(Collectors.toList());
    }

    public void logQuery(String query) {
        List<SearchQuery> existingQueries = searchQueryRepository.findByQueryStartingWith(query);
        if (existingQueries.isEmpty()) {
            SearchQuery newQuery = new SearchQuery();
            newQuery.setQuery(query);
            newQuery.setCount(1L);
            searchQueryRepository.save(newQuery);
        } else {
            SearchQuery existingQuery = existingQueries.get(0);
            existingQuery.setCount(existingQuery.getCount() + 1);
            searchQueryRepository.save(existingQuery);
        }
    }
}

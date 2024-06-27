package com.example.googlesearch_recommendation.repository;



import com.example.googlesearch_recommendation.model.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {

    List<SearchQuery> findByQueryStartingWith(String query);
}

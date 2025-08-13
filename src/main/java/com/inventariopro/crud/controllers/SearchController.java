package com.inventariopro.crud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.SearchResultsDTO;
import com.inventariopro.crud.services.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResultsDTO> searchGlobal(@RequestParam("query") String query) {
        SearchResultsDTO results = searchService.searchGlobal(query);
        return ResponseEntity.ok(results);
    }
}

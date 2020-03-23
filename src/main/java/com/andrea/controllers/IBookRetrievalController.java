package com.andrea.controllers;

import com.andrea.dto.rest.GetBookListResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

public interface IBookRetrievalController {
    @GetMapping(value = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    GetBookListResponse getBookList();
}

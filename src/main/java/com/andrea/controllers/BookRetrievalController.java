package com.andrea.controllers;

import com.andrea.dto.rest.GetBookListResponse;
import com.andrea.services.IBookRetrievalService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class BookRetrievalController implements IBookRetrievalController {

    final IBookRetrievalService bookRetrievalService;

    public BookRetrievalController(IBookRetrievalService bookRetrievalService) {
        this.bookRetrievalService = bookRetrievalService;
    }


    @GetMapping(value = "/books",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetBookListResponse getBookList(){
        return bookRetrievalService.getBookList();
    }


}

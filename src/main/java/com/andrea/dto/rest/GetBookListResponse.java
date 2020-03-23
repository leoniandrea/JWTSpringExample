package com.andrea.dto.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class GetBookListResponse {
    List<Book> books;

    public GetBookListResponse(List<Book> books){
        this.books = books;
    }
}

package com.andrea.dto.rest;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Book {
    private String title;
    private String author;

    public Book(String title){
        this.title = title;
    }
}

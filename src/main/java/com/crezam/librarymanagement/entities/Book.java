package com.crezam.librarymanagement.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
 
@Entity
@Getter
@Setter
public class Book {
    @Id
    private String isbn;  // Unique ISBN

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    private String category;
    
    private int availableCopies;
    private int publishedYear;
}

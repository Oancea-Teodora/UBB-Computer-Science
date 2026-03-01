package ro.ubb.authordocumentsmovies.repository;

import ro.ubb.authordocumentsmovies.model.Author;

public interface AuthorRepository extends MainRepository<Author, Long>{
    Author findByName(String name);
}

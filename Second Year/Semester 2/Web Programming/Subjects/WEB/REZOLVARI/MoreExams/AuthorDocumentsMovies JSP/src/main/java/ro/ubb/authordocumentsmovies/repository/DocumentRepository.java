package ro.ubb.authordocumentsmovies.repository;

import ro.ubb.authordocumentsmovies.model.Author;
import ro.ubb.authordocumentsmovies.model.Document;

public interface DocumentRepository extends MainRepository<Document, Long>{
    Document findByName(String name);
}

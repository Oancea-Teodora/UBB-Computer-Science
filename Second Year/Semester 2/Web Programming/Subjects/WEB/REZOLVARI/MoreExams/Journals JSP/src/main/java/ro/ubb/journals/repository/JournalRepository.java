package ro.ubb.journals.repository;

import ro.ubb.journals.model.Journal;

import java.util.List;

public interface JournalRepository extends MainRepository<Journal, Long>{

    Journal findByName(String name);
}


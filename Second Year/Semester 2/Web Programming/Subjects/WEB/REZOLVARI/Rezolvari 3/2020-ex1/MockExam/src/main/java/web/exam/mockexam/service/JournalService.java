package web.exam.mockexam.service;

import web.exam.mockexam.model.Journal;
import web.exam.mockexam.repository.JournalRepository;

import java.util.List;
import java.util.Optional;

public class JournalService {
    private final JournalRepository journalRepository;

    public JournalService() {
        journalRepository = new JournalRepository();
    }

    List<Journal> findAll() {
        return journalRepository.findAll();
    }

    Optional<Journal> findByName(String name) {
        return journalRepository.findByName(name);
    }

    public void save(Journal journal) {
        journalRepository.save(journal);
    }
}

package com.library.services;

import com.library.models.Patron;
import java.util.List;
import java.util.Optional;

/**
 * Interface for patron management operations.
 * Follows the Interface Segregation Principle by focusing only on patron-related operations.
 */
public interface PatronService {
    void addPatron(Patron patron);
    void removePatron(String patronId);
    Optional<Patron> updatePatron(String patronId, Patron updatedPatron);
    Optional<Patron> findPatronById(String patronId);
    List<Patron> getAllPatrons();
    List<Patron> searchPatronsByName(String name);
}

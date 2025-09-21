package com.library.services;

import com.library.models.Patron;
import com.library.utils.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of PatronService.
 * Manages patron data and operations.
 */
public class PatronServiceImpl implements PatronService {
    private final Map<String, Patron> patrons;

    public PatronServiceImpl() {
        this.patrons = new ConcurrentHashMap<>();
    }

    @Override
    public void addPatron(Patron patron) {
        if (patron == null || patron.getPatronId() == null || patron.getPatronId().trim().isEmpty()) {
            throw new IllegalArgumentException("Patron and patron ID cannot be null or empty");
        }
        
        if (patrons.containsKey(patron.getPatronId())) {
            throw new IllegalArgumentException("Patron with ID " + patron.getPatronId() + " already exists");
        }
        
        patrons.put(patron.getPatronId(), patron);
        Logger.logInfo("Patron added: " + patron.getName() + " (ID: " + patron.getPatronId() + ")");
    }

    @Override
    public void removePatron(String patronId) {
        if (patronId == null || patronId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        Patron removedPatron = patrons.remove(patronId);
        if (removedPatron != null) {
            Logger.logInfo("Patron removed: " + removedPatron.getName() + " (ID: " + patronId + ")");
        } else {
            Logger.logWarning("Attempted to remove non-existent patron with ID: " + patronId);
        }
    }

    @Override
    public Optional<Patron> updatePatron(String patronId, Patron updatedPatron) {
        if (patronId == null || updatedPatron == null) {
            throw new IllegalArgumentException("Patron ID and updated patron cannot be null");
        }
        
        Patron existingPatron = patrons.get(patronId);
        if (existingPatron != null) {
            existingPatron.setName(updatedPatron.getName());
            existingPatron.setEmail(updatedPatron.getEmail());
            existingPatron.setPhoneNumber(updatedPatron.getPhoneNumber());
            existingPatron.setAddress(updatedPatron.getAddress());
            existingPatron.setPatronType(updatedPatron.getPatronType());
            Logger.logInfo("Patron updated: " + existingPatron.getName() + " (ID: " + patronId + ")");
            return Optional.of(existingPatron);
        }
        
        Logger.logWarning("Attempted to update non-existent patron with ID: " + patronId);
        return Optional.empty();
    }

    @Override
    public Optional<Patron> findPatronById(String patronId) {
        return Optional.ofNullable(patrons.get(patronId));
    }

    @Override
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }

    @Override
    public List<Patron> searchPatronsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return patrons.values().stream()
                .filter(patron -> patron.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
}

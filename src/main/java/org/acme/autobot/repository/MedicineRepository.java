package org.acme.autobot.repository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.autobot.entity.Medicine;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
public class MedicineRepository implements PanacheRepository<Medicine> {

    public Uni<Medicine> findByCui(String cui) {
        return find("cui", cui).firstResult();
    }

    public PanacheQuery<Medicine> searchByName(String name) {
        return find("LOWER(name) LIKE ?1", name);
    }
}

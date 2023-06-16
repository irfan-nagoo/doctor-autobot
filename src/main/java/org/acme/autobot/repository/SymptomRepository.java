package org.acme.autobot.repository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.autobot.entity.Symptom;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
public class SymptomRepository implements PanacheRepository<Symptom> {

    public Uni<Symptom> findByCui(String cui) {
        return find("cui", cui).firstResult();
    }

    public Uni<List<Symptom>> findByCuiList(List<String> cuiList) {
        return list("cui IN ?1", cuiList);
    }

    public PanacheQuery<Symptom> searchByName(String name) {
        return find("LOWER(name) LIKE ?1", name);
    }

}

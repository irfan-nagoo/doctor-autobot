package org.acme.autobot.repository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.autobot.entity.Disease;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
public class DiseaseRepository implements PanacheRepository<Disease> {

    public Uni<Disease> findByCui(String cui) {
        return find("cui", cui).firstResult();
    }

    public Uni<List<Disease>> findByCuiList(List<String> cuiList) {
        return list("cui IN ?1", cuiList);
    }

    public PanacheQuery<Disease> searchByName(String name) {
        return find("LOWER(name) LIKE ?1", name);
    }

}

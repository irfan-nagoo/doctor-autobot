package org.acme.autobot.util;

import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {

    @Test
    void getSort() {
        Sort sort = SortUtil.getSort("name", "asc");
        assertNotNull(sort.getColumns());
        assertFalse(sort.getColumns().isEmpty());
        assertEquals("name", sort.getColumns().get(0).getName());
        assertEquals(Sort.Direction.Ascending, sort.getColumns().get(0).getDirection());
    }

    @Test
    void getSort_Exception() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> SortUtil.getSort("cui,name", "asc"));
    }

    @Test
    void getSortDirection() {
        assertEquals(Sort.Direction.Ascending, SortUtil.getSortDirection("asc"));
        assertEquals(Sort.Direction.Descending, SortUtil.getSortDirection("desc"));
    }
}
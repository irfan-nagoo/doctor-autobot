package org.acme.autobot.util;

import io.quarkus.panache.common.Sort;
import org.acme.autobot.constants.AutoBotConstants;

import java.util.StringTokenizer;

import static org.acme.autobot.constants.MessagingConstants.SORT_FIELD_ERROR_MSG;

/**
 * @author irfan.nagoo
 */
public class SortUtil {

    public static Sort getSort(String sortBy, String sortDirection) {
        Sort sort = Sort.empty();
        StringTokenizer sortByTokenizer = new StringTokenizer(sortBy, AutoBotConstants.COMMA);
        StringTokenizer sortDirTokenizer = new StringTokenizer(sortDirection, AutoBotConstants.COMMA);
        if (sortByTokenizer.countTokens() == sortDirTokenizer.countTokens()) {
            while (sortByTokenizer.hasMoreTokens() && sortDirTokenizer.hasMoreTokens()) {
                sort = sort.and(sortByTokenizer.nextToken(), getSortDirection(sortDirTokenizer.nextToken()));
            }
        } else {
            throw new IllegalArgumentException(SORT_FIELD_ERROR_MSG);
        }
        return sort;
    }

    public static Sort.Direction getSortDirection(String sortDir) {
        return AutoBotConstants.ASC.equalsIgnoreCase(sortDir) ? Sort.Direction.Ascending :
                Sort.Direction.Descending;
    }
}

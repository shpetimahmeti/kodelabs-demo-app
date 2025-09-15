package org.kodelabs.domain.common.pagination.mapper;

import org.kodelabs.domain.common.pagination.PaginationFacetResult;
import org.kodelabs.domain.common.pagination.dto.PaginatedResponse;

import java.util.List;
import java.util.function.Function;

public class PaginationMapper {
    public static <T, R> PaginatedResponse<R> toPaginatedResponse(
            PaginationFacetResult<T> paginationFacetResult,
            int page,
            int size,
            Function<T, R> mapper
    ) {
        List<T> items = paginationFacetResult.getItems();


        if (items == null || items.isEmpty()) {
            return new PaginatedResponse<>(List.of(), page, size, 0L);
        }

        List<R> results = items.stream().map(mapper).toList();

        return new PaginatedResponse<>(results, page, size, paginationFacetResult.getTotalCount());
    }
}

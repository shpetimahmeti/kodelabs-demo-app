package org.kodelabs.domain.common.dto;

import java.util.List;

public class PaginationFacetResult<T> {
    private List<T> items;
    private Long totalCount;

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
}

package org.kodelabs.domain.airport.dto;

import org.kodelabs.domain.airport.AirportEntity;

import java.util.List;

public class AirportFacetResult {
    private List<AirportEntity> results;
    private List<CountResult> totalCount;

    public List<AirportEntity> getResults() {
        return results;
    }

    public void setResults(List<AirportEntity> results) {
        this.results = results;
    }

    public List<CountResult> getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(List<CountResult> totalCount) {
        this.totalCount = totalCount;
    }

    public static class CountResult {
        private long count;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

}

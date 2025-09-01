package org.kodelabs.domain.flight.dto;

import org.kodelabs.domain.flight.entity.FlightEntity;

import java.util.List;

public class FlightFacetResult {
    private List<FlightEntity> results;
    private List<CountResult> totalCount;

    public List<FlightEntity> getResults() {
        return results;
    }

    public void setResults(List<FlightEntity> results) {
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

package org.kodelabs.domain.common.pagination.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

import jakarta.validation.constraints.Min;

import static org.kodelabs.domain.common.mongo.Fields.ID;

public class PaginationQueryParams {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(0)
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(1)
    public int size;

    @QueryParam("asc")
    @DefaultValue("true")
    public boolean ascending;

    @QueryParam("sortField")
    @DefaultValue(ID)
    public String sortField;
}

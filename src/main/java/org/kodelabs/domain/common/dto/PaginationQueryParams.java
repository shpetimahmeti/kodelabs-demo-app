package org.kodelabs.domain.common.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

import jakarta.validation.constraints.Min;
import org.kodelabs.domain.common.annotation.ValidEntityField;
import org.kodelabs.domain.common.entity.BaseEntity;

import static org.kodelabs.domain.common.util.Fields.ID;

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
    @ValidEntityField(entity = BaseEntity.class, message = "Non existing sorting field")
    public String sortField;
}

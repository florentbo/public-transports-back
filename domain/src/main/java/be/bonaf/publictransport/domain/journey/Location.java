package be.bonaf.publictransport.domain.journey;

import lombok.Builder;

@Builder(builderMethodName = "aLocation", setterPrefix = "with")
public record Location(LocationId locationId, String name) {}

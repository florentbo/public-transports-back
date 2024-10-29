package be.bonaf.publictransport.domain.journey;

import java.time.OffsetDateTime;

public record ArrivalTime(
    String platformName,
    String destinationName,
    OffsetDateTime estimatedTimeOfArrival,
    String minutesAndSecondsToArrival) {}

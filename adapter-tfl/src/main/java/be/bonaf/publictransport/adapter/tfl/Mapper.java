package be.bonaf.publictransport.adapter.tfl;

import be.bonaf.publictransport.domain.schedule.Departure;
import org.openapitools.client.model.TflApiPresentationEntitiesArrivalDeparture;

import static be.bonaf.publictransport.domain.schedule.Departure.minutesAndSecondsToDeparture;

public class Mapper {
  static Departure from(TflApiPresentationEntitiesArrivalDeparture arrivalDeparture) {
    return Departure.builder()
        .platformName(arrivalDeparture.getPlatformName())
        .stationName(arrivalDeparture.getStationName())
        .stationId(arrivalDeparture.getNaptanId())
        .destinationName(arrivalDeparture.getDestinationName())
        .destinationId(arrivalDeparture.getDestinationNaptanId())
        .minutesAndSecondsToArrival(minutesAndSecondsToDeparture(arrivalDeparture.getMinutesAndSecondsToArrival()))
        .build();
  }
}

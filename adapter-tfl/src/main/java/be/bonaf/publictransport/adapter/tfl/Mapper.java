package be.bonaf.publictransport.adapter.tfl;

import org.openapitools.client.model.TflApiPresentationEntitiesArrivalDeparture;

import static be.bonaf.publictransport.adapter.tfl.Departure.minutesAndSecondsToDeparture;

public class Mapper {
  static Departure from(TflApiPresentationEntitiesArrivalDeparture arrivalDeparture) {
    return new Departure(
        arrivalDeparture.getPlatformName(),
        arrivalDeparture.getStationName(),
        arrivalDeparture.getDestinationName(),
        minutesAndSecondsToDeparture(arrivalDeparture.getMinutesAndSecondsToArrival()));
  }
}

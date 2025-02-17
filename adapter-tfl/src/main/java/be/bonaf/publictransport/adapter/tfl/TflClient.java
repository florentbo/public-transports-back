package be.bonaf.publictransport.adapter.tfl;

import be.bonaf.publictransport.domain.journey.ArrivalTime;
import be.bonaf.publictransport.domain.schedule.Departure;

import java.util.List;
import java.util.Map;

public interface TflClient {
  List<Departure> departures(String startingNaptanId, String destinationNaptanId, String lineId);
  Map<String, List<ArrivalTime>> arrivals(String stopId);

}

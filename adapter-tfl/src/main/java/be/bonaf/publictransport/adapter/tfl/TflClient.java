package be.bonaf.publictransport.adapter.tfl;

import be.bonaf.publictransport.domain.schedule.Departure;

import java.util.List;

public interface TflClient {
  List<Departure> departures(String startingNaptanId, String destinationNaptanId, String lineId);
}

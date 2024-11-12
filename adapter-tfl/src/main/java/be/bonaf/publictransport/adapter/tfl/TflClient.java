package be.bonaf.publictransport.adapter.tfl;

import java.util.List;

public interface TflClient {
  List<Departure> departures(String startingNaptanId, String destinationNaptanId, String lineId);
}

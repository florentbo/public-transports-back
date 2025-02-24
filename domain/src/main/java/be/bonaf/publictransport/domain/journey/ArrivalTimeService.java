package be.bonaf.publictransport.domain.journey;

import java.util.List;
import java.util.Map;

public interface ArrivalTimeService {
  List<ArrivalTime> arrivalTimes(Journey journey);

  Map<String, List<ArrivalTime>> arrivalTimesPerLine(Journey journeys);
}

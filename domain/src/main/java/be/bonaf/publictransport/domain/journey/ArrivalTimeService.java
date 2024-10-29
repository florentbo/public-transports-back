package be.bonaf.publictransport.domain.journey;

import java.util.List;

public interface ArrivalTimeService {
  List<ArrivalTime> arrivalTimes(Journey journey);
}

package be.bonaf.publictransport.domain.service;

import be.bonaf.publictransport.domain.journey.*;
import be.bonaf.publictransport.domain.journey.Journey.*;

import java.util.List;

public interface JourneyService {
  List<Journey> currentUserJourneys();

  List<ArrivalTime> arrivalTimes(JourneyId journey);

  List<TransportOption> transportOptions(JourneyId journey);
}

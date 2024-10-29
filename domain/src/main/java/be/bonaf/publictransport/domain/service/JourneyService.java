package be.bonaf.publictransport.domain.service;

import be.bonaf.publictransport.domain.journey.*;

import java.util.List;

public interface JourneyService {

  List<Journey> currentUserJourneys();

  List<ArrivalTime> arrivalTimesForJourney(Journey journey);
}

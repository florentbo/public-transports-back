package be.bonaf.publictransport.domain.user;

import be.bonaf.publictransport.domain.journey.Journey;

import java.util.List;

public interface UserRepository {
  User getCurrentUser();

  List<Journey> journeys();

  Journey journey(Journey.JourneyId journeyId);
}

package be.bonaf.publictransport.domain.journey;

import be.bonaf.publictransport.domain.user.UserId;
import java.util.List;

public interface JourneyRepository {
  List<Journey> journeysForUser(UserId userId);

  void saveJourney(UserId userId, Journey journey);
}

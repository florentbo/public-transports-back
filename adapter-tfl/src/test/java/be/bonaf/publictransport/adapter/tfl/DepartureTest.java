package be.bonaf.publictransport.adapter.tfl;

import be.bonaf.publictransport.domain.schedule.Departure;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

class DepartureTest {

  @ParameterizedTest
  @ValueSource(strings = {"9:6", "9:06", "09:6", "09:06"})
  void minutesAndSecondsToArrival(String minutesAndSeconds) {
    LocalTime minutesAndSecondsToArrival =
        Departure.minutesAndSecondsToDeparture(minutesAndSeconds);
    assertThat(minutesAndSecondsToArrival).isEqualTo(LocalTime.of(0, 9, 6));
  }
}

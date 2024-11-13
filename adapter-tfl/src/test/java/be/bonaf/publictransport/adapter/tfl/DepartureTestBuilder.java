package be.bonaf.publictransport.adapter.tfl;

import java.time.LocalTime;

public class DepartureTestBuilder {
  private String platformName;
  private String stationName;
  private String destinationName;
  private LocalTime minutesAndSecondsToArrival;

  public DepartureTestBuilder withPlatformName(String platformName) {
    this.platformName = platformName;
    return this;
  }

  public DepartureTestBuilder withStationName(String stationName) {
    this.stationName = stationName;
    return this;
  }

  public DepartureTestBuilder withDestinationName(String destinationName) {
    this.destinationName = destinationName;
    return this;
  }

  public DepartureTestBuilder withMinutesAndSecondsToArrival(int minutes, int seconds) {
    this.minutesAndSecondsToArrival = LocalTime.of(0, minutes, seconds);
    return this;
  }

  public Departure build() {
    return new Departure(platformName, stationName, destinationName, minutesAndSecondsToArrival);
  }

  public static DepartureTestBuilder aDefaultToLiverpoolStation() {
    return new DepartureTestBuilder()
        .withPlatformName("Platform 1")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withDestinationName("London Liverpool Street Rail Station")
        .withMinutesAndSecondsToArrival(3, 4);
  }

  public static DepartureTestBuilder aDefaultToCheshuntStation() {
    return new DepartureTestBuilder()
        .withPlatformName("Platform 2")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withDestinationName("Cheshunt Rail Station")
        .withMinutesAndSecondsToArrival(7, 4);
  }
}

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

  public DepartureTestBuilder withMinutesAndSecondsToArrival(LocalTime minutesAndSecondsToArrival) {
    this.minutesAndSecondsToArrival = minutesAndSecondsToArrival;
    return this;
  }

  public Departure build() {
    return new Departure(platformName, stationName, destinationName, minutesAndSecondsToArrival);
  }

  public static Departure aDefaultToLiverpoolStation() {
    return new DepartureTestBuilder()
        .withPlatformName("Platform 1")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withDestinationName("London Liverpool Street Rail Station")
        .withMinutesAndSecondsToArrival(LocalTime.of(0, 3, 4))
        .build();
  }
}

package be.bonaf.publictransport.adapter.tfl;

import java.time.LocalTime;

public class DepartureTestBuilder {
  private String platformName;
  private String stationName;
  private String stationId;
  private String destinationName;
  private String destinationId;
  private LocalTime minutesAndSecondsToArrival;

  public DepartureTestBuilder withPlatformName(String platformName) {
    this.platformName = platformName;
    return this;
  }

  public DepartureTestBuilder withStationName(String stationName) {
    this.stationName = stationName;
    return this;
  }

  public DepartureTestBuilder withStationId(String stationId) {
    this.stationId = stationId;
    return this;
  }

  public DepartureTestBuilder withDestinationName(String destinationName) {
    this.destinationName = destinationName;
    return this;
  }

  public DepartureTestBuilder withDestinationId(String destinationId) {
    this.destinationId = destinationId;
    return this;
  }

  public DepartureTestBuilder withMinutesAndSecondsToArrival(int minutes, int seconds) {
    this.minutesAndSecondsToArrival = LocalTime.of(0, minutes, seconds);
    return this;
  }

  public Departure build() {
    return new Departure(
        platformName,
        stationName,
        stationId,
        destinationName,
        destinationId,
        minutesAndSecondsToArrival);
  }

  public static DepartureTestBuilder aDefaultToLiverpoolStation() {
    return new DepartureTestBuilder()
        .withPlatformName("Platform 1")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withStationId("910GCAMHTH")
        .withDestinationName("London Liverpool Street Rail Station")
        .withDestinationId("910GLIVST")
        .withMinutesAndSecondsToArrival(3, 4);
  }

  public static DepartureTestBuilder aDefaultToCheshuntStation() {
    return new DepartureTestBuilder()
        .withPlatformName("Platform 2")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withStationId("910GCAMHTH")
        .withDestinationName("Cheshunt Rail Station")
        .withDestinationId("910GCHESHNT")
        .withMinutesAndSecondsToArrival(7, 4);
  }
}

package be.bonaf.publictransport.adapter.tfl;

import org.openapitools.client.model.TflApiPresentationEntitiesArrivalDeparture;

public class ArrivalDepartureTestBuilder {

  private String platformName;
  private String destinationNaptanId;
  private String destinationName;
  private String naptanId;
  private String stationName;
  private String minutesAndSecondsToArrival;

  public ArrivalDepartureTestBuilder withPlatformName(String platformName) {
    this.platformName = platformName;
    return this;
  }

  public ArrivalDepartureTestBuilder withDestinationNaptanId(String destinationNaptanId) {
    this.destinationNaptanId = destinationNaptanId;
    return this;
  }

  public ArrivalDepartureTestBuilder withDestinationName(String destinationName) {
    this.destinationName = destinationName;
    return this;
  }

  public ArrivalDepartureTestBuilder withNaptanId(String naptanId) {
    this.naptanId = naptanId;
    return this;
  }

  public ArrivalDepartureTestBuilder withStationName(String stationName) {
    this.stationName = stationName;
    return this;
  }

  public ArrivalDepartureTestBuilder withMinutesAndSecondsToArrival(
      String minutesAndSecondsToArrival) {
    this.minutesAndSecondsToArrival = minutesAndSecondsToArrival;
    return this;
  }

  public TflApiPresentationEntitiesArrivalDeparture build() {
    return new TflApiPresentationEntitiesArrivalDeparture()
        .platformName(platformName)
        .destinationNaptanId(destinationNaptanId)
        .destinationName(destinationName)
        .naptanId(naptanId)
        .stationName(stationName)
        .minutesAndSecondsToArrival(minutesAndSecondsToArrival);
  }

  public static TflApiPresentationEntitiesArrivalDeparture aDefaultToLiverpoolStation() {
    return new ArrivalDepartureTestBuilder()
        .withPlatformName("Platform 1")
        .withDestinationNaptanId("910GLIVST")
        .withDestinationName("London Liverpool Street Rail Station")
        .withNaptanId("910GCAMHTH")
        .withStationName("Cambridge Heath (London) Rail Station")
        .withMinutesAndSecondsToArrival("3:4")
        .build();
  }
}

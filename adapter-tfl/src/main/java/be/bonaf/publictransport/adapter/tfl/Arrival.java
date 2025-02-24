package be.bonaf.publictransport.adapter.tfl;

import lombok.Builder;

@Builder
public record Arrival(String direction, int secondsToStation) {
  public static Arrival of(String direction, int secondsToStation) {
    return new Arrival(direction, secondsToStation);
  }
}

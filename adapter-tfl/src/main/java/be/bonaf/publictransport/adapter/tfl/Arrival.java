package be.bonaf.publictransport.adapter.tfl;

import lombok.Builder;

@Builder
public record Arrival(String towards, int secondsToStation) {
  public static Arrival of(String towards, int secondsToStation) {
    return new Arrival(towards, secondsToStation);
  }
}

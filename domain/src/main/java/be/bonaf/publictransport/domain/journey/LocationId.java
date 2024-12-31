package be.bonaf.publictransport.domain.journey;

import java.util.UUID;

public record LocationId(UUID value) {
  public static LocationId from(String value) {
    return new LocationId(UUID.fromString(value));
  }

  public static LocationId of(UUID value) {
    return new LocationId(value);
  }

  public static LocationId create() {
    return new LocationId(UUID.randomUUID());
  }
}

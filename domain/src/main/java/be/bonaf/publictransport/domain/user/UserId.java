package be.bonaf.publictransport.domain.user;

import java.util.UUID;

public record UserId(UUID value) {
    public static UserId from(String value) {
        return new UserId(UUID.fromString(value));
    }
}

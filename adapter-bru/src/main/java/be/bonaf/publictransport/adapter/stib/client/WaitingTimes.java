package be.bonaf.publictransport.adapter.stib.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.*;
import java.util.*;
import lombok.Builder;

public record WaitingTimes(@JsonProperty("total_count") int count, List<Result> results) {

  @Builder(builderMethodName = "aResult", setterPrefix = "with")
  public record Result(
      @JsonProperty("pointid") String pointId,
      @JsonProperty("lineid") String lineId,
      @JsonProperty("passingtimes") @JsonDeserialize(using = PassingTimesDeserializer.class)
          List<PassingTime> passingTimes) {

    @Builder(builderMethodName = "aPassingTime", setterPrefix = "with")
    public record PassingTime(
        Destination destination,
        Message message,
        OffsetDateTime expectedArrivalTime,
        String lineId) {
      @Builder(builderMethodName = "aDestination", setterPrefix = "with")
      public record Destination(String fr, String nl) {}

      public record Message(String en, String fr, String nl) {}
    }
  }

  public static class PassingTimesDeserializer extends JsonDeserializer<List<Result.PassingTime>> {
    @Override
    public List<Result.PassingTime> deserialize(
        JsonParser jsonParser, DeserializationContext context) throws IOException {
      ObjectMapper objectMapper =
          new ObjectMapper()
              .registerModule(new JavaTimeModule())
              .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
      return objectMapper.readValue(jsonParser.getText(), new TypeReference<>() {});
    }
  }
}

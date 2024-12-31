package be.bonaf.publictransport.adapter.stib;

import static org.assertj.core.api.Assertions.assertThat;


import static be.bonaf.publictransport.adapter.stib.WaitingTimes.Result.*;
import static be.bonaf.publictransport.adapter.stib.WaitingTimes.Result.PassingTime.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class WaitingTimesParserTest {

  @Test
  void parse() throws Exception {
    String jsonString =
        """
                    {
                      "total_count": 1,
                      "results": [
                        {
                          "pointid": "8784",
                          "lineid": "6",
                          "passingtimes": "[{\\"destination\\": {\\"fr\\": \\"ELISABETH\\", \\"nl\\": \\"ELISABETH\\"}, \\"expectedArrivalTime\\": \\"2024-12-27T18:51:00+01:00\\", \\"lineId\\": \\"6\\"}, {\\"destination\\": {\\"fr\\": \\"ELISABETH\\", \\"nl\\": \\"ELISABETH\\"}, \\"expectedArrivalTime\\": \\"2024-12-27T18:56:00+01:00\\", \\"lineId\\": \\"6\\"}]"
                        }
                      ]
                    }
                    """;

    var passTime =
        aPassingTime()
            .withDestination(new Destination("ELISABETH", "ELISABETH"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-27T18:51+01:00"))
            .withLineId("6")
            .build();

    var passTime2 =
        aPassingTime()
            .withDestination(new Destination("ELISABETH", "ELISABETH"))
            .withExpectedArrivalTime(OffsetDateTime.parse("2024-12-27T18:56+01:00"))
            .withLineId("6")
            .build();

    var result =
        aResult()
            .withPointId("8784")
            .withLineId("6")
            .withPassingTimes(List.of(passTime, passTime2))
            .build();

    ObjectMapper mapper = new ObjectMapper();
    WaitingTimes actual = mapper.readValue(jsonString, WaitingTimes.class);

    var expected = new WaitingTimes(1, List.of(result));
    assertThat(actual).isEqualTo(expected);
  }
}

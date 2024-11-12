package be.bonaf.publictransport.adapter.tfl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

  @Test
  void from() {
    var arrivalDeparture = ArrivalDepartureTestBuilder.aDefaultToLiverpoolStation();
    assertThat(Mapper.from(arrivalDeparture))
        .isEqualTo(DepartureTestBuilder.aDefaultToLiverpoolStation());
  }
}

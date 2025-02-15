package be.bonaf.publictransport.adapter.stib.client;

// 8814001 = Brussels-South
// 8893401 = Dendermonde
// 8814001:8893401 = Brussels-South - Dendermonde
// 8893401:8814001 = Dendermonde - Brussels-South

// 8815016 tour et taxi

import com.google.transit.realtime.GtfsRealtime;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SncbFeed {
  public static void main(String[] args) throws Exception {
    URL url =
        new URL("https://sncb-opendata.hafas.de/gtfs/realtime/c21ac6758dd25af84cca5b707f3cb3de");
    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());

    /*  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
    .withZone(ZoneId.of("Europe/Brussels"));*/

    LocalDateTime localDateTime = getLocalDateTime(feed.getHeader().getTimestamp());
    System.out.println("Feed timestamp: " + localDateTime);
    System.out.println("Number of entities: " + feed.getEntityList().size());

    for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {

      // System.out.println("Entity: " + entity);
      var tripUpdate = entity.getTripUpdate();
      String tripId = tripUpdate.getTrip().getTripId();

      // 8814001:8893401 = Brussels-South - Dendermonde
      // 8893401:8814001 = Dendermonde - Brussels-South
      if (tripId.contains("8814001:8893401")
      //                || tripId.contains("8893401:8814001")
      ) {
        var stopTimeUpdates = tripUpdate.getStopTimeUpdateList();
         System.out.println("FeedEntity: " + entity);
        // System.out.println("\ntrip getStartDate: " + trip.getStartDate());
        // System.out.println("trip getStartTime: " + trip.getStartTime());

        stopTimeUpdates.stream()
            .map(
                stopTimeUpdate -> {
                  // System.out.println("Departure: " + stopTimeUpdate);
                  long time = stopTimeUpdate.getDeparture().getTime();
                  LocalDateTime departureInstant = getLocalDateTime(time);
                  // System.out.println("Departure: " + departureInstant);
                  return StopTimeUpdate.of(
                      departureInstant, tripId, stopTimeUpdate.getStopId(), entity.toString());
                })
            .filter(
                stopTimeUpdate -> {
                  // return stopTimeUpdate.stopId().equals("8815016"); // tou et taxi
                  return stopTimeUpdate.stopId().equals("8814001");
                  // return stopTimeUpdate.departure().getHour() == 18

                })
            .forEach(
                update -> {
                  System.out.println("StopTimeUpdate departure: " + update.departure());
                  // System.out.println("StopTimeUpdate content: " + update.content);

                  /*System.out.println("\nTrain ID: " + x.tripId());
                  System.out.println("From: " + x.from());
                  System.out.println("To: " + x.to());*/
                });

        /*if (!stopTimeUpdates.isEmpty()) {
        System.out.println("\nTrain ID: " + tripUpdate.getTrip().getTripId());
        System.out.println("From: " + stopTimeUpdates.get(0).getStopId());
        System.out.println("To: " + stopTimeUpdates.get(stopTimeUpdates.size() - 1).getStopId());

        stopTimeUpdates.forEach(
            stopTimeUpdate -> {
              long time = stopTimeUpdate.getDeparture().getTime();
              Instant departureInstant = Instant.ofEpochSecond(time);
              System.out.println("Departure: " + departureInstant);
            });*/

        /*var firstStop = stopTimeUpdates.get(0);
        if (firstStop.hasDeparture()) {
          Instant departureInstant = Instant.ofEpochSecond(firstStop.getDeparture().getTime());
          System.out.println("Departure: " + departureInstant);
        }*/
      }
    }
  }

  record StopTimeUpdate(LocalDateTime departure, String tripId, String stopId, String content) {
    static StopTimeUpdate of(LocalDateTime departure, String tripId, String stopId, String to) {
      return new StopTimeUpdate(departure, tripId, stopId, to);
    }
  }

  static LocalDateTime getLocalDateTime(long timestamp) {
    Instant instant = Instant.ofEpochSecond(timestamp);
    return LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Brussels"));
  }
}

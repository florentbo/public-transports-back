package be.bonaf.publictransport.adapter.stib.client;

import com.google.transit.realtime.GtfsRealtime;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.*;

import static be.bonaf.publictransport.adapter.stib.client.SncbFeed.getLocalDateTime;

public class BeRailClient {
  public static void main(String[] args) throws Exception {

    URL url =
        new URL("https://sncb-opendata.hafas.de/gtfs/realtime/c21ac6758dd25af84cca5b707f3cb3de");
    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
    Set<LocalDateTime> dep =
        feed.getEntityList().stream()
            .filter(
                entity -> entity.getTripUpdate().getTrip().getTripId().contains("8814001:8893401"))
            .flatMap(entity -> entity.getTripUpdate().getStopTimeUpdateList().stream())
            .filter(stopTimeUpdate -> stopTimeUpdate.getStopId().equals("8814001"))
            .map(stopTimeUpdate -> getLocalDateTime(stopTimeUpdate.getDeparture().getTime()))
            .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    System.out.println(dep);
  }

  static LocalDateTime getLocalDateTime(long timestamp) {
    Instant instant = Instant.ofEpochSecond(timestamp);
    return LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Brussels"));
  }
}

package be.bonaf.publictransport.domain.user;

import be.bonaf.publictransport.domain.journey.Journey;

import java.util.*;

public record User(UserId id, List<Journey> journeys) {}

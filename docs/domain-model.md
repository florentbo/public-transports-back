# Unified Domain Model - Personal Transport Assistant

This document defines the domain model for the Personal Transport Assistant, aligned with the vision of user-centered transport optimization.

## Domain Model Overview

```mermaid
classDiagram
    class User {
        +UUID id
        +String email
        +String name
        +UserPreferences preferences
        +DateTime createdAt
        +DateTime updatedAt
    }

    class PersonalRoute {
        +UUID id
        +UUID userId
        +String routeName
        +String description
        +PersonalLocation origin
        +PersonalLocation destination
        +RouteType routeType
        +RouteSchedule routeSchedule
        +RouteMetadata metadata
        +RouteStatistics statistics
        +List~TransportSegment~ transportSegments
        +DateTime createdAt
        +DateTime updatedAt
        +recordUsage()
        +updateStatistics()
        +isActive()
    }

    class PersonalLocation {
        +String name
        +String personalName
        +String address
        +Coordinates coordinates
        +LocationType locationType
        +distanceTo(PersonalLocation)
    }

    class Coordinates {
        +Double latitude
        +Double longitude
        +distanceTo(Coordinates)
    }

    class TransportSegment {
        +Integer segmentOrder
        +TransportType transportType
        +String line
        +Station startStation
        +Station endStation
        +Integer estimatedDuration
        +Double estimatedCost
        +String platform
        +String direction
        +String notes
    }

    class RouteSchedule {
        +Time preferredDepartureTime
        +Time preferredArrivalTime
        +List~DayOfWeek~ activeDays
        +Integer flexibility
        +isActiveToday()
        +getNextDeparture()
    }

    class RouteMetadata {
        +Boolean isFavorite
        +Boolean isActive
        +String color
        +String icon
        +List~String~ tags
        +toggleFavorite()
    }

    class RouteStatistics {
        +DateTime lastUsed
        +Integer timesUsed
        +Integer averageDuration
        +Double averageCost
        +Double successRate
        +Double reliabilityScore
        +Double satisfactionRating
        +updateFromUsage(RouteUsage)
    }

    class RouteUsage {
        +UUID id
        +UUID routeId
        +DateTime usedAt
        +Integer actualDuration
        +Integer expectedDuration
        +Boolean onTime
        +Integer satisfactionRating
        +Weather weather
        +List~Disruption~ disruptions
    }

    class Station {
        +String id
        +String name
        +List~String~ lines
        +Coordinates coordinates
    }

    class UserPreferences {
        +Time defaultDepartureTime
        +NotificationSettings notifications
        +AccessibilitySettings accessibility
        +String preferredTransportTypes
    }

    User "1" --> "*" PersonalRoute : owns
    PersonalRoute "1" --> "1" PersonalLocation : origin
    PersonalRoute "1" --> "1" PersonalLocation : destination
    PersonalRoute "1" --> "*" TransportSegment : contains
    PersonalRoute "1" --> "1" RouteSchedule : has
    PersonalRoute "1" --> "1" RouteMetadata : has
    PersonalRoute "1" --> "1" RouteStatistics : tracks
    PersonalRoute "1" --> "*" RouteUsage : history
    PersonalLocation "1" --> "1" Coordinates : has
    TransportSegment "1" --> "2" Station : connects
    User "1" --> "1" UserPreferences : has
```

## Bounded Contexts

```mermaid
graph TB
    subgraph "User Management Context"
        U[User]
        UP[UserPreferences]
        AUTH[Authentication]
    end
    
    subgraph "Route Management Context"
        PR[PersonalRoute]
        PL[PersonalLocation]
        TS[TransportSegment]
        RS[RouteSchedule]
        RM[RouteMetadata]
    end
    
    subgraph "Usage Analytics Context"
        RU[RouteUsage]
        RST[RouteStatistics]
        ML[ML Predictions]
        INSIGHTS[Usage Insights]
    end
    
    subgraph "Transport Information Context"
        RT[Real-time Data]
        ST[Stations]
        LI[Lines]
        DISRUPTIONS[Service Disruptions]
    end
    
    U --> PR
    PR --> RU
    RU --> RST
    RU --> ML
    PR --> RT
    RT --> DISRUPTIONS
```

## Aggregates and Aggregate Roots

### User Aggregate
- **Root**: User
- **Entities**: UserPreferences
- **Boundaries**: User profile management, authentication
- **Invariants**: Email must be unique, preferences must be valid

### PersonalRoute Aggregate
- **Root**: PersonalRoute
- **Value Objects**: PersonalLocation, Coordinates, RouteSchedule, RouteMetadata, RouteStatistics
- **Entities**: TransportSegment
- **Boundaries**: Route definition, metadata, and basic statistics
- **Invariants**: Route must have origin and destination, segments must be ordered, route name must be unique per user

### RouteUsage Aggregate
- **Root**: RouteUsage
- **Boundaries**: Individual usage events and analytics
- **Invariants**: Usage must reference valid route, timestamps must be consistent

## Domain Events

```mermaid
sequenceDiagram
    participant User
    participant PersonalRoute
    participant RouteUsage
    participant Statistics
    participant ML

    User->>PersonalRoute: Create Route
    PersonalRoute-->>User: PersonalRouteCreated Event
    
    User->>PersonalRoute: Use Route
    PersonalRoute->>RouteUsage: Record Usage
    RouteUsage-->>Statistics: RouteUsed Event
    RouteUsage-->>ML: UsageRecorded Event
    
    Statistics->>PersonalRoute: Update Statistics
    Statistics-->>PersonalRoute: StatisticsUpdated Event
    
    ML->>PersonalRoute: Update Predictions
    ML-->>PersonalRoute: PredictionsUpdated Event
    
    User->>PersonalRoute: Toggle Favorite
    PersonalRoute-->>User: FavoriteToggled Event
```

## Key Domain Events

1. **PersonalRouteCreated** - New route added to user's collection
2. **PersonalRouteUpdated** - Route metadata or segments changed
3. **PersonalRouteUsed** - User completed a journey using this route
4. **FavoriteRouteToggled** - Route favorite status changed
5. **RouteStatisticsUpdated** - Statistics recalculated from usage data
6. **RouteDeactivated** - Route marked as no longer active
7. **UsagePatternsDetected** - ML system detected new usage patterns

## Value Objects Details

### RouteType Enum
- `DAILY_COMMUTE` - Regular work commute
- `WEEKLY_ROUTINE` - Weekly recurring trips
- `OCCASIONAL` - Infrequent but remembered routes
- `WEEKEND` - Weekend/leisure routes
- `SPECIAL` - Event-specific routes

### LocationType Enum
- `HOME` - Primary residence
- `WORK` - Workplace or office
- `FRIEND` - Friend's or family's place
- `GYM` - Fitness center
- `SCHOOL` - Educational institution
- `SHOPPING` - Shopping center or market
- `RESTAURANT` - Dining location
- `OTHER` - General purpose location

### TransportType Enum
- `METRO` - Underground/subway
- `BUS` - Bus service
- `TRAM` - Tram/streetcar
- `TRAIN` - Regional/national train
- `WALK` - Walking segment
- `BIKE` - Bicycle (personal or shared)
- `CAR` - Car (personal or shared)

## Business Rules

### PersonalRoute Rules
1. A route must have exactly one origin and one destination
2. Transport segments must be ordered sequentially
3. Route name must be unique per user
4. Inactive routes are hidden from main views but preserved for history
5. Favorite routes appear at the top of user's route list

### RouteUsage Rules
1. Usage can only be recorded for active routes
2. Actual duration must be positive
3. Satisfaction rating must be between 1-5
4. On-time calculation based on expected vs actual arrival time

### Statistics Rules
1. Statistics are computed from usage history
2. Success rate = percentage of on-time arrivals
3. Reliability score combines success rate, duration consistency, and user satisfaction
4. Statistics are updated asynchronously after usage recording

## Integration Points

### External Systems
- **Transport APIs** - Real-time arrival information
- **Mapping Services** - Geocoding and route calculation
- **Weather APIs** - Context for usage analysis
- **Push Notifications** - Departure reminders and alerts

### Internal Services
- **Authentication Service** - User identity management
- **Analytics Service** - Usage pattern analysis and ML
- **Notification Service** - User alerts and reminders

## Future Extensions

### Planned Features
- **Route Sharing** - Share routes with other users
- **Group Coordination** - Coordinate departures with friends/colleagues
- **Smart Suggestions** - AI-powered route optimization suggestions
- **Predictive Departures** - Optimal departure time recommendations
- **Weather Integration** - Route adaptations based on weather conditions
- **Event Awareness** - Adjustments for local events affecting transport

### Domain Evolution
- **SocialRoute Aggregate** - For shared and group routes
- **PredictiveModel Aggregate** - For AI/ML predictions
- **EventContext Value Object** - For external events affecting routes
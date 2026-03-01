package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.exceptions.UserNotFound;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserRepository;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderEventResponse;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderEvent;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderEventRepository;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelPriceChangedResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.ManagerAssignedToFuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.ManagerUnassignedFromFuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.domain.events.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationEventRepository;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import com.fuelstation.managmentapi.manager.domain.Manager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class GetFuelStationEvents {

    private static final int DEFAULT_LIMIT = 10;

    private final FuelStationEventRepository fuelStationEventRepository;
    private final FuelOrderEventRepository fuelOrderEventRepository;
    private final UserRepository userRepository;

    public List<DomainEventResponse> process(long fuelStationId, Instant occurredAfter) {
        List<DomainEvent> events = new ArrayList<>();
        if (occurredAfter != null) {
            events.addAll(
                    fuelStationEventRepository.findByFuelStationIdAfter(fuelStationId, occurredAfter, DEFAULT_LIMIT)
            );
            events.addAll(
                    fuelOrderEventRepository.findByFuelStationIdAfter(fuelStationId, occurredAfter, DEFAULT_LIMIT)
            );
        } else {
            events.addAll(fuelStationEventRepository.findLatestByFuelStationId(fuelStationId, DEFAULT_LIMIT));
            events.addAll(fuelOrderEventRepository.findLatestByFuelStationId(fuelStationId, DEFAULT_LIMIT));
        }
        events = events.stream()
                .sorted(Comparator.comparing(DomainEvent::getOccurredAt).reversed())
                .limit(DEFAULT_LIMIT)
                .toList();

        Set<Long> userIds = events.stream()
                .flatMap(event -> {
                    Long userId = event.getPerformedBy().isSystem() ? null : event.getPerformedBy().id();
                    return switch (event) {
                        case ManagerAssignedToFuelStation e -> Stream.of(userId, e.getManagerId());
                        case ManagerUnassignedFromFuelStation e -> Stream.of(userId, e.getManagerId());
                        default -> Stream.of(userId);
                    };
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, User> usersById = userRepository.findByIds(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        return events.stream()
                .map(event -> {
                    var performerResponse = event.getPerformedBy().isSystem()
                            ? null
                            : UserResponse.fromUser(getUserById(event.getPerformedBy().id(), usersById));

                    return switch (event) {
                        case ManagerAssignedToFuelStation e -> new ManagerAssignedToFuelStationResponse(
                                e.getOccurredAt(),
                                performerResponse,
                                ManagerResponse.fromDomain(Manager.fromUser(getUserById(e.getManagerId(), usersById)))
                        );
                        case ManagerUnassignedFromFuelStation e -> new ManagerUnassignedFromFuelStationResponse(
                                e.getOccurredAt(),
                                performerResponse,
                                ManagerResponse.fromDomain(Manager.fromUser(getUserById(e.getManagerId(), usersById)))
                        );
                        case FuelPriceChanged e -> new FuelPriceChangedResponse(
                                e.getOccurredAt(),
                                performerResponse,
                                e.getFuelGrade(),
                                e.getPricePerLiter()
                        );
                        case FuelOrderEvent e -> new FuelOrderEventResponse(
                                e.getType().name(),
                                e.getOccurredAt(),
                                performerResponse,
                                e.getFuelOrderId(),
                                e.getFuelStationId()
                        );
                        case FuelStationEvent e -> new DomainEventResponse(
                                e.getType().name(),
                                e.getOccurredAt(),
                                performerResponse
                        );
                        default -> throw new IllegalStateException(
                                "Unknown event type: " + event.getClass().getSimpleName() + ", event=" + event
                        );
                    };
                })
                .toList();
    }

    private User getUserById(long userId, Map<Long, User> usersById) {
        var user = usersById.get(userId);
        if (user == null) {
            throw new UserNotFound(userId);
        }
        return user;
    }
}

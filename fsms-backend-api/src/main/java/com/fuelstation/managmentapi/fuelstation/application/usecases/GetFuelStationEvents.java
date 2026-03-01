package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.exceptions.UserNotFound;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserRepository;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelPriceChangedResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.ManagerAssignedToFuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.ManagerUnassignedFromFuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.domain.events.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationEventRepository;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import com.fuelstation.managmentapi.manager.domain.Manager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class GetFuelStationEvents {

    private final FuelStationEventRepository fuelStationEventRepository;
    private final UserRepository userRepository;

    public Page<DomainEventResponse> process(long fuelStationId, String eventType, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));

        Page<FuelStationEvent> events;
        if (eventType != null && !eventType.isBlank()) {
            events = fuelStationEventRepository.findByFuelStationIdAndEventType(
                    fuelStationId,
                    FuelStationEventType.fromString(eventType),
                    pageable
            );
        } else {
            events = fuelStationEventRepository.findByFuelStationId(fuelStationId, pageable);
        }

        var userIds = events.stream()
                .flatMap(event -> {
                    var performedBy = event.getPerformedBy();
                    var userId = performedBy.isSystem() ? null : performedBy.id();
                    return switch (event) {
                        case ManagerAssignedToFuelStation e -> Stream.of(userId, e.getManagerId());
                        case ManagerUnassignedFromFuelStation e -> Stream.of(userId, e.getManagerId());
                        default -> Stream.of(userId);
                    };
                })
                .collect(Collectors.toCollection(HashSet::new));
        Map<Long, User> usersById = userRepository.findByIds(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        return events.map(event -> {
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
                default -> new DomainEventResponse(
                        event.getType().name(),
                        event.getOccurredAt(),
                        performerResponse
                );
            };
        });
    }

    private User getUserById(long userId, Map<Long, User> usersById) {
        var user = usersById.get(userId);
        if (user == null) {
            throw new UserNotFound(userId);
        }
        return user;
    }
}
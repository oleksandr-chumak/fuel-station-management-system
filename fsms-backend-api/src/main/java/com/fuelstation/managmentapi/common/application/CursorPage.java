package com.fuelstation.managmentapi.common.application;

import java.util.List;

public record CursorPage<T, C>(
        List<T> content,
        long totalElements,
        boolean hasMore,
        C nextCursor
) {}
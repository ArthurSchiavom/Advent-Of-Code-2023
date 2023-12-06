package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RaceResult {
    private final long duration;
    private final long winDistance;
}

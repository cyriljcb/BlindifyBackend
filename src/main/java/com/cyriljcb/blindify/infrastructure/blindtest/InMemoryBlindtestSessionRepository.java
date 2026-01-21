package com.cyriljcb.blindify.infrastructure.blindtest;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Repository;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;

@Repository
public class InMemoryBlindtestSessionRepository
        implements BlindtestSessionRepository {

    private final AtomicReference<Blindtest> current = new AtomicReference<>();

    @Override
    public Optional<Blindtest> getCurrent() {
        return Optional.ofNullable(current.get());
    }

    @Override
    public void save(Blindtest blindtest) {
        current.set(blindtest);
    }

    @Override
    public void clear() {
        current.set(null);
    }
}

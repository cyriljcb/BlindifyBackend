package com.cyriljcb.blindify.infrastructure.blindtest;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Repository;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;
import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;

@Repository
public class InMemoryBlindtestSessionRepository
        implements BlindtestSessionRepository {

    private final AtomicReference<Blindtest> current = new AtomicReference<>();

    @Override
    public Blindtest getCurrent() {
        var blindtest = current.get();
        if (blindtest == null) {
            throw new NoActiveBlindtestException("No active blindtest session");
        }
        return blindtest;
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

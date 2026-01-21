package com.cyriljcb.blindify.domain.blindtest.port;


import java.util.Optional;
import com.cyriljcb.blindify.domain.blindtest.Blindtest;

public interface BlindtestSessionRepository {

    Optional<Blindtest> getCurrent();
    void save(Blindtest blindtest);
    void clear();
}

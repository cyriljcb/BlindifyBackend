package com.cyriljcb.blindify.domain.blindtest.port;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;

public interface BlindtestSessionRepository {

    Blindtest getCurrent();
    void save(Blindtest blindtest);
    void clear();
}

package com.hogatte.feature.destinationcheck;

import com.hogatte.feature.destinationcheck.model.DestinationCheckRequest;
import com.hogatte.feature.destinationcheck.model.DestinationCheckResponse;

public interface DestinationCheckService {

    DestinationCheckResponse check(DestinationCheckRequest request);
}
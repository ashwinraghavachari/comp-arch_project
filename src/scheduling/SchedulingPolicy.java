package scheduling;

import processing.ProcessingUnit;
import requests.Request;

public interface SchedulingPolicy {

    void setFrequencies(Request req);

    void setCPU(ProcessingUnit cpu);
    void setGPU(ProcessingUnit gpu);
}

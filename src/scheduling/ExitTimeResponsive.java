package scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import processing.ProcessingUnit;
import requests.Request;
import utils.FREQ_STATE;

public class ExitTimeResponsive implements SchedulingPolicy {

    private ProcessingUnit cpu;
    private ProcessingUnit gpu;

    private final static int THRESHOLD_LOW = SCHEDULING_THRESHOLDS.LOW.getThreshold();
    private final static int THRESHOLD_MID = SCHEDULING_THRESHOLDS.MID.getThreshold();
    private final static int THRESHOLD_HIGH = SCHEDULING_THRESHOLDS.HIGH.getThreshold();

    List<Request> cpuReqsInSystem = new ArrayList<>();
    List<Request> gpuReqsInSystem = new ArrayList<>();

    List<Request> cpuReqsFinished = new ArrayList<>();
    List<Request> gpuReqsFinished = new ArrayList<>();

    FREQ_STATE gpuDVFS = FREQ_STATE.GPU0;
    FREQ_STATE cpuDVFS = FREQ_STATE.CPU0;

    @Override
    public void setFrequencies(Request req) {
        if(req.needsGPU() || gpuReqsInSystem.contains(req))
        {
            //Uses gpu
            if(!gpuReqsInSystem.remove(req))
                gpuReqsInSystem.add(req);
            else
                gpuReqsFinished.add(req);
                
            gpuDVFS = updateGPU();
        }
        else
        {
            //Uses cpu
            if(!cpuReqsInSystem.remove(req))
                cpuReqsInSystem.add(req);
            else
                cpuReqsFinished.add(req);
            cpuDVFS = updateCPU();
        }
        gpu.setFreq(gpuDVFS);
        cpu.setFreq(cpuDVFS);
    }
    
    private FREQ_STATE updateGPU()
    {
        Double averageFinish = gpuReqsFinished.stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()));
        Double averageSLA = gpuReqsFinished.stream().collect(Collectors.averagingLong(r -> r.getSLA()));

        if(averageFinish > averageSLA)
            return FREQ_STATE.GPU2;
        else if(averageFinish * 2 > averageSLA)
        {
            switch(gpuDVFS)
            {
            case GPU0:
                return FREQ_STATE.GPU0;
            case GPU1:
                return FREQ_STATE.GPU0;
            case GPU2:
                return FREQ_STATE.GPU1;
            default:
                return FREQ_STATE.GPU0;
            }
        }

        if(gpuReqsInSystem.size() < THRESHOLD_LOW){
            return FREQ_STATE.GPU0;
        }
        else if (gpuReqsInSystem.size() < THRESHOLD_MID){
            return (FREQ_STATE.GPU1);
        }
        else if (gpuReqsInSystem.size() < THRESHOLD_HIGH){
            return (FREQ_STATE.GPU2);
        }
        else {
            return (FREQ_STATE.GPU2);
        }
    }

    private FREQ_STATE updateCPU()
    {
        Double averageFinish = cpuReqsFinished.stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()));
        Double averageSLA = cpuReqsFinished.stream().collect(Collectors.averagingLong(r -> r.getSLA()));

        if(averageFinish > averageSLA)
            return FREQ_STATE.CPU3;
        else if(averageFinish > averageSLA * 2)
        {
            switch(cpuDVFS)
            {
            case CPU0:
                return FREQ_STATE.CPU0;
            case CPU1:
                return FREQ_STATE.CPU0;
            case CPU2:
                return FREQ_STATE.CPU1;
            case CPU3:
                return FREQ_STATE.CPU2;
            default:
                return FREQ_STATE.CPU0;
            }
        }
        
        if(cpuReqsInSystem.size() < THRESHOLD_LOW){
            return (FREQ_STATE.CPU0);
        }
        else if (cpuReqsInSystem.size() < THRESHOLD_MID){
            return (FREQ_STATE.CPU1);
        }
        else if (cpuReqsInSystem.size() < THRESHOLD_HIGH){
            return (FREQ_STATE.CPU2);
        }
        else {
            return (FREQ_STATE.CPU3);
        }
    }

    public void setCPU(ProcessingUnit cpu){
        this.cpu = cpu;
    }

    public void setGPU(ProcessingUnit gpu){
        this.gpu = gpu;
    }

}

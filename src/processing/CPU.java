package processing;
import java.util.HashMap;
import java.util.Map;

import requests.Request;
import simulator.Simulator;
import utils.FREQ_STATE;

public class CPU extends ProcessingUnit{
	private double totalEnergy;
	private Map<Double, Double> freqToVolt;
	Simulator sim;
	
	public CPU(Simulator sim){
		// Maps freq (GHz) to voltage (V)
		// Specs taken from the intel i7
		this.freqToVolt = new HashMap<Double, Double>();
		this.freqToVolt.put(2.92, 1.176);
		this.freqToVolt.put(3.65, 1.240);
		this.freqToVolt.put(3.80, 1.336);
		this.freqToVolt.put(4.00, 1.496);
		
		this.sim = sim;
	}
	
	@Override
	public void processRequest (Request req) {
		long cpucycles;
		
		// Send request to GPU if needed
		if (req.needsGPU()){
		    req.setNeedsGPU(false);
			req.setDestination(inboundPU);
			inboundPU.setFreq(FREQ_STATE.GPU2);
			cpucycles = req.getPreGPUCycles();
		}
		else {
			req.setDestination(outboundPU);
			inboundPU.setFreq(FREQ_STATE.GPU0);
			cpucycles = req.getCPUCycles();
		}

		double time = (double)cpucycles / freq;
		double voltage = freqToVolt.get(freq);
		double curEnergy = time * voltage * voltage;
		this.totalEnergy += curEnergy;
		
		req.setStart(sim.getCurrentTime() + (long)time);
        sim.addReq(req);
	}
	
	public double getTotalEnergyUsage(){
		return totalEnergy;
	}
	
	public double updateEnergyUsage(int idlecycles){
		double time = (double)idlecycles / freq;
		double voltage = freqToVolt.get(freq);
		double idleEnergy = time * voltage * voltage;
		totalEnergy += idleEnergy;
		return totalEnergy;
	}
}

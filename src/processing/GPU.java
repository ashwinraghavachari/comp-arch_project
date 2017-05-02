package processing;
import java.util.HashMap;
import java.util.Map;

import requests.Request;
import simulator.Simulator;

public class GPU extends ProcessingUnit{
	private double totalEnergy;
	private Map<Double, Double> freqToVolt;
	Simulator sim;
	private long currentEndTime;
	private long currentDelayTime;
	
	public GPU(Simulator sim){
		// Maps freq (GHz) to voltage (V)
		// Specs taken from NVIDIA GTX980
		this.freqToVolt = new HashMap<Double, Double>();
		this.freqToVolt.put(.540, 0.987);
		this.freqToVolt.put(.405, 0.850);
		this.freqToVolt.put(.135, 0.850);
		
		this.sim = sim;
	}
	
	@Override
	public void processRequest (Request req) {
	    if(sim.getCurrentTime() < currentEndTime)
	    {
	        req.setStart(currentDelayTime++);
	        sim.addReq(req);
	        return;
	    }

		long gpucycles = req.getGPUCycles();
		double time = (double)gpucycles / freq;
		double voltage = freqToVolt.get(freq);
		double curEnergy = time * voltage * voltage;
		this.totalEnergy += curEnergy;
		
		long scheduledTime = sim.getCurrentTime() + (long)time;
		currentDelayTime = currentEndTime = scheduledTime;
		
		req.setDestination(outboundPU);
		req.setStart(scheduledTime);
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

    @Override
    public long getDelayTime() {
        return currentDelayTime;
    }

    @Override
    public void printSummary() {
        System.out.println("***GPU***");
        System.out.println("total energy: " + totalEnergy);
    }
}

package processing;
import java.util.HashMap;
import java.util.Map;

import requests.Request;
import simulator.Simulator;

public class GPU extends ProcessingUnit{
	private double totalEnergy;
	private double freq;
	private Map<Double, Double> freqToVolt;
	Simulator sim;
	
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
		long gpucycles = req.getGPUCycles();
		double time = (double)gpucycles / freq;
		double voltage = freqToVolt.get(freq);
		double curEnergy = time * voltage * voltage;
		this.totalEnergy += curEnergy;
		
		req.setDestination(outboundPU);
	}
	
	public void setFreq(double freq){
		this.freq = freq;
	}
	
	public double getTotalEnergyUsage(){
		return totalEnergy;
	}
}

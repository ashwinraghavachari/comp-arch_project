import java.util.*;

public class CPU implements ProcessingUnit{
	private double totalEnergy;
	private double freq;
	private HashMap<Double, Double> freqToVolt;
	
	public CPU(){
		// Maps freq (GHz) to voltage (V)
		// Specs taken from the intel i7
		HashMap<Double, Double> freqToVolt = new HashMap<Double, Double>();
		freqToVolt.put(2.92, 1.176);
		freqToVolt.put(3.65, 1.240);
		freqToVolt.put(3.80, 1.336);
		freqToVolt.put(4.00, 1.496);
	}
	
	//@Override
	public void processRequest (Request req) {
		int preGPUcycles = req.getCyclecount();
		double time = (double)preGPUcycles / freq;
		double voltage = freqToVolt.get(freq);
		double curEnergy = time * voltage * voltage;
		totalEnergy += curEnergy;
	}
	
	public void setFreq(double freq){
		this.freq = freq;
	}
}

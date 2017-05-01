import java.util.*;

public class CPU extends ProcessingUnit{
	private int totalEnergy;
	private int freq;
	private HashMap<Double, Double> freqVoltMap;
	
	public CPU(){
		// Maps freq (GHz) to voltage (V)
		// Specs taken from the intel i7
		HashMap<Double, Double> freqVoltMap = new HashMap<Double, Double>();
		freqVoltMap.put(2.92, 1.176);
		freqVoltMap.put(3.65, 1.240);
		freqVoltMap.put(3.80, 1.336);
		freqVoltMap.put(4.00, 1.496);
	}
	
	@Override
	public void processRequest (Request req) {
		
	}
	
	void setFreq(int freq){
		
	}
}

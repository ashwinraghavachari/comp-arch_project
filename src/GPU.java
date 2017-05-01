import java.util.*;

public class GPU extends ProcessingUnit{
	private int totalEnergy;
	private double freq;
	private HashMap<Double, Double> freqVoltMap;
	
	public GPU(){
		// Maps freq (GHz) to voltage (V)
		// Specs taken from NVIDIA GTX980
		HashMap<Double, Double> freqVoltMap = new HashMap<Double, Double>();
		freqVoltMap.put(.540, 0.987);
		freqVoltMap.put(.405, 0.850);
		freqVoltMap.put(.135, 0.850);
	}
	
	@Override
	public void processRequest (Request req) {
		
	}
	
	void setFreq(int freq){
		
	}
}


public enum FREQ_STATE {

	CPU0(2.92),
	CPU1(3.65),
	CPU2(3.80),
	CPU3(4.00),
	GPU0(.540),
	GPU1(.405),
	GPU2(.135);
	
	private double freq;
	
	FREQ_STATE(double f){
		this.freq = f;
	}
	
	public double getFreq(){
		return this.freq;
	}
}

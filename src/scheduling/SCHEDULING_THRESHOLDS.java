package scheduling;

public enum SCHEDULING_THRESHOLDS {

    LOW(250),
    MID(500),
    HIGH(750);

	private int threshold;
	
	SCHEDULING_THRESHOLDS(int t){
		this.threshold = t;
	}
	
	public int getThreshold(){
		return this.threshold;
	}
}

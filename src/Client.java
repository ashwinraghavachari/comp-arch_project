import java.util.ArrayList;
import java.util.List;


public class Client {

	public static void main(String[] args)
	{
		//create requests and start simulator
		List<Request> reqs = new ArrayList<>();
		
		Simulator sim = new Simulator(reqs);
		
		sim.run();
	}
}

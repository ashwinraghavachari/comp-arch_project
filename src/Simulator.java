import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class Simulator implements Runnable{

	private Queue<Request> eventStream = new PriorityQueue<>();
	
	public Simulator()
	{
		ProcessingUnit cpu = new CPU(this);
		ProcessingUnit gpu = new GPU(this);
		ProcessingUnit nic = new NIC(this);
		Client client = new Client(this);
	}

	@Override
	public void run() {

		while(!eventStream.isEmpty())
		{
			Request req = eventStream.poll();
			req.processRequest();
		}
	}	
	
	public boolean addReqs(List<Request> reqs)
	{
		return eventStream.addAll(reqs);
	}
	
	public boolean addReq(Request req)
	{
		return eventStream.add(req);
	}	
	
	public static void main()
	{
		Simulator sim = new Simulator();
		sim.run();
	}
}

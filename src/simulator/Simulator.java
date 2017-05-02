package simulator;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import processing.CPU;
import processing.Client;
import processing.GPU;
import processing.NIC;
import processing.ProcessingUnit;
import requests.Request;


public class Simulator implements Runnable{

	private Queue<Request> eventStream = new PriorityQueue<>();
    private long simTime = 0;
    private Client client;
	
	public Simulator()
	{
		ProcessingUnit cpu = new CPU(this);
		ProcessingUnit gpu = new GPU(this);
		ProcessingUnit nic = new NIC(this);
		client = new Client(this, nic);
		
		client.addInbound(nic);
		client.addOutbound(nic);

		nic.addInbound(cpu);
		nic.addOutbound(client);

		cpu.addInbound(gpu);
		cpu.addOutbound(nic);

		gpu.addInbound(cpu);
		gpu.addOutbound(cpu);
	}

	@Override
	public void run() {

		while(!eventStream.isEmpty())
		{
			Request req = eventStream.poll();
			simTime = req.getStartTime();
			req.processRequest();
		}
	}	
	
	public Client getClient()
	{
	    return client;
	}
	
	public boolean addReqs(List<Request> reqs)
	{
		return eventStream.addAll(reqs);
	}
	
	public boolean addReq(Request req)
	{
		return eventStream.add(req);
	}	
	
    public long getCurrentTime() {
        return simTime;
    }

	public static void main(String[] args)
	{
		Simulator sim = new Simulator();
		sim.run();
		sim.getClient().printSummary();
	}

}

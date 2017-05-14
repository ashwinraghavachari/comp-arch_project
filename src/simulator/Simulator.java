package simulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import processing.CPU;
import processing.Client;
import processing.GPU;
import processing.NIC;
import processing.ProcessingUnit;
import requests.Request;
import scheduling.GPUIndependentCPUCoupled;
import scheduling.LockStep;
import scheduling.NaiveIndependent;
import scheduling.SchedulingPolicy;
import workloads.CPUHeavy;
import workloads.GPUHeavy;
import workloads.PUUniform;
import workloads.RandomWork;
import workloads.Workload;


public class Simulator implements Runnable{

	private Queue<Request> eventStream = new PriorityQueue<>();
    private long simTime = 0;

    private ProcessingUnit client;
    private ProcessingUnit cpu;
    private ProcessingUnit gpu;
    private ProcessingUnit nic;
    
    private SchedulingPolicy policy;
    private Workload workload;
    
	
	public Simulator(SchedulingPolicy policy, Workload workload)
	{
		cpu = new CPU(this);
		gpu = new GPU(this);
		nic = new NIC(this);
		client = new Client(this, nic, workload);
		
		client.addInbound(nic);
		client.addOutbound(nic);

		nic.addInbound(cpu);
		nic.addOutbound(client);

		cpu.addInbound(gpu);
		cpu.addOutbound(nic);

		gpu.addInbound(cpu);
		gpu.addOutbound(cpu);
		
		this.policy = policy;
		policy.setCPU(cpu);
		policy.setGPU(gpu);
		
		this.workload = workload;
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
	
	public boolean addWorkload(List<Request> workload)
	{
		return eventStream.addAll(workload);
	}
	
	public boolean addReq(Request req)
	{
		return eventStream.add(req);
	}	
	
    public long getCurrentTime() {
        return simTime;
    }
    
    public SchedulingPolicy getPolicy()
    {
        return policy;
    }

    private void printSummary() {
        System.out.println("*******SIM FINISH*******");
        System.out.println("Policy: " + policy.getClass().getSimpleName());
        System.out.println("Workload: " + workload.getClass().getSimpleName());
        System.out.println();

		client.printSummary();
		cpu.printSummary();
		gpu.printSummary();
		nic.printSummary();
    }
    
    private double cpuEnergy()
    {
        return cpu.getTotalEnergyUsage();
    }

    private double gpuEnergy()
    {
        return gpu.getTotalEnergyUsage();
    }

    /*
     * Compare dvfs with cpu and gpu in lock step vs independent
     */
	public static void main(String[] args)
	{
	    Map<SchedulingPolicy, Map<Workload, Simulator>> results = new HashMap<>();

	    List<SchedulingPolicy> policies = Arrays.asList(
	            new LockStep(), 
	            new NaiveIndependent(), 
	            new GPUIndependentCPUCoupled());

	    List<Workload> workloads = Arrays.asList(
	            new PUUniform(), 
	            new RandomWork(), 
	            new GPUHeavy(), 
	            new CPUHeavy());

	    for(SchedulingPolicy policy : policies)
	    {
	        for(Workload workload : workloads)
	        {
	            Simulator sim = new Simulator(policy, workload);
	            sim.run();

	            sim.printSummary();

	            results.putIfAbsent(policy, new HashMap<>());

	            results.get(policy).put(workload, sim);

	            double baselineCPU = results.get(policies.get(0)).get(workload).cpuEnergy();
	            double baselineGPU = results.get(policies.get(0)).get(workload).gpuEnergy();

	            double cpuImprovement = (baselineCPU - sim.cpuEnergy())
	                    /sim.cpuEnergy() * 100;

	            double gpuImprovement = (baselineGPU - sim.gpuEnergy()) 
	                    /sim.gpuEnergy() * 100;

	            System.out.println("CPU energy improvement: " + cpuImprovement + "%");
	            System.out.println("GPU energy improvement: " + gpuImprovement + "%");
	        }
	    }
	    
	}

}

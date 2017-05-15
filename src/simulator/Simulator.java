package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import processing.CPU;
import processing.Client;
import processing.GPU;
import processing.NIC;
import processing.ProcessingUnit;
import requests.Request;
import scheduling.ExitTimeResponsive;
import scheduling.GPUIndependentCPUCoupled;
import scheduling.LockStep;
import scheduling.NaiveIndependent;
import scheduling.SchedulingPolicy;
import workloads.CPUHeavy;
import workloads.GPUHeavy;
import workloads.PUUniform;
import workloads.RandomWork;
import workloads.Workload;

import com.opencsv.CSVWriter;


public class Simulator implements Runnable{

	private Queue<Request> eventStream = new PriorityQueue<>();
    private long simTime = 0;

    private ProcessingUnit client;
    private ProcessingUnit cpu;
    private ProcessingUnit gpu;
    private ProcessingUnit nic;
    
    private SchedulingPolicy policy;
    private Workload workload;
    
    private static final ExecutorService exec = Executors.newCachedThreadPool();
	
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

    private boolean printSummary() {
        System.out.println("*******SIM FINISH*******");
        System.out.println("Policy: " + policy.getClass().getSimpleName());
        System.out.println("Workload: " + workload.getClass().getSimpleName());
        System.out.println();

		client.printSummary();
		cpu.printSummary();
		gpu.printSummary();
		nic.printSummary();
		
		return true;
    }

    private void writeHeaders(CSVWriter writer) {
        List<String> headers = new ArrayList<>();

        headers.add("Scheduling Policy");
        headers.add("Workload");

		headers.addAll(client.getHeaders());
		headers.addAll(cpu.getHeaders());
		headers.addAll(gpu.getHeaders());
		headers.addAll(nic.getHeaders());

		headers.add("CPU Energy Improvement");
		headers.add("GPU Energy Improvement");
		
		writer.writeNext(headers.toArray(new String[0]));
    }

    private void writeSummary(CSVWriter writer, double cpuImprovement, double gpuImprovement) {
        List<String> output = new ArrayList<>();

        output.add(policy.getClass().getSimpleName());
        output.add(workload.getClass().getSimpleName());

		output.addAll(client.getSummary());
		output.addAll(cpu.getSummary());
		output.addAll(gpu.getSummary());
		output.addAll(nic.getSummary());
		
		output.add(Double.toString(cpuImprovement));
		output.add(Double.toString(gpuImprovement));
		
		
		writer.writeNext(output.toArray(new String[0]));
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
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
	    Map<SchedulingPolicy, Map<Workload, Simulator>> results = new HashMap<>();

	    List<SchedulingPolicy> policies = Arrays.asList(
	            new LockStep(), 
	            new NaiveIndependent(), 
	            new GPUIndependentCPUCoupled(), 
	            new ExitTimeResponsive());

	    List<Workload> workloads = Arrays.asList(
	            new PUUniform(), 
	            new RandomWork(), 
	            new GPUHeavy(), 
	            new CPUHeavy());

	    boolean headers = false;
	    try(CSVWriter writer = new CSVWriter(new FileWriter("summary.csv"), ',')){
	        // feed in your array (or convert your data to an array)

	        for(SchedulingPolicy policy : policies)
	        {
	            for(Workload workload : workloads)
	            {
	                final Simulator sim = new Simulator(policy, workload);
	                sim.run();
	                
	                List<Future<Object>> res = exec.invokeAll(Arrays.asList(() -> sim.printSummary()));

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


	                if(!headers)
	                {
	                    sim.writeHeaders(writer);
	                    headers = true;
	                }
	                sim.writeSummary(writer, cpuImprovement, gpuImprovement);

	                for(Future f : res)
	                    f.get();
	            }
	            writer.writeNext(new String[0]);
	        }


	    }
	}

}

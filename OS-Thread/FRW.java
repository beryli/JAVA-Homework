//complete ver
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
import java.security.SecureRandom;
import java.util.Scanner;

public class FRW {
	private final static SecureRandom generator = new SecureRandom();
	static Semaphore mutex_r;
	static Semaphore mutex_w;
	static int r, w;
	static int total_r = 0, total_w = 0;
	static boolean running = true;
	static int sleepTime;
	static int sum = 0;

	
	static class Reader implements Runnable
	{
		private final String taskName; // name of task
		private final Buffer sharedLocation; // reference to shared object
		
		public Reader (String taskName, Buffer sharedLocation)
		{
			this.sharedLocation = sharedLocation;
			this.taskName = "     [" + taskName + "]";
		}
		
		public synchronized void read() 
		{
			try 
			{
					System.out.printf(taskName + " reads data %2d\n", sharedLocation.blockingGet());
					total_r++;
			}
			catch (InterruptedException exception) 
			{
	            Thread.currentThread().interrupt();
			}
		}
		
		public void run()
		{
			while (running) 
			{
				try 
				{
						//boolean a = (sharedLocation.blockingGet() > 0);
						//System.out.print(a);
					//if (sharedLocation.blockingGet() > 0) {
						mutex_r.acquire();
						System.out.printf(taskName + " enters critical section.\n");
						read();
						sleepTime = (generator.nextInt(3) + 1);
						System.out.printf(taskName + " sleeps for " + sleepTime +  " seconds. . . . Wake Up!\n");
						Thread.sleep(sleepTime*1000);
						System.out.printf(taskName + " exits critical section.\n");
						mutex_r.release();
						Thread.sleep(4000);
					//}
				}
				catch (InterruptedException exception) 
				{
		            Thread.currentThread().interrupt();
				}
			}
			//System.out.printf(taskName + " terminated!\n");
		}
	}
	
	
	static class Writer implements Runnable
	{
		private final String taskName; // name of task
		private final Buffer sharedLocation; // reference to shared object
		
		// constructor
		public Writer (String taskName, Buffer sharedLocation)
		{
			this.sharedLocation = sharedLocation;
			this.taskName = "[" + taskName + "]";
		}
		
		public synchronized void write()
		{
			try 
			{
				System.out.print(taskName);
				int index = ((generator.nextInt(8)+1)*10);
				sum += index;
	            sharedLocation.blockingPut(sum);
	            total_w++;
			}
			catch (InterruptedException exception) 
			{
	            Thread.currentThread().interrupt();
			}
		}
		
		// method run contains the code that a thread will execute
		public void run() 
		{
			while (running) 
			{
				try 
				{
					mutex_w.acquire(1);
					mutex_r.acquire(r);
					System.out.printf(taskName + " enters critical section.\n");
					write();
					System.out.printf(taskName + " sleeps for " + sleepTime +  " seconds. . . . Wake Up!\n");
					sleepTime = (generator.nextInt(3) + 1);
					Thread.sleep(sleepTime*1000);
					mutex_w.release(1);
					System.out.printf(taskName + " exits critical section.\n");
					mutex_r.release(r);
					Thread.sleep(2000);
				}
				catch (InterruptedException exception) 
				{
		            Thread.currentThread().interrupt();
				}
			}
			//System.out.printf(taskName + " terminated!\n");
		} 
	}
	
	
	
	public static void main(String[] args) 
	{
		Scanner input = new Scanner(System.in);
		System.out.print("How many Readers?");
		r = input.nextInt();
		System.out.print("How many Writers?");
		w = input.nextInt();
		System.out.println("-------------------------");
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		Buffer sharedLocation = new Buffer();
		
		mutex_r = new Semaphore(r);
		mutex_w = new Semaphore(1);
		for (int i = 1; i <= w; i++) 
		{
			executorService.execute(new Writer("Writer" + i, sharedLocation));
		}
			
		for (int i = 1; i <= r; i++) 
		{
			executorService.execute(new Reader("Reader" + i, sharedLocation));
		}
		
		
		executorService.shutdown(); // terminate app when tasks complete
		
		try 
		{
			boolean tasksEnded = executorService.awaitTermination(120, TimeUnit.SECONDS);
			running = false;

			executorService.awaitTermination(20, TimeUnit.SECONDS);
			if (tasksEnded) {
				System.out.println("First Reader-Writer stop!");
			} else {
				System.out.println("-------------------------");
				System.out.println("First Reader-Writer stop");
				System.out.println("Total write counts: " + total_w);
				System.out.println("Total read counts: " + total_r);
			}
		}
		catch (InterruptedException exception) 
		{
			exception.printStackTrace();
		}
	}
}

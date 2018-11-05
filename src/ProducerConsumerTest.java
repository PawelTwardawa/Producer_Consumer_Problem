
package producerConsumerProblem;
import java.nio.BufferOverflowException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTextArea;

/* 
 *  Problem producenta i konsumenta
 *
 *  Autor: Pawe³ Twardawa
 *   Data: 12 grudzien 2017 r.
 */


abstract class  Worker extends Thread {
	
	// Metoda usypia w¹tek na podany czas w milisekundach
	public static void sleep(int millis){
		try {
			Thread.sleep(millis);
			} catch (InterruptedException e) { }
	}

	// Metoda usypia w¹tek na losowo dobrany czas z przedzia³u [min, max) milsekund
	public static void sleep(int min_millis, int max_milis){
		sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
	}
	
	// Unikalny identyfikator przedmiotu wyprodukowanego
	// przez producenta i zu¿ytego przez konsumenta
	// Ten identyfikator jest wspólny dla wszystkich producentów
	// i bêdzie zwiêkszany przy produkcji ka¿dego nowego przedmiotu
	static int itemID = 0;
	
	// Minimalny i maksymalny czas produkcji przedmiotu
	public int MIN_PRODUCER_TIME = 100;
	public int MAX_PRODUCER_TIME = 1000;
	
	// Minimalny i maksymalny czas konsumpcji (zu¿ycia) przedmiotu
	public int MIN_CONSUMER_TIME = 100;
	public int MAX_CONSUMER_TIME = 1000;
	
	
	String name;
	Buffer buffer;
	JTextArea textOut;
	
	//int item;
	
	boolean paused; 
	
	@Override
	public abstract void run();
	public abstract void pause();
	public abstract void play();

}


class Producer extends Worker {

	
	public Producer(String name , Buffer buffer){ 
		this.name = name;
		this.buffer = buffer;
	}
	
	public Producer(String name , Buffer buffer, JTextArea out, int minProducerTime, int maxProducerTime){ 
		this.name = name;
		this.buffer = buffer;
		this.textOut = out;
		this.MIN_PRODUCER_TIME = minProducerTime;
		this.MAX_PRODUCER_TIME = maxProducerTime;
		this.paused = false;
	}
	
	@Override 
	public void pause()
	{
		this.paused = true;
	}
	
	@Override 
	public void play()
	{
		this.paused = false;
	}
	
	@Override
	public void run(){ 
		int item;
		while(true){
			while(paused) 
			{
				sleep(1);
			}

				item = itemID++;
				this.textOut.append("Producent <" + name + ">   produkuje: " + item + "\n");

				sleep(MIN_PRODUCER_TIME, MAX_PRODUCER_TIME);
				

				buffer.put(this, item);
		}
	}
	
	
} // koniec klasy Producer


class Consumer extends Worker {
	
	public Consumer(String name , Buffer buffer){ 
		this.name = name;
		this.buffer = buffer;
	}
	
	public Consumer(String name , Buffer buffer, JTextArea out, int minConsumerTime, int maxConsumerTime){ 
		this.name = name;
		this.buffer = buffer;
		this.textOut = out;
		this.MIN_CONSUMER_TIME = minConsumerTime;
		this.MAX_CONSUMER_TIME = maxConsumerTime;
		this.paused = false;
	}

	@Override 
	public void pause()
	{
		this.paused = true;
	}
	
	@Override 
	public void play()
	{
		this.paused = false;
	}
	
	@Override
	public void run(){ 
		int item;
		while(true){
			while(paused) 
			{
				sleep(1);
			}

				item = buffer.get(this);
				
				// Konsument zu¿ywa popraany przedmiot.
				sleep(MIN_CONSUMER_TIME, MAX_CONSUMER_TIME);
				this.textOut.append("Konsument <" + name + ">       zu¿y³: " + item+ "\n");


			
		}
	}
	
} // koniec klasy Consumer


class Buffer {
	
	private int contents;

	private int available = 0;
	private JTextArea textOut;
	private int bufferSize; 

	Buffer()
	{
		
	}
	
	Buffer(JTextArea out, int bufferSize)
	{
		this.textOut = out;
		this.bufferSize = bufferSize;
	}
	
	public synchronized int get(Consumer consumer){
		
		this.textOut.append("Konsument <" + consumer.name + "> chce zabrac"+ contents + "\n");
	
		while(this.available == 0) {
			try { 
				this.textOut.append("Konsument <" + consumer.name + ">   bufor pusty - czekam"+ "\n");
	
				  wait();
				} catch (InterruptedException e) { }
		}
		int item = contents;

		this.available--;
		this.textOut.append("Konsument <" + consumer.name + ">      zabral: " + contents+ " z: "+ available + "\n");
		

		notifyAll();
		return item;
	}

	public synchronized void put(Producer producer, int item){
		
		this.textOut.append("Producent <" + producer.name + ">  chce oddac: " + item+ "\n");

		while(this.available == this.bufferSize ) {
			try { 
				this.textOut.append("Producent <" + producer.name + ">   bufor zajety - czekam"+ "\n");
				
				  wait();
				} catch (InterruptedException e) { }
		}
		contents = item;
	
		this.available++;
		this.textOut.append("Producent <" + producer.name + ">       oddal: " + item+ " do: " + available + "\n");
		
		notifyAll();
	}
	
} // koniec klasy Buffer




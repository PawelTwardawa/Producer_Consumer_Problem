package producerConsumerProblem;
/* 
 *  Problem producenta i konsumenta
 *
 *  Autor: Pawe³ Twardawa
 *   Data: 12 grudzien 2017 r.
 */
import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextArea;

public class WindowDialog {
	
	private Buffer buffer ;
	private ArrayList<Producer> producers = new ArrayList<Producer>();
	private ArrayList<Consumer> consumers = new ArrayList<Consumer>();
	
	WindowDialog(JTextArea out)
	{
		this.textOut = out;
		//buffer = new Buffer(out);
	}
	
	JTextArea textOut;
	

	public static void main(String[] args) 
	{
		new Window();
	}

	public void initialize (int countConsumers, int countProducer, int minConsumerTime, int minProducerTime, int maxConsumerTime, int maxProducerTime, int bufferSize)
	{
		this.buffer = new Buffer(this.textOut, bufferSize);
		
		for(int i =0; i < countProducer; i++)
		{
			producers.add(new Producer("P"+i, this.buffer, this.textOut, minProducerTime, maxProducerTime));
		}
		for(int i =0; i < countConsumers; i++)
		{
			consumers.add(new Consumer("C"+i, this.buffer, this.textOut, minConsumerTime, maxConsumerTime));
		}
	}
	
	public void start()
	{
		//this.initialize(countConsumers, countProducer);
		
		for(Producer p : this.producers)
		{
			p.start();
		}
		
		for(Consumer c : this.consumers)
		{
			c.start();
		}	
	}
	
	public void pause()
	{
		
		for(Producer p : this.producers)
		{
			p.pause();
		}
		
		for(Consumer c : this.consumers)
		{
			c.pause();
		}
	}
	
	public void play()
	{
		
		for(Producer p : this.producers)
		{
			p.play();
			
		}
		
		for(Consumer c : this.consumers)
		{
			c.play();
		}
	}

	
} // koniec klasy 

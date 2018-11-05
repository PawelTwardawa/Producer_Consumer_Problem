package producerConsumerProblem;
/* 
 *  Problem producenta i konsumenta
 *
 *  Autor: Pawe³ Twardawa
 *   Data: 12 grudzien 2017 r.
 */
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame implements ActionListener, ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int[] comboBoxItems = {1,2,3,4,5,6};
	private JPanel panel = new JPanel();
	
	private JTextArea textArea = new JTextArea();
	private JScrollPane areaScrollPane	= new JScrollPane(this.textArea);
	
	private JLabel label_bufferSize = new  JLabel("Pojemnosc bufora: ");
	private JLabel label_countProducer = new JLabel("Liczba producentow: ");
	private JLabel label_countCustomer = new JLabel("Liczba konsumentow: ");
	private JLabel label_minProductionTime = new JLabel("Min czas produkcji:" );
	private JLabel label_minConsumerTime = new JLabel("Min czas konsumpcji: ");
	private JLabel label_maxProcuctionTime = new JLabel("Max czas produkcji: ");
	private JLabel label_maxConsumerTime = new JLabel("Max czas konsumpcji: " );
	
	private JComboBox<Integer> comboBox_bufferSize = new JComboBox<Integer>();
	private JComboBox<Integer> comboBox_countProducer = new JComboBox<Integer>();
	private JComboBox<Integer> comboBox_countCustomer = new JComboBox<Integer>();
	
	private SpinnerNumberModel spinnerModel_minProductionTime = new SpinnerNumberModel(100, 100, 10000, 10);
	private SpinnerNumberModel spinnerModel_minConsumerTime = new SpinnerNumberModel(100, 100, 10000, 10);
	private SpinnerNumberModel spinnerModel_maxProductionTime = new SpinnerNumberModel(1000, 100, 10000, 10);
	private SpinnerNumberModel spinnerModel_maxConsumerTime = new SpinnerNumberModel(1000, 100, 10000, 10);
	
	private JSpinner spinner_minProductionTime = new JSpinner(spinnerModel_minProductionTime);
	private JSpinner spinner_minConsumerTime = new JSpinner(spinnerModel_minConsumerTime);
	private JSpinner spinner_maxProcuctionTime = new JSpinner(spinnerModel_maxProductionTime);
	private JSpinner spinner_maxConsumerTime = new JSpinner(spinnerModel_maxConsumerTime);
	
	
	private JButton button_start = new JButton("Start");
	private JButton button_stop = new JButton("Pauza");
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu[] menu = { new JMenu("Plik"), new JMenu("Pomoc") };
	private JMenuItem[] menuItem = {new JMenuItem("Start"), new JMenuItem("Stop"), new JMenuItem("O programie")};
	
	WindowDialog dialog = null;
	private boolean paused = false;
	
	
	Window()
	{
		setTitle("Pawel Twardawa");
		setResizable(false);
		setSize(650, 500);
		setLocationRelativeTo(null);
		
		this.textArea.setEditable(false);
		this.textArea.setColumns(40);
		this.textArea.setRows(15);
		
		this.areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.comboBox_bufferSize.addActionListener(this);
		this.comboBox_countCustomer.addActionListener(this);
		this.comboBox_countProducer.addActionListener(this);
		this.spinner_maxConsumerTime.addChangeListener(this);
		this.spinner_maxProcuctionTime.addChangeListener(this);
		this.spinner_minConsumerTime.addChangeListener(this);
		this.spinner_minProductionTime.addChangeListener(this);
		
		for(int i : comboBoxItems)
		{
			this.comboBox_bufferSize.addItem(i);
			this.comboBox_countCustomer.addItem(i);
			this.comboBox_countProducer.addItem(i);
		}
		
		this.button_start.addActionListener(this);
		this.button_stop.addActionListener(this);
		
		
		this.panel.add(label_bufferSize);
		this.panel.add(comboBox_bufferSize);
		this.panel.add(label_countCustomer);
		this.panel.add(comboBox_countCustomer);
		this.panel.add(label_countProducer);
		this.panel.add(comboBox_countProducer);
		
		this.panel.add(label_maxConsumerTime);
		this.panel.add(spinner_maxConsumerTime);
		this.panel.add(label_minConsumerTime);
		this.panel.add(spinner_minConsumerTime);
		this.panel.add(label_maxProcuctionTime);
		this.panel.add(spinner_maxProcuctionTime);
		this.panel.add(label_minProductionTime);
		this.panel.add(spinner_minProductionTime);
		
		//this.panel.add(textArea);
		this.panel.add(areaScrollPane);
		
		this.panel.add(button_start);
		this.panel.add(button_stop);
		
		for(JMenu m : this.menu)
		{
			this.menuBar.add(m);
			
		}
		
		for(JMenuItem i : this.menuItem)
		{
			i.addActionListener(this);
		}
		
		this.menu[0].add(this.menuItem[0]);
		this.menu[0].add(this.menuItem[1]);
		this.menu[1].add(this.menuItem[2]);
		
		this.setJMenuBar(this.menuBar);
		
		
		this.setContentPane(panel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == button_start || source == this.menuItem[0] )
		{
			if(!this.paused)
			{
				dialog = new WindowDialog(this.textArea);
				dialog.initialize(((Integer) this.comboBox_countCustomer.getSelectedItem()).intValue(), ((Integer)this.comboBox_countProducer.getSelectedItem()).intValue(), (Integer)this.spinner_minConsumerTime.getValue(), (Integer)this.spinner_minProductionTime.getValue(), (Integer)this.spinner_maxConsumerTime.getValue(), (Integer)this.spinner_maxProcuctionTime.getValue(), ((Integer)this.comboBox_bufferSize.getSelectedItem()).intValue());
				dialog.start();
				this.changeEditableItems(false);
			}
			else
			{
				dialog.play();
				this.paused = false;
			}
		}
		if(source == button_stop || source == this.menuItem[1] )
		{
			if(!this.paused && dialog != null)
			{
				dialog.pause();
				this.paused = true;
			}
		}
		if(source == this.menuItem[2])
		{
			JOptionPane.showMessageDialog(this, "Program symulujacy wspoldzialanie producentow i konsumentow \n Pawe³ Twardawa");
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		
		if(source == spinner_maxConsumerTime)
		{

		}
		if(source == spinner_maxProcuctionTime)
		{
			
		}
		if(source == spinner_minConsumerTime)
		{
			
		}
		if(source == spinner_minProductionTime)
		{
			
		}
	}
	
	private void changeEditableItems(boolean edit)
	{
		this.comboBox_bufferSize.setEnabled(edit);
		this.comboBox_countCustomer.setEnabled(edit);
		this.comboBox_countProducer.setEnabled(edit);
		this.spinner_maxConsumerTime.setEnabled(edit);
		this.spinner_maxProcuctionTime.setEnabled(edit);
		this.spinner_minConsumerTime.setEnabled(edit);
		this.spinner_minProductionTime.setEnabled(edit);
	}
}

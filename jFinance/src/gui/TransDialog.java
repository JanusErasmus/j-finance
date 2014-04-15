package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import content.Category;
import content.Transaction;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.DropMode;
import javax.swing.SwingConstants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;

public class TransDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean OKflag;
	private boolean subCatFlag;
	private ArrayList<Category> categories;
	private Transaction transact;

	private final JPanel contentPanel = new JPanel();
	private JTextField amountTxt;
	private final JTextField descTxt = new JTextField();
	private JComboBox catCombo;
	private JTextField dateTxt;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox descCombo;
	private JComboBox fromCombo;
	private JLabel descLabel;
	 
	
	public static Transaction show(ArrayList<Category> cat, Transaction trans)
	{
		Transaction filledTrans = null;		
		
		TransDialog transDlg = new TransDialog(cat, trans);
		transDlg.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		transDlg.setVisible(true);
		
		if(transDlg.OKPressed())
		{
			filledTrans = transDlg.getVal();
		}
		
		return filledTrans;
	}
	
	public TransDialog(ArrayList<Category> cat, Transaction trans)
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(TransDialog.class.getResource("/ico/jFin.png")));
		OKflag = false;		
		subCatFlag = false;
		categories = cat;
		transact = trans;
				
		setTitle("Add Transaction");
		setBounds(100, 100, 596, 120);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		amountTxt = new JTextField("R 0.00");
		amountTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		amountTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				amountTxt.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				amountTxt.setText(parseUtils.amount(amountTxt.getText()));
			}
		});
		amountTxt.setBounds(498, 24, 76, 20);
		contentPanel.add(amountTxt);
		amountTxt.setColumns(10);
		if(transact != null)
		{
			amountTxt.setText(parseUtils.amount(transact.amount));			
		}
		
		descCombo = new JComboBox();
		descCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)					
					{
						setSubCatDefault();
					}
			}
		});
		descCombo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setSubCatDefault();
			}
		});
		descCombo.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				
				if(arg0.getOldValue() == null || arg0.getNewValue() == null)
					return;
								
				if(arg0.getOldValue().toString() == "PRESSED" && arg0.getNewValue().toString() == "NORMAL")
				{
					setSubCatDefault();
				}
			}
		});
		descCombo.setBounds(189, 24, 229, 20);			
		contentPanel.add(descCombo);
		

		descLabel = new JLabel("transfer from");
		descLabel.setBounds(247, 27, 124, 14);
		descLabel.setVisible(false);
		contentPanel.add(descLabel);
		
		catCombo = new JComboBox();		
		catCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED)
				{
					//System.out.println(arg0.getStateChange());	
					if(catCombo.getItemCount() > 1)
						descSelectVisable(catCombo.getSelectedIndex());
				}
			}
		});
		catCombo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(catCombo.getItemCount() > 1)
					descSelectVisable(catCombo.getSelectedIndex());				
			}
		});
		catCombo.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
										
				
				if(arg0.getOldValue() == null || arg0.getNewValue() == null)
					return;
				
				if(arg0.getOldValue().toString() == "PRESSED" && arg0.getNewValue().toString() == "NORMAL")
				{
					if(catCombo.getItemCount() > 1)
						descSelectVisable(catCombo.getSelectedIndex());
				}
			}
		});
		catCombo.setBounds(97, 24, 82, 20);
		contentPanel.add(catCombo);
		
		
		for (int i = 0; i < cat.size(); i++)
		{
			catCombo.addItem(cat.get(i).name);	
		}
		catCombo.addItem("Transfer");

		if(transact != null)
		{			
			if(	transact.category.equalsIgnoreCase("Transfer"))
			{
				catCombo.setSelectedIndex(catCombo.getItemCount() - 1);
			}
			else
			{
				ArrayList<String> lst = new ArrayList<String>();
				for (int i = 0; i < categories.size(); i++)
				{
					lst.add(categories.get(i).name);
				}
				int index = lst.indexOf(transact.category);
				if(index >= 0  && index < catCombo.getItemCount() )
					catCombo.setSelectedIndex( index );
			}			
		}		
				
		dateTxt = new JTextField();	
		dateTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {				
				dateTxt.setText(parseDate(dateTxt.getText()));
			}
			@Override
			public void focusGained(FocusEvent e) {
				dateTxt.selectAll();
			}
		});
		dateTxt.setBounds(10, 24, 77, 20);		
		contentPanel.add(dateTxt);
		dateTxt.setColumns(10);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(10, 11, 77, 14);
		contentPanel.add(lblDate);
					
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(189, 11, 135, 14);
		contentPanel.add(lblDescription);
		
		JLabel lblCatagory = new JLabel("Catagory");
		lblCatagory.setBounds(97, 11, 46, 14);
		contentPanel.add(lblCatagory);
		
		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setBounds(498, 11, 46, 14);
		contentPanel.add(lblAmount);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					amountTxt.setText(parseUtils.amount(amountTxt.getText()));
					//System.out.println("OK press");
					OKflag = true;
					setVisible(false);
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		//buttonPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{okButton, cancelButton}));

		//set transaction start date to today
		Calendar cal = Calendar.getInstance();
		java.util.Date now = cal.getTime();			
		DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");	
		if(transact != null)
		{
			dateTxt.setText(transact.date);
		}
		else
		{
			dateTxt.setText(fmt.format(now));
		}

		descTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				descTxt.selectAll();
			}
		});
		if(transact != null)
		{
			descTxt.setText(transact.description);
		}
		else
		{
			descTxt.setText("Description");
		}
		descTxt.setDropMode(DropMode.INSERT);
		descTxt.setBounds(189, 24, 229, 20);
		descTxt.setVisible(false);
		contentPanel.add(descTxt);
		descTxt.setColumns(10);
		
		fromCombo = new JComboBox();
		fromCombo.setBounds(428, 24, 60, 20);
		contentPanel.add(fromCombo);
		fromCombo.addItem("Bank");
		fromCombo.addItem("Wallet");
		if(transact != null)
		{
			ArrayList<String> lst = new ArrayList<String>();
			for (int i = 0; i < fromCombo.getItemCount(); i++)
			{
				lst.add((String)fromCombo.getItemAt(i));
			}
			int index = lst.indexOf(transact.from);
			fromCombo.setSelectedIndex(index);
		}
		
		JLabel lblFrom = new JLabel("From");
		lblFrom.setBounds(428, 11, 60, 14);
		contentPanel.add(lblFrom);
		
		contentPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblDescription, catCombo, dateTxt, amountTxt, lblDate, lblCatagory, lblAmount, descTxt}));

		if(transact != null)
		{
			descSelectVisable(catCombo.getSelectedIndex());
		}
		else
		{
			descSelectVisable(0);
		}
		
		//setFocus to description
		descTxt.requestFocus();
		dateTxt.requestFocus();

	}
	
	private void descSelectVisable(int index)
	{		 		
		if(categories.size() == 0 || categories.size() < index)
			return;
		
		//if a transfer
		if(index == categories.size())
		{
			descLabel.setVisible(true);
			descTxt.setVisible(false);
			descCombo.setVisible(false);			
			subCatFlag = false;
			if(transact != null)
				amountTxt.setText(parseUtils.amount(transact.amount));
			else	
			amountTxt.setText("R 0.00");	
		}
		else
		{
			descLabel.setVisible(false);
			
			int cnt = categories.get(index).subCatagories.size();
			if (cnt > 0)
			{					
				descTxt.setVisible(false);

				descCombo.removeAllItems();
				for (int i = 0; i < cnt; i++)
				{
					descCombo.addItem(categories.get(index).subCatagories.get(i).name);	
				}					
				descCombo.setVisible(true);
				descCombo.requestFocus();
				subCatFlag = true;	
				if(transact != null)
				{					
					ArrayList<String> lst = new ArrayList<String>();
					for (int i = 0; i < descCombo.getItemCount(); i++)
					{
						lst.add((String)descCombo.getItemAt(i));						
					}
					int idx = lst.indexOf(transact.description);
					descCombo.setSelectedIndex(idx);
				}
				setSubCatDefault();
			}
			else
			{
				setCatDefault();
				
				descTxt.setVisible(true);
				descCombo.setVisible(false);
				subCatFlag = false;
			}
		}		
	}
	
	/** Set default amount to the available in the selected category
	 * 
	 */
	private void setCatDefault()
	{	
		if(transact != null)
			amountTxt.setText(parseUtils.amount(transact.amount));
		else
		{
			int catIndex = catCombo.getSelectedIndex();
			if( catIndex >= 0 )
			{
			String defAmnt = parseUtils.amount(categories.get(catIndex).amount);
			amountTxt.setText(defAmnt);
			}
		}
		
	}
	
	/** Set default of amount to the available in the selected subCatagory	
	 * 
	 */
	private void setSubCatDefault()
	{			
		int descIndex = descCombo.getSelectedIndex();
		int catIndex = catCombo.getSelectedIndex();
		if(descIndex >= 0 && catIndex >= 0 )
		{
			String defAmnt = parseUtils.amount(categories.get(catIndex).subCatagories.get(descIndex).amount);
			amountTxt.setText(defAmnt);			
		}		
	}
		
	private String parseDate(String date)
	{
		String newDate = null;
		DateFormat fmt = null;				
		java.util.Date now = null;
		
		fmt = DateFormat.getDateInstance();
		
		try
		{
			now = fmt.parse(date);
		}
		catch (ParseException e2)
		{

			//System.out.println("Parse err retry");
			fmt = new SimpleDateFormat("dd-MM-yyyy");
			try
			{
				now = fmt.parse(date);
			}
			catch (ParseException e)
			{
				//System.out.println("Parse err retry");
				fmt = new SimpleDateFormat("dd/MM/yyyy");

				try
				{
					now = fmt.parse(date);
				}
				catch (ParseException e1)
				{
					//System.out.println("Parse err");
				}
			}
		}
				

		fmt = new SimpleDateFormat("dd-MM-yyyy");
		if (now != null)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			int year = cal.get(Calendar.YEAR);
			if(year < 2000)
			{
				cal.set(Calendar.YEAR, 2000 + year);
				now = cal.getTime();
			}
			
			newDate = fmt.format(now);
		}
		else
		{
			newDate = fmt.format(Calendar.getInstance().getTime());
		}
		
		return newDate;
	}
	
	private boolean OKPressed()
	{
		return OKflag;
	}
	
	private Transaction getVal()
	{		
		Transaction trans = new Transaction();
		trans.date = dateTxt.getText();
		if(subCatFlag)
		{
			trans.description = (String)descCombo.getSelectedItem();
		}
		else
		{
			trans.description = descTxt.getText();
		}
		
		trans.from = (String)fromCombo.getSelectedItem();
		
		//if the last item is selected its a transfer
		if(catCombo.getSelectedIndex() == categories.size())
		{
			if(trans.from.equalsIgnoreCase("Wallet"))
			{
				trans.description = "from Wallet ...";
			}
			
			if(trans.from.equalsIgnoreCase("Bank"))
			{
				trans.description = "from Bank ...";
			}
		}
		
		trans.category = (String)catCombo.getSelectedItem();
		trans.amount = parseUtils.getAmount(amountTxt.getText());
		
				
		return trans;
	}
}

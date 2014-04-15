package gui;

import java.awt.AWTException;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Robot;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

import content.Category;
import content.Content;
import content.Transaction;

import fileIO.XMLjFin;
import fileIO.openObj;
import fileIO.saveObj;
import fileIO.NoFileName;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;

public class mainApp
{
	
	private static final String VERSION = "0.2.0";

	private JFrame frmJfinance;
	private JTable transTable;
	private JPanel incomePanel;
	private JPanel catagoryLabels;
	private JPanel summaryPanel;	
	private JLabel lblBankSum;
	private JLabel lblWalletSum;
	private JTextField bankTxt;
	private JLabel lblErrorMessageBank;
	private JLabel lblErrorMessageWallet;
	private JLabel lblErrorMessageIncome;
	private JLabel lblTotalVal;
	private JTextField walletTxt;
	private JTextField prevBankTxt;
	private JTextField prevWalletTxt;
	private JTextField currBankTxt;
	private JTextField currWalletTxt;
	private JPanel catagoryPanel;
	
	//Budget variables
	private Content budget;
	private Category summary;
	private static String currFileName;
	private static boolean fileChange;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{	
		{
			String input = null;
			currFileName = null;
			fileChange = false;
			
			try
			{
				input = args[0];
			}
			catch (ArrayIndexOutOfBoundsException ex)
			{				
			}
			if(input != null)
			{
				currFileName = input;
				System.out.println("File name is " + currFileName);
			}
		}
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1)
		{
			e1.printStackTrace();
		} catch (InstantiationException e1)
		{
			e1.printStackTrace();
		} catch (IllegalAccessException e1)
		{
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1)
		{
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					mainApp window = new mainApp();
					window.frmJfinance.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mainApp()
	{
		budget = new Content();
				
		initialize();		
	}
			

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmJfinance = new JFrame();
		frmJfinance.addWindowListener(new WindowAdapter() 
		{
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				if(fileChange)
				{
					int opt = JOptionPane .showConfirmDialog(frmJfinance, "Budget has been changed.\nSave before close?", "jFinance", JOptionPane.YES_NO_OPTION);
					if(opt == JOptionPane.OK_OPTION)
					{
						doSave();
					}
				}
			}
		});
		frmJfinance.setIconImage(Toolkit.getDefaultToolkit().getImage(mainApp.class.getResource("/ico/jFin.png")));
		frmJfinance.setTitle("jFinance");
		frmJfinance.setBounds(100, 100, 828, 561);
		frmJfinance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmJfinance.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSaveAs();
			}
		});
		
		JMenuItem mntmSaveXML = new JMenuItem("Save XML");
		mntmSaveXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				doSaveXML();
			}
		});
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doOpen();
				doOpenXML();
			}			
		});
		
		JMenuItem mntmNew = new JMenuItem("New..");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				doNew();
			}
		});
		mnFile.add(mntmNew);
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		
		JMenuItem mntmNextMonth = new JMenuItem("Next month...");
		mntmNextMonth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mntmNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateNextMonth();
			}
		});
		mnFile.add(mntmNextMonth);
		mnFile.add(mntmSave);
		mnFile.add(mntmSaveAs);
		mnFile.add(mntmSaveXML);

		JMenuItem mntmExit = new JMenuItem("exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("Action pressed you dumb ass");
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showConfirmDialog(frmJfinance, "jFinance version " + VERSION, "jFinance", JOptionPane.OK_OPTION);
			}
		});
		mnHelp.add(mntmAbout);
		frmJfinance.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(263, 32, 547, 459);
		frmJfinance.getContentPane().add(tabbedPane);

		JPanel transPanel = new JPanel();
		tabbedPane.addTab("Transactions", null, transPanel, null);
		transPanel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 522, 363);
		transPanel.add(scrollPane_1);
		
		transTable = new JTable();
		transTable.setFont(new Font("Arial", Font.PLAIN, 13));
		transTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (arg0.getClickCount() == 2)
				{
					//System.out.println("Double");
					editEntry(transTable.getSelectedRow());
				}
				
			}
		});
		
		transTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Date", "Catagory", "Description", "Amount"
			}
		) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane_1.setViewportView(transTable);
		
		DefaultTableCellRenderer  dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.RIGHT);		
		transTable.getColumn("Amount").setCellRenderer(dtcr);
		
		JButton btnAddTrans = new JButton("Add");
		btnAddTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					Transaction trans = TransDialog.show(summary.subCatagories, null); 
					
					if(trans != null)
					{
						fileChanged();	
						budget.entries.add(trans);
						
						//System.out.println("Values entered " + trans.description);
						Object[] rowEntry = new Object[]{trans.date, trans.category, trans.description, parseUtils.amount(trans.amount)};
		            	((DefaultTableModel)transTable.getModel()).addRow(rowEntry);
		            	Rectangle rect = transTable.getCellRect(transTable.getRowCount()-1, 1, true);
		    			transTable.scrollRectToVisible(rect);
		    			
		            	fillSummary();		            	
		            	
					}
			}
		});
		btnAddTrans.setBounds(10, 385, 76, 23);
		transPanel.add(btnAddTrans);
		
		JButton btnRemoveTrans = new JButton("Remove");
		btnRemoveTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = transTable.getSelectedRow();
				
				if(index < 0)
				{
					JOptionPane.showMessageDialog(frmJfinance,
						    "Select a transaction first",
						    "No Entry selected",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				
				//System.out.println("Selected row" + index);
				Transaction trans = budget.entries.get(index);
				if(trans != null)
				{
				//System.out.println("Trans to be removed " + trans.description);				
					budget.entries.remove(index);				
				}

				fileChanged();
				fillTransTable();
				fillCatagory();
				fillSummary();
			}
		});
		btnRemoveTrans.setBounds(182, 385, 76, 23);
		transPanel.add(btnRemoveTrans);
		
		JButton btnEditTrans = new JButton("Edit");
		btnEditTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int index = transTable.getSelectedRow();
				
				if(index < 0)
				{
					JOptionPane.showMessageDialog(frmJfinance,
						    "Select a transaction first",
						    "No Entry selected",
						    JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					editEntry(index);
				}
			}
		});
		btnEditTrans.setBounds(96, 385, 76, 23);
		transPanel.add(btnEditTrans);

		incomePanel = new JPanel();
		tabbedPane.addTab("Income", null, incomePanel, null);
		incomePanel.setLayout(null);
		
		JLabel lblIncome = new JLabel("Bank:");
		lblIncome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIncome.setBounds(83, 11, 46, 14);
		incomePanel.add(lblIncome);
		
		bankTxt = new JTextField();
		bankTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10)
				{
					try
					{
						Robot bot = new Robot();
						bot.keyPress(KeyEvent.VK_TAB);
					}
					catch (AWTException ex)
					{
					}
				}
			}
		});
		bankTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		bankTxt.setText("R 0.00");
		bankTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				fileChanged();
				JTextField f = ((JTextField)e.getSource());
				f.setText(parseUtils.amount(f.getText()));
				budget.income.bank = parseUtils.getAmount(f.getText());
								
				calcCatagorySync();
				fillCatagory();
				fillSummary();
			}
		});
		bankTxt.setBounds(225, 8, 102, 20);
		incomePanel.add(bankTxt);
		bankTxt.setColumns(10);
		
		catagoryPanel = new JPanel();
		catagoryPanel.setBorder(new TitledBorder(null, "Catagories", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		catagoryPanel.setBounds(10, 64, 329, 356);
		incomePanel.add(catagoryPanel);
		catagoryPanel.setLayout(null);
		
		JButton btnRemoveCatagory = new JButton("Remove");
		btnRemoveCatagory.setBounds(218, 322, 89, 23);
		catagoryPanel.add(btnRemoveCatagory);
		
		JButton btnAddCatagory = new JButton("Add");
		btnAddCatagory.setBounds(20, 322, 89, 23);
		catagoryPanel.add(btnAddCatagory);
		
		catagoryLabels = new JPanel();
		catagoryLabels.setBounds(20, 24, 290, 258);
		catagoryPanel.add(catagoryLabels);
		catagoryLabels.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("75px"),
				ColumnSpec.decode("12px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("75px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JButton btnEditCatagory = new JButton("Edit");
		btnEditCatagory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				int index = budget.categories.size();
				
				if(index <= 0)
				{
					JOptionPane.showMessageDialog(frmJfinance,
						    "Catagories has to be entered first",
						    "No Entry selected",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				CategoryDialog catDlg = new CategoryDialog(budget.categories, frmJfinance);
				catDlg.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
				catDlg.setVisible(true);	
				
				fileChanged();
				fillCatagory();
				fillSummary();
			}
		});
		btnEditCatagory.setBounds(119, 322, 89, 23);
		catagoryPanel.add(btnEditCatagory);
		
		lblErrorMessageIncome = new JLabel("Error message");
		lblErrorMessageIncome.setForeground(Color.RED);
		lblErrorMessageIncome.setBounds(20, 293, 287, 14);
		lblErrorMessageIncome.setVisible(false);
		catagoryPanel.add(lblErrorMessageIncome);
		
		JLabel lblWallet_1 = new JLabel("Wallet:");
		lblWallet_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWallet_1.setBounds(83, 39, 46, 14);
		incomePanel.add(lblWallet_1);
		
		walletTxt = new JTextField();
		walletTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10)
				{
					try
					{
						Robot bot = new Robot();
						bot.keyPress(KeyEvent.VK_TAB);
					}
					catch (AWTException ex)
					{
					}
				}
			}
		});
		walletTxt.setText("R 0.00");
		walletTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		walletTxt.setColumns(10);
		walletTxt.setBounds(225, 35, 102, 20);
		walletTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				fileChanged();
				JTextField f = ((JTextField)e.getSource());
				f.setText(parseUtils.amount(f.getText()));
				budget.income.wallet = parseUtils.getAmount(f.getText());
								
				calcCatagorySync();
				fillCatagory();
				fillSummary();
			}});
		
		incomePanel.add(walletTxt);
		
		prevBankTxt = new JTextField();
		prevBankTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		prevBankTxt.setEnabled(false);
		prevBankTxt.setColumns(10);
		prevBankTxt.setBounds(139, 8, 76, 20);
		incomePanel.add(prevBankTxt);
		
		prevWalletTxt = new JTextField();
		prevWalletTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		prevWalletTxt.setEnabled(false);
		prevWalletTxt.setColumns(10);
		prevWalletTxt.setBounds(139, 33, 76, 20);
		incomePanel.add(prevWalletTxt);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 32, 243, 332);
		frmJfinance.getContentPane().add(panel);
		panel.setLayout(null);
		
		summaryPanel = new JPanel();
		summaryPanel.setBounds(10, 23, 223, 249);
		panel.add(summaryPanel);
		summaryPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("12px"),
				ColumnSpec.decode("90px"),
				ColumnSpec.decode("12px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("80px"),
				ColumnSpec.decode("17px"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblErrorMessageBank = new JLabel("amount");
		lblErrorMessageBank.setForeground(Color.RED);
		lblErrorMessageBank.setBounds(10, 283, 134, 14);
		panel.add(lblErrorMessageBank);
		lblErrorMessageBank.setVerticalAlignment(SwingConstants.TOP);
		
		lblErrorMessageWallet = new JLabel("amount");
		lblErrorMessageWallet.setForeground(Color.RED);
		lblErrorMessageWallet.setBounds(10, 307, 134, 14);
		panel.add(lblErrorMessageWallet);
		lblErrorMessageWallet.setVisible(false);
		lblErrorMessageBank.setVisible(false);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Current", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 375, 243, 88);
		frmJfinance.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("Bank:");
		label.setBounds(10, 14, 46, 14);
		panel_1.add(label);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel label_1 = new JLabel("Wallet:");
		label_1.setBounds(10, 39, 46, 14);
		panel_1.add(label_1);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		currBankTxt = new JTextField();
		currBankTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10)
				{
					try
					{
						Robot bot = new Robot();
						bot.keyPress(KeyEvent.VK_TAB);
					}
					catch (AWTException ex)
					{
					}
				}
			}
		});
		currBankTxt.setText("R 0.00");
		currBankTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		currBankTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				currBankTxt.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				JTextField f = ((JTextField)e.getSource());
				f.setText(parseUtils.amount(f.getText()));
				budget.currIncome.bank = parseUtils.getAmount(f.getText());
				
				fileChanged();
				fillSummary();
			}
		});
		currBankTxt.setBounds(66, 11, 84, 20);
		panel_1.add(currBankTxt);
		currBankTxt.setColumns(10);
		
		currWalletTxt = new JTextField();
		currWalletTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10)
				{
					try
					{
						Robot bot = new Robot();
						bot.keyPress(KeyEvent.VK_TAB);
					}
					catch (AWTException e)
					{
					}
				}
			}
		});
		currWalletTxt.setText("R 0.00");
		currWalletTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		currWalletTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				currWalletTxt.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				JTextField f = ((JTextField)e.getSource());
				f.setText(parseUtils.amount(f.getText()));
				budget.currIncome.wallet = parseUtils.getAmount(f.getText());				
				fileChanged();
				fillSummary();
			}
		});
		currWalletTxt.setBounds(66, 36, 84, 20);
		panel_1.add(currWalletTxt);
		currWalletTxt.setColumns(10);
		
		lblBankSum = new JLabel("R 0.00");
		lblBankSum.setBounds(160, 14, 73, 14);
		panel_1.add(lblBankSum);
		lblBankSum.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblWalletSum = new JLabel("R 0.00");
		lblWalletSum.setBounds(160, 39, 73, 14);
		panel_1.add(lblWalletSum);
		lblWalletSum.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setBounds(10, 63, 46, 14);
		panel_1.add(lblTotal);
		
		lblTotalVal = new JLabel("R 0.00");
		lblTotalVal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalVal.setBounds(149, 63, 85, 14);
		panel_1.add(lblTotalVal);
		btnAddCatagory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				String s = (String) JOptionPane.showInputDialog(frmJfinance,
						"Catagory:",
						"New Catagory", JOptionPane.PLAIN_MESSAGE);

				// If a string was returned, say so.
				if ((s != null) && (s.length() > 0))
				{
					if(budget.categories.size() == 10)
					{
						JOptionPane.showMessageDialog(frmJfinance,
							    "To much catagories.",
							    "New Catagory error",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					fileChanged();
					Category newCat = new Category();
					newCat.name = s;
					
					budget.categories.add(newCat);
					fillCatagory();
					fillSummary();
				}
			}
		});
		btnRemoveCatagory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				int index = budget.categories.size();
				
				if(index <= 0)
				{
					JOptionPane.showMessageDialog(frmJfinance,
						    "There has to be catagories to remove",
						    "No Entry selected",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				ArrayList<String> catNames = new ArrayList<String>();
				for (int i = 0; i < budget.categories.size(); i++)
				{
					catNames.add(budget.categories.get(i).name);
				}
				
				Object[] possibilities = catNames.toArray();				
				String s = (String)JOptionPane.showInputDialog(
				                    frmJfinance,
				                    "Select catagory to remove:\n",				                    
				                    "Remove Catagory",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    "");
				
				if ((s != null) && (s.length() > 0))
				{				
					int idx = catNames.indexOf(s);
					
					Object[] options = { "Yes", "No" };
					int n = JOptionPane.showOptionDialog(frmJfinance,
							"Are you sure you want to delete "  + s + " ?",
							"Remove catagory",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
					
					if(n == 0)
					{			
						fileChanged();
						budget.categories.remove(idx);
					}
				}
				

				fillCatagory();
				fillSummary();
				fillTransTable();
			}			
			
			
		});		
		
		
		if(currFileName != null)
		{
			File openFile = new File(currFileName);
			openBudget(openFile);
		}
	}
	
	private void doNew()
	{
		if(fileChange)
		{
			int opt = JOptionPane.showConfirmDialog(frmJfinance, "Budget has been changed.\nSave before creating empty budget?", "jFinance", JOptionPane.YES_NO_CANCEL_OPTION);
			if(opt == JOptionPane.OK_OPTION)
			{
				doSave();
			}
			else if(opt == JOptionPane.CANCEL_OPTION)
				return;
		}
		
		currFileName = null;
		
		budget = new Content();
		fillTransTable();
    	fillCatagory();
    	calcCatagorySync();
    	fillSummary();
    	    	    	
    	frmJfinance.setTitle("jFinance");
    	fileChange = false;
	}
	
	private void doSaveAs()
	{   
		System.out.println("argType");
        JFileChooser fc;
        if(currFileName == null)
        	fc = new JFileChooser();
        else
        	fc = new JFileChooser(new File(currFileName));
        
        FileNameExtensionFilter jFinFilter = new FileNameExtensionFilter("jFinance file", "jfin");
        fc.setFileFilter(jFinFilter);
        // Show open dialog; this method does not return until the dialog is closed
        if(fc.showSaveDialog(frmJfinance) == JFileChooser.CANCEL_OPTION)
        	return;
        
        String filename = fc.getSelectedFile().toString();        
        if(filename == null)
        	return;
        
        File selFile = new File(formatFileName(filename));      
        
        
        
        if(selFile != null)
        {
        	currFileName = selFile.toString();
        }

        saveObj file = null;
		try
		{
			file = new saveObj(selFile);
		}
		catch (NoFileName ex)
		{
			System.out.println("No file name entered");
		}
		
		if(file != null)
		{	
			file.write(budget);
			frmJfinance.setTitle(selFile.getName() + " - " + "jFinance");
			fileChange = false;
		}

	}
	
	private void doSaveXML()
	{
		XMLjFin xml = new XMLjFin();
		xml.newFile("bbjFin", budget);
	}
	
	private void doOpenXML()
	{
		XMLjFin xml = new XMLjFin();
		xml.readFile("try");
	}
	
	private void doSave()
	{
		if(currFileName != null)
		{
			File openFile = new File(currFileName);
			
			saveObj file = null;
			try
			{
				file = new saveObj(openFile);
			}
			catch (NoFileName ex)
			{
				System.out.println("No file name entered");
			}
			
			if(file != null)
			{	
				//System.out.println("Bank " + budget.income.bank);
				file.write(budget);
			}
			System.out.println("File saved " + currFileName);
			frmJfinance.setTitle(openFile.getName() + " - " + "jFinance");
			fileChange = false;
		}
		else
		{
			doSaveAs();
		}
	}
	
	
	private void doOpen()
	{	       
        
        JFileChooser fc;
        if(currFileName == null)
        	fc = new JFileChooser();
        else
        	fc = new JFileChooser(new File(currFileName));
        
        FileNameExtensionFilter jFinFilter = new FileNameExtensionFilter("jFinance file", "jfin");
        fc.setFileFilter(jFinFilter);
        
        // Show open dialog; this method does not return until the dialog is closed
        if(fc.showOpenDialog(frmJfinance) == JFileChooser.CANCEL_OPTION)
        	return;
        
        File selFile = fc.getSelectedFile();
        
        if(selFile != null)
        {
			openBudget(selFile);				
        }
        else
        {
        	frmJfinance.setTitle("jFinance");
        	fileChange = false;
        }
	}
	
	private void doCreateNextMonth()
	{
		//System.out.println("Summaries are:");
		
//		String s = (String) JOptionPane.showInputDialog(frmJfinance,
//				"Month:",
//				"Create next month", JOptionPane.PLAIN_MESSAGE);
		
		JFileChooser fc;
        if(currFileName == null)
        	fc = new JFileChooser();
        else
        	fc = new JFileChooser(new File(currFileName));
        FileNameExtensionFilter jFinFilter = new FileNameExtensionFilter("jFinance file", "jfin");        
        fc.setFileFilter(jFinFilter);
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(frmJfinance);
                
        String filename = fc.getSelectedFile().toString();
        File selFile = new File(formatFileName(filename));

		// If a string was returned, say so.
		//if ((s != null) && (s.length() > 0))
        if(selFile != null)
		{
			Content nextMonth = new Content();

			int cnt = budget.categories.size();
			for (int i = 0; i < cnt; i++)
			{
				Category cat = (Category)budget.categories.get(i);
				//System.out.println("Catagory: " + cat.name);
				
				Category nCat = calcSum(cat);	
				//System.out.println("now " + nCat.name +": " + nCat.prevAmount);		
				
				nextMonth.categories.add(nCat);
			}
			
			fillSummary();
			
			nextMonth.income = budget.income;
			nextMonth.prevIncome.bank = parseUtils.getAmount(lblBankSum.getText());
			nextMonth.prevIncome.wallet = parseUtils.getAmount(lblWalletSum.getText());
					
			
			//System.out.println("File name " + s);			
			//File openFile = new File(s + ".jfin");
			
			saveObj file = null;
			try
			{
				file = new saveObj(selFile);
			}
			catch (NoFileName ex)
			{
				System.out.println("No file name entered");
			}
			
			if(file != null)
			{	
				file.write(nextMonth);
			}
			System.out.println("File saved " + selFile.toString());
			
			openBudget(selFile);	
		}
		
	}
	
	private String formatFileName(String name)
	{
		String nName = name;
		System.out.println("File is " + name);
		
		int index = name.lastIndexOf(".");
		if(index > 0)
		{
			//System.out.println("Is correct extention added "  + name.substring(index));
			if(name.substring(index).compareToIgnoreCase(".jfin") != 0)
			{				
				nName += ".jfin";
			}
		}
		else
		{
			nName += ".jfin";
		}
		
		
		
		return nName;
	}
	
	private Category calcSum(Category cat)
	{
		Category sumCat = new Category();
		sumCat.name = cat.name;
		sumCat.amount = cat.amount;
		
		double sum = 0;
		int cnt = cat.subCatagories.size();		
		if(cnt > 0)
		{

			sum = cat.prevAmount;
			for (int i = 0; i < cat.subCatagories.size(); i++)
			{
				double subSum = 0;
				Category sub = cat.subCatagories.get(i);
				//System.out.println("\tfor " + sub.name);
				
				subSum = sub.amount + sub.prevAmount;
				//search the entries for these values				
				for (int j = 0; j < budget.entries.size(); j++)
				{
					if(budget.entries.get(j).category == cat.name && budget.entries.get(j).description == sub.name)
					{
						subSum -= budget.entries.get(j).amount;						
					}
				}
							
				
				sum += subSum ;
				
				//System.out.println("\t sum is " + subSum);	
				
				
				sumCat.subCatagories.add(new Category(sub.name, sub.amount, subSum));
				
				
			}
		}	
		else
		{
			//System.out.println("\t for " + cat.name);
			
			sum = cat.amount + cat.prevAmount;
			for (int j = 0; j < budget.entries.size(); j++)
			{				
				if(budget.entries.get(j).category == cat.name)
				{
					sum -= budget.entries.get(j).amount;						
				}
			}
			//System.out.println("Sum is " + sum);			
		}
		
		sumCat.prevAmount = sum;
		
		return sumCat;
	}
	
	private void openBudget(File fileName)
	{
		 openObj file = null;
	        try
			{
				file = new openObj(fileName);
			}
			catch (NoFileName ex)
			{
				System.out.println("No file name entered");
			}
	        
	        if(file != null)
	        {   
	        	budget = (Content)file.read();
	        	fillTransTable();
	        	fillCatagory();
	        	calcCatagorySync();
	        	fillSummary();
	        	
	        	
	        	currFileName = fileName.toString();	        	
	        	frmJfinance.setTitle(fileName.getName() + " - " + "jFinance");
	        }
	}
	
	private void editEntry(int index)
	{
		//System.out.println("Selected row" + index + " there is " + budget.Entries.size());
		Transaction trans = budget.entries.get(index);
		if (trans != null)
		{					

			trans = TransDialog.show(budget.categories, trans);
			if(trans != null)
			{
				fileChanged();			
				budget.entries.remove(index);
				budget.entries.add(index,trans);		            	
			}
						
			fillTransTable();
			fillCatagory();
			fillSummary();
		}
	}
	
	private void fillTransTable()
	{
		if(budget == null)
			return;

		int entCnt  = budget.entries.size();

		int r = transTable.getModel().getRowCount();
		while (r-- > 0)
		{
			((DefaultTableModel) transTable.getModel()).removeRow(r);
		}
		
		if(entCnt > 0)
		{
			for (int i = 0; i < entCnt ; i++)
			{
				Transaction entry = budget.entries.get(i);            	
				//System.out.println("Opened " + entry.catagory + " " + entry.description + " " + entry.amount);
				Object[] rowEntry = new Object[]{entry.date, entry.category, entry.description, parseUtils.amount(entry.amount)};
				((DefaultTableModel)transTable.getModel()).addRow(rowEntry);				
				
			}
			transTable.getColumnModel().getColumn(2).setMinWidth(200);
			Rectangle rect = transTable.getCellRect(entCnt-1, 1, true);
			transTable.scrollRectToVisible(rect);
			
		}	
		
		transTable.revalidate();
	}
	
	private void fillCatagory()
	{
		JLabel label = null;
		JTextField field = null; 
		String arg = null;
		
		
		catagoryLabels.removeAll();
		
		label = new JLabel("Name");	
		label.setHorizontalAlignment(SwingConstants.CENTER);
		arg = "1, 2, 1, 1";			
		catagoryLabels.add(label, arg);
		
		label = new JLabel("Previous");	
		label.setHorizontalAlignment(SwingConstants.CENTER);
		arg = "4, 2, 1, 1";
		catagoryLabels.add(label, arg);	
		
		
		label = new JLabel("Amount");			
		label.setHorizontalAlignment(SwingConstants.CENTER);
		arg = "6, 2, 1, 1";
		catagoryLabels.add(label, arg);	
		
		//parse and fill the gui
		for (int i = 0; i < budget.categories.size(); i++)
		{
			if(i > 9)
				break;
			
			label = new JLabel(budget.categories.get(i).name);	
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			arg = "1, " + (i*2 + 4) + ", 1, 1";			
			catagoryLabels.add(label, arg);
			
			//previous months values
			field = new JTextField(parseUtils.amount(budget.categories.get(i).prevAmount));
			field.setColumns(10);
			field.setName("catPrev " + Integer.toString(i));			
			field.setHorizontalAlignment(SwingConstants.RIGHT);
			field.setEnabled(false);			
			arg = "4, " + (i*2 + 4) + ", 1, 1";
			catagoryLabels.add(field, arg);	
			
			
			//this months values
			field = new JTextField(parseUtils.amount(budget.categories.get(i).amount));
			field.setColumns(10);
			field.setHorizontalAlignment(SwingConstants.RIGHT);
			field.setName("catagoryAmount " + Integer.toString(i));
			field.setAlignmentY(JTextField.RIGHT_ALIGNMENT);
			field.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == 10)
					{
						try
						{
							Robot bot = new Robot();
							bot.keyPress(KeyEvent.VK_TAB);
						}
						catch (AWTException ex)
						{
						}
					}
				}
			});
			field.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {					
					((JTextField)e.getSource()).selectAll();					
				}
				@Override
				public void focusLost(FocusEvent e) {
					JTextField f = ((JTextField)e.getSource());
					
					fileChanged();
					calcSubCatagory(f);
					calcCatagorySync();
					fillSummary();
					
				}
			});
			
			calcSubCatagory(field);			
			arg = "6, " + (i*2 + 4) + ", 1, 1";
			catagoryLabels.add(field, arg);		
			
			
			
		}
		
		
		
		prevBankTxt.setText(parseUtils.amount(budget.prevIncome.bank));
		bankTxt.setText(parseUtils.amount(budget.income.bank));
		prevWalletTxt.setText(parseUtils.amount(budget.prevIncome.wallet));
		walletTxt.setText(parseUtils.amount(budget.income.wallet));
				
		//incomePanel.revalidate();
		//catagoryPanel.revalidate();
		catagoryLabels.repaint();
		catagoryLabels.revalidate();
		
		
	}	
	
	private void calcCatagorySync()
	{		
		double subTotal = budget.sumOfCategory();		
		
		//System.out.println("fillCatagoty - Sub totals are " + subTotal);
		subTotal -= budget.income.bank + budget.prevIncome.bank;
		subTotal -= budget.income.wallet + budget.prevIncome.wallet;
		
		
		if(subTotal != 0)
		{
			lblErrorMessageIncome.setText("Income is unsync with " + parseUtils.amount(subTotal));
			lblErrorMessageIncome.setVisible(true);
		}
		else
		{
			lblErrorMessageIncome.setVisible(false);
		}
	}
	
	private void calcSummary()
	{
		summary = new Category();
		
		for (int i = 0; i < budget.categories.size(); i++)
		{
			//calc values here
			double sum =  -budget.sumOfTransaction(budget.categories.get(i));

			int subSize = budget.categories.get(i).subCatagories.size(); 
			if(subSize > 0)
			{	
			
				sum += budget.sumOfSubCategory(budget.categories.get(i));
				summary.subCatagories.add(i,new Category(budget.categories.get(i).name, sum, 0));

						for (int j = 0; j < budget.categories.get(i).subCatagories.size(); j++)
						{
							double subSum = budget.categories.get(i).subCatagories.get(j).amount + budget.categories.get(i).subCatagories.get(j).prevAmount;

							subSum -= budget.sumOfTransaction(budget.categories.get(i), budget.categories.get(i).subCatagories.get(j));					
										
							summary.subCatagories.get(i).subCatagories.add(new Category(budget.categories.get(i).subCatagories.get(j).name, subSum, 0));							
						}						
			}
			else
			{
				sum += (budget.categories.get(i).amount + budget.categories.get(i).prevAmount) ;
				summary.subCatagories.add(i,new Category(budget.categories.get(i).name, sum, 0));
			}
		}
		
//		System.out.println("Summary calc ");
//		System.out.println("There is " + summary.subCatagories.size() + " subs");
//		
//		for (int i = 0; i < summary.subCatagories.size(); i++)
//		{
//			System.out.println(summary.subCatagories.get(i).name + " " + summary.subCatagories.get(i).amount);
//			for (int j = 0; j < summary.subCatagories.get(i).subCatagories.size(); j++)
//			{
//				System.out.println("\t" + summary.subCatagories.get(i).subCatagories.get(j).name + " " + summary.subCatagories.get(i).subCatagories.get(j).amount);
//			}			
//		}
		
	}
	
	private void fillSummary()
	{
		JLabel label = null;
		String arg = null;
		JButton button = null;
		
		calcSummary();
		
		summaryPanel.removeAll();
		for (int i = 0; i < summary.subCatagories.size(); i++)
		{
			Category cat = summary.subCatagories.get(i);
			
			
			label = new JLabel(cat.name + ":");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			arg = "2, " + (i * 2 + 2) + ", 1, 1";
			summaryPanel.add(label, arg);

			int subSize = cat.subCatagories.size();
			if (subSize > 0)
			{
				button = new JButton(parseUtils.amount(cat.amount));
				button.setHorizontalAlignment(SwingConstants.RIGHT);
				if (cat.amount < 0)
					button.setForeground(Color.RED);

				button.setName("summaryBtn " + i);
				button.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						// System.out.println("Sumary action");
						String sumNames = ((JButton) arg0.getSource())
								.getName();
						int nameIdx = sumNames.indexOf(" ") + 1;
						int idx = Integer.parseInt(sumNames.substring(nameIdx));
						
						SummaryDialog.show(summary.subCatagories.get(idx));
					}
				});

				arg = "5, " + (i * 2 + 2) + ", 2, 1";
				summaryPanel.add(button, arg);
			}
			else
			{			
				label = new JLabel(parseUtils.amount(cat.amount));
				label.setHorizontalAlignment(SwingConstants.RIGHT);
				if (cat.amount < 0)
					label.setForeground(Color.RED);
				arg = "5, " + (i * 2 + 2) + ", 1, 1";
				summaryPanel.add(label, arg);
			}
		}
		
		currBankTxt.setText(parseUtils.amount(budget.currIncome.bank));
		currWalletTxt.setText(parseUtils.amount(budget.currIncome.wallet));
		
		
		Double fromTotal = ( budget.prevIncome.bank + budget.income.bank ) - budget.sumOfTransactions("Bank")  + budget.sumOfTransfers("Wallet");
		Double subTotal = fromTotal; 
		lblBankSum.setText(parseUtils.amount(fromTotal));		
		fromTotal -= budget.currIncome.bank;		
		if(fromTotal != 0)
		{
			lblErrorMessageBank.setText("Bank unsync " + parseUtils.amount(fromTotal) );
			lblErrorMessageBank.setVisible(true);
		}
		else
		{
			lblErrorMessageBank.setVisible(false);
		}
			

		fromTotal = (budget.prevIncome.wallet + budget.income.wallet) - budget.sumOfTransactions("Wallet") + + budget.sumOfTransfers("Bank");
		subTotal += fromTotal; 
		lblWalletSum.setText(parseUtils.amount(fromTotal));
		fromTotal -= budget.currIncome.wallet;		
		if(fromTotal != 0)
		{
			lblErrorMessageWallet.setText("Wallet unsync " + parseUtils.amount(fromTotal) );
			lblErrorMessageWallet.setVisible(true);
		}
		else
		{
			lblErrorMessageWallet.setVisible(false);
		}
		
		lblTotalVal.setText(parseUtils.amount(subTotal));
		

		summaryPanel.repaint();
		summaryPanel.revalidate();
	}
		
		
	private void calcSubCatagory(JTextField f)
	{	
		int nameIdx = f.getName().indexOf(" ") + 1;
		int idx = Integer.parseInt(f.getName().substring(nameIdx));
		
		budget.categories.get(idx).amount = parseUtils.getAmount(f.getText());
		
		//when category has sub categories, it should be disabled
		int subSize = budget.categories.get(idx).subCatagories.size(); 
		if(subSize > 0)
		{
			f.setEnabled(false);
			f.setText(parseUtils.amount(budget.sumOfSubCategory(budget.categories.get(idx))));			
		}
		else
		{
			f.setEnabled(true);
			f.setText(parseUtils.amount(f.getText()));			
		}
		
	}
	
	private void fileChanged()
	{
		if(fileChange)
			return;
		
		fileChange = true;
		
		if(currFileName == null)
			return;
		
		int idx = currFileName.lastIndexOf(File.separator);	
		if(idx > 0)
			frmJfinance.setTitle("*" + currFileName.substring( idx + 1 ) + " - " + "jFinance");		
		
	}
}

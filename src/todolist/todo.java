//Code By :- Yash Brid
package todolist;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.text.ParseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;

public class todo extends JFrame {

	private JPanel contentPane;
	private JTextField input_task;
	private JTable table;
	private JTextField input_date;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					todo frame = new todo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	Connection con=null;
	public todo() {
		con=(Connection) DB.dbconnect();
		setTitle("todo list");//224, 96, 126
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 504, 540);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(219, 211, 173));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("TO-DO LIST");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 29));
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(183, 11, 216, 53);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Task");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(81, 53, 45, 37);
		contentPane.add(lblNewLabel_1);
		
		input_task = new JTextField();
		input_task.setBackground(Color.WHITE);
		input_task.setBounds(139, 63, 260, 20);
		contentPane.add(input_task);
		input_task.setColumns(10);

		//ADD BUTTON
		JButton add = new JButton("ADD");
		add.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\todolist\\bin\\todolist\\add.png"));
		add.setBackground(new Color(224, 96, 126));
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String tsk= input_task.getText();
					String dte= input_date.getText();
					if(tsk.equals(""))
					{
						JOptionPane.showMessageDialog(null, "Please Enter the Task!!");	
					}
					else {
							if (dte.trim().equals("YYYY-MM-DD"))
							{
								JOptionPane.showMessageDialog(null, "Enter Date in the Format of YYY-MM-DD !");
							}
							else{
								SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-mm-dd");
								sdfrmt.setLenient(false);
								try{
									Date javaDate = sdfrmt.parse(dte); 					      
									PreparedStatement pst=(PreparedStatement) con.prepareStatement("insert into todo(task,date,checked) values(?,?,?)");
									pst.setString(1,tsk);
									pst.setString(2, dte);
									pst.setBoolean(3,false);
									pst.executeUpdate();
									input_task.setText("");
									input_date.setText("YYYY-MM-DD");
									} 
								catch (ParseException e)
								{
									JOptionPane.showMessageDialog(null, "Enter Date in the Format of YYYY-MM-DD !");
								}	
								}
						}
				 //TABLE UPDATE
					int a;
					PreparedStatement pst1=(PreparedStatement)con.prepareStatement("select * from todo");
					ResultSet rs= pst1.executeQuery();
					ResultSetMetaData rd= (ResultSetMetaData) rs.getMetaData();
					a= rd.getColumnCount();
					DefaultTableModel df=(DefaultTableModel) table.getModel();
					df.setRowCount(0);
					while(rs.next())
					{
						Vector v2= new Vector();
						for(int i=1;i<=a;i++)
						{
							v2.add(rs.getString("id"));
							v2.add(rs.getString("task"));
							v2.add(rs.getString("date"));
							v2.add(rs.getBoolean("checked"));
						}
						df.addRow(v2);
					} 
				}catch (SQLException e) 
				{
					e.printStackTrace();
				}	
			}
		});
		add.setBounds(188, 127, 89, 37);
		contentPane.add(add);
		
		//EDIT
		JButton btnNewButton_1 = new JButton("EDIT");
		btnNewButton_1.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\todolist\\bin\\todolist\\edit.png"));
		btnNewButton_1.setBackground(new Color(224, 96, 126));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel df=(DefaultTableModel)table.getModel();
				int s=table.getSelectedRow();
				int id=Integer.parseInt(df.getValueAt(s,0).toString());
				try {
					String tsk= input_task.getText();
					 String dte= input_date.getText();
					 int row = table.getSelectedRow();
					 int column = 3;
					 Boolean ck = (Boolean)table.getValueAt(row, column) ;
					 if (dte.trim().equals("YYYY-MM-DD"))
						{
						 JOptionPane.showMessageDialog(null, "Enter Date in the Format of YYYY-MM-DD !");
						}
						
						else
						{
						   
						    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-mm-dd");
						    sdfrmt.setLenient(false);
						    
						    try
						    {
						        Date javaDate = sdfrmt.parse(dte);						      
						        PreparedStatement pst=(PreparedStatement) con.prepareStatement("update todo set task=?,date=?,checked=? where id=?");
								pst.setString(1,tsk);
								pst.setString(2, dte);
								pst.setBoolean(3, ck);
								pst.setInt(4, id);
								pst.executeUpdate();
								JOptionPane.showMessageDialog(null, "List updated Successfully");
								 input_task.setText("");
								 input_date.setText("YYYY-MM-DD");
						    }
						    
						    catch (ParseException e)
						    {
						    	JOptionPane.showMessageDialog(null, "Enter Date in the Format of YYYY-MM-DD !");
						    }	
						}
					//Display
					 int a;
						PreparedStatement pst1=(PreparedStatement)con.prepareStatement("select * from todo");
						ResultSet rs= pst1.executeQuery();
						ResultSetMetaData rd= (ResultSetMetaData) rs.getMetaData();
						a= rd.getColumnCount();
						DefaultTableModel df1=(DefaultTableModel) table.getModel();
						df1.setRowCount(0);
						while(rs.next())
						{
							Vector v2= new Vector();
							for(int i=1;i<=a;i++)
							{
								v2.add(rs.getString("id"));
								v2.add(rs.getString("task"));
								v2.add(rs.getString("date"));
								v2.add(rs.getBoolean("checked"));
								
							}
							df.addRow(v2);
						}
						add.setEnabled(true);
				}catch(Exception e) {
					System.out.println(e);
				}
			}
		});
		btnNewButton_1.setBounds(81, 397, 95, 37);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("DELETE");
		btnNewButton_2.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\todolist\\bin\\todolist\\trash.png"));
		btnNewButton_2.setBackground(new Color(224, 96, 126));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel df=(DefaultTableModel)table.getModel();
				int s=table.getSelectedRow();
				int id=Integer.parseInt(df.getValueAt(s,0).toString());
				try {
					PreparedStatement pst=(PreparedStatement) con.prepareStatement("delete from todo where id=?");
					pst.setInt(1,id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Task Deleted Successfully");
					 input_task.setText("");
					 input_date.setText("YYYY-MM-DD");

					 int a;
						PreparedStatement pst1=(PreparedStatement)con.prepareStatement("select * from todo");
						ResultSet rs= pst1.executeQuery();
						ResultSetMetaData rd= (ResultSetMetaData) rs.getMetaData();
						a= rd.getColumnCount();
						DefaultTableModel df1=(DefaultTableModel) table.getModel();
						df1.setRowCount(0);
						while(rs.next())
						{
							Vector v2= new Vector();
							for(int i=1;i<=a;i++)
							{
								v2.add(rs.getString("id"));
								v2.add(rs.getString("task"));
								v2.add(rs.getString("date"));
								v2.add(rs.getBoolean("checked"));
							}
							df.addRow(v2);
						}
						add.setEnabled(true);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
		});
		btnNewButton_2.setBounds(188, 397, 106, 37);
		contentPane.add(btnNewButton_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(81, 173, 318, 213);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel df=(DefaultTableModel)table.getModel();
				int selected=table.getSelectedRow();
				int id=Integer.parseInt(df.getValueAt(selected,0).toString());
				input_task.setText(df.getValueAt(selected,1).toString());
				input_date.setText(df.getValueAt(selected,2).toString());
				add.setEnabled(false);
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Task", "Date", "done"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, Object.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		
		input_date = new JTextField();
		input_date.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				input_date.setText("");
			}
			
		});
		input_date.setText("YYYY-MM-DD");
		input_date.setBounds(139, 94, 260, 20);
		contentPane.add(input_date);
		input_date.setColumns(10);
		
		
		JLabel lblNewLabel_2 = new JLabel("Date");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(80, 96, 46, 18);
		contentPane.add(lblNewLabel_2);
		
		
		JButton btnNewButton = new JButton("EXIT");
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\todolist\\bin\\todolist\\correct.png"));
		btnNewButton.setBackground(new Color(224, 96, 126));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton.setBounds(304, 397, 95, 37);
		contentPane.add(btnNewButton);
		
		JLabel lblCodeByYashbrid = new JLabel("CODE BY:- YASHBRID03");
		lblCodeByYashbrid.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCodeByYashbrid.setForeground(Color.BLUE);
		lblCodeByYashbrid.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblCodeByYashbrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
			         
			        Desktop.getDesktop().browse(new URI("https://github.com//yashbrid03"));
			         
			    } catch (IOException | URISyntaxException e1) {
			        e1.printStackTrace();
			    }
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblCodeByYashbrid.setText("<html><a href='https://github.com//yashbrid03'>CODE BY:- YASHBRID03</a></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblCodeByYashbrid.setText("CODE BY:- YASHBRID03");
			}
		});
		lblCodeByYashbrid.setBounds(247, 465, 152, 25);
		contentPane.add(lblCodeByYashbrid);
	}
}

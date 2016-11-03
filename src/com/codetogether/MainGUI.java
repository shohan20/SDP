package com.codetogether;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import jsyntaxpane.syntaxkits.*;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtAddress;
        private JTextArea chat;
        private JTextArea agerchat;
        private JButton send;
	private JEditorPane textPane;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnLanguages;
	private JMenuBar menuBar;
	public JMenu mnConnectedUsers;
	
	private static final Action New = null;
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false;
	
	ActionMap m = null;
	Action Cut = null;
	Action Copy = null;
	Action Paste = null;
	
	/**
	 * Create the frame.
	 */
	public MainGUI(final Client client) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		textPane = new JEditorPane();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Document doc = textPane.getDocument();
				try {
					client.send(doc.getText(0, doc.getLength()));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			public void keyPressed(KeyEvent e) {
				changed = true;
				Save.setEnabled(true);
				SaveAs.setEnabled(true);
			}
		});
		
		m = textPane.getActionMap();
		Cut = m.get(DefaultEditorKit.cutAction);
		Copy = m.get(DefaultEditorKit.copyAction);
		Paste = m.get(DefaultEditorKit.pasteAction);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					client.running = false;
					client.listenThread.interrupt();
					client.sendThread.interrupt();
					if (client.outputStream != null && client.br != null && client.socket != null) {
						client.outputStream.println("/disconnect");
						client.outputStream.close();
						client.br.close();
						client.socket.close();
					}
					dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		setTitle("CodeTogether");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 544);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mnLanguages = new JMenu("Languages");
		menuBar.add(mnLanguages);
		
		mnFile.add(New);mnFile.add(Open);mnFile.add(Save);
		mnFile.add(Quit);mnFile.add(SaveAs);
		mnFile.addSeparator();
		
		mnLanguages.add("C");
		mnLanguages.add("C++");
		mnLanguages.add("JAVA");
		mnLanguages.add("Python");
		mnLanguages.addSeparator();
		
		mnLanguages.getItem(0).addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				int choice = JOptionPane
						.showConfirmDialog(
								null,
								"This will clear the current text mnEditor. Are you sure to proceed?",
								"Warning", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					CSyntaxKit.initKit();
					textPane.setContentType("text/c");
				} else {
					return;
				}
		    }
		});
		mnLanguages.getItem(1).addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				int choice = JOptionPane
						.showConfirmDialog(
								null,
								"This will clear the current text mnEditor. Are you sure to proceed?",
								"Warning", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					CSyntaxKit.initKit();
					textPane.setContentType("text/cpp");
				} else {
					return;
				}
		    }
		});
		mnLanguages.getItem(2).addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				int choice = JOptionPane
						.showConfirmDialog(
								null,
								"This will clear the current text mnEditor. Are you sure to proceed?",
								"Warning", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					CSyntaxKit.initKit();
					textPane.setContentType("text/java");
				} else {
					return;
				}
		    }
		});
		mnLanguages.getItem(3).addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				int choice = JOptionPane
						.showConfirmDialog(
								null,
								"This will clear the current text mnEditor. Are you sure to proceed?",
								"Warning", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					CSyntaxKit.initKit();
					textPane.setContentType("text/python");
				} else {
					return;
				}
		    }
		});
		
		
		for(int i=0; i<4; i++)
			mnFile.getItem(i).setIcon(null);
		
		mnEdit.add(Cut);mnEdit.add(Copy);mnEdit.add(Paste);

		mnEdit.getItem(0).setText("Cut out");
		mnEdit.getItem(1).setText("Copy");
		mnEdit.getItem(2).setText("Paste");
		
		JToolBar tool = new JToolBar();
		add(tool,BorderLayout.NORTH);
		tool.add(New);tool.add(Open);tool.add(Save);
		tool.addSeparator();
		
		JButton cut = tool.add(Cut), cop = tool.add(Copy),pas = tool.add(Paste);
		
		cut.setText(null); cut.setIcon(new ImageIcon("cut.gif"));
		cop.setText(null); cop.setIcon(new ImageIcon("copy.gif"));
		pas.setText(null); pas.setIcon(new ImageIcon("paste.gif"));
		
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		mnConnectedUsers = new JMenu("Connected Users");
		menuBar.add(mnConnectedUsers);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		

		JScrollPane scroll = new JScrollPane(textPane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblConnectedToHub = new JLabel("Connected to hub address:");
		lblConnectedToHub.setFont(new Font("Arial Nova Light", Font.BOLD, 16));
		panel.add(lblConnectedToHub, BorderLayout.NORTH);
		
		txtAddress = new JTextField();
		txtAddress.setEditable(false);
		txtAddress.setText(client.address);
		panel.add(txtAddress, BorderLayout.CENTER);
		txtAddress.setColumns(10);
		
		textPane.requestFocusInWindow();
		JPanel jp=new JPanel();
                jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
                JPanel agerchatp=new JPanel();
                agerchat=new JTextArea(15,30);
                 agerchat.setEditable(false);
                 agerchat.setLineWrap(true);
                agerchat.setWrapStyleWord(true);
                JScrollPane chatscrool = new JScrollPane(agerchat);
                chatscrool.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                agerchatp.add(chatscrool);
                jp.add(agerchatp);
                JPanel jp2=new JPanel();
                jp2.setLayout(new FlowLayout());
                chat = new JTextArea(5,20);
                chat.setLineWrap(true);
                chat.setWrapStyleWord(true);
                JScrollPane areaScrollPane = new JScrollPane(chat);
    /*  chat.addKeyListener(new KeyListener(){
        @Override
       public void keyPressed(KeyEvent e){
           if(e.getKeyCode()==KeyEvent.VK_SHIFT + KeyEvent.VK_ENTER){
               e.consume();
               chat.append(" \n");   
           }
           else if(e.getKeyCode() == KeyEvent.VK_ENTER){
            e.consume();
            send.doClick();
        }
}

        @Override
            public void keyTyped(KeyEvent e) {
        }

        @Override
            public void keyReleased(KeyEvent e) {
        }
        });*/
                areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                send = new JButton("Send");
                jp2.add(areaScrollPane);
                jp2.add(send);
                jp.add(jp2);
                contentPane.add(jp,BorderLayout.EAST);
                send.addActionListener((ActionEvent ae) -> {
                    String in=chat.getText();
                    client.send("chat"+client.username+": "+in);
                    String newmsg=agerchat.getText()+"\n"+client.username+": "+in;
                    agerchat.setText(newmsg);
                    chat.setText("");
                });
               // JPanel jp=new JPanel();
                
                //chatpanel.add(jp);
                //CSyntaxKit.initKit();
		//textPane.setContentType("text/c");
	}
	
	Action Open = new AbstractAction("Open", new ImageIcon("open.gif")) {
		private static final long serialVersionUID = 1L;

                @Override
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	
	Action Save = new AbstractAction("Save", new ImageIcon("save.gif")) {
		private static final long serialVersionUID = 1L;

                @Override
		public void actionPerformed(ActionEvent e) {
			if(!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	
	Action SaveAs = new AbstractAction("Save as...") {
		private static final long serialVersionUID = 1L;

                @Override
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	Action Quit = new AbstractAction("Quit") {
		private static final long serialVersionUID = 1L;

                @Override
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	
	
	private void saveFileAs() {
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}
	
	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}
	
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			textPane.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		}
		catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
		}
		
	}
	
	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			textPane.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		}
		catch(IOException e) {
		}
	}
	
	
	public void setSourceCode(String code){
		textPane.setText(code);
	}
        public void setchat(String msg){
            String newmsg=agerchat.getText()+"\n"+msg;
            agerchat.setText(newmsg);
        }

   

}
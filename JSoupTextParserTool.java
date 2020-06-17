import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class JSoupTextParserTool extends JPanel implements ActionListener, ItemListener{
	private JTextField websiteAddressLoader=null;
	private JTextField elementInputter=null;
	private JTextField classInputter=null;
	private JTextField attributeInputter=null;
	private JTextField attributeValueInputter=null;
	
	JTextArea textArea=null;
	JScrollPane scrollPane=null;
	
	JRadioButton classOption=null;
	JRadioButton attributeOption=null;
 
	JComboBox returnValuesList=null;
 
	JButton showOutputButton=null;
	
	String oldReturnType="Text";
	public JSoupTextParserTool(){

		
		setLayout(null);
		websiteAddressLoader=new JTextField(44);
		websiteAddressLoader.setText("https://www.google.com/search?q=ezyVet Ltd");
		websiteAddressLoader.setBounds(80,20,285,20);
		add(websiteAddressLoader);
		
		setLayout(null);
		elementInputter=new JTextField(10);
		elementInputter.setBounds(30,90,150,20);
		add(elementInputter);
		
		setLayout(null);
		classInputter=new JTextField(10);
		classInputter.setBounds(150,130,150,20);
		add(classInputter);
		
		
		setLayout(null);
		attributeInputter=new JTextField(10);
		attributeInputter.setEnabled(false);
		attributeInputter.setBounds(150,170,150,20);
		add(attributeInputter);
		
				setLayout(null);
		attributeValueInputter=new JTextField(10);
		attributeValueInputter.addActionListener(this);
		attributeValueInputter.setEnabled(false);
		attributeValueInputter.setBounds(150,210,150,20);
		add(attributeValueInputter);
		
		setLayout(null);
		classOption=new JRadioButton("Class =",true);
			classOption.addItemListener(this);

		classOption.setActionCommand("Class =");
		classOption.setBounds(20,130,100,20);
		add(classOption);
	attributeOption=new JRadioButton("Attribute Name =");
	attributeOption.addItemListener(this);
	classOption.setActionCommand("Attribute Name =");
			attributeOption.setBounds(20,170,130,20);
		add(attributeOption);
		
	    ButtonGroup group = new ButtonGroup();
    group.add(classOption);
    group.add(attributeOption);
	
	setLayout(null);
	String[] returnValuesString = {"Text", "Attribute value"};
	returnValuesList=new JComboBox(returnValuesString);
	returnValuesList.addActionListener(this);
	returnValuesList.setBounds(380,60,100,20);
	add(returnValuesList);
	
	setLayout(null);
	textArea = new JTextArea(5, 25);
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	textArea.setEditable(false);
	scrollPane = new JScrollPane(textArea); 
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setBounds(30,280,440,190);
	add(scrollPane);

	setLayout(null);
	showOutputButton=new JButton("Show Output");
	showOutputButton.addActionListener(this);
	showOutputButton.setBounds(350,210,120,30);
	add(showOutputButton);
	

	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==returnValuesList){
			String resultType = (String)(returnValuesList.getSelectedItem());
			if(!resultType.equals(oldReturnType)){
				if(resultType.equals("Text")){
					attributeOption.setEnabled(true);
					classOption.setEnabled(true);
					if(attributeOption.isSelected()){
						classInputter.setEnabled(false);
						attributeValueInputter.setEnabled(true);
					}else{
						attributeInputter.setEnabled(false);
					}
				}else{
					attributeOption.setEnabled(false);
					classOption.setEnabled(false);
					if(!classInputter.isEnabled()){
						classInputter.setEnabled(true);
					}else{
						attributeInputter.setEnabled(true);
					}
					attributeValueInputter.setEnabled(false);
				}
				oldReturnType=resultType;
			}
		}else if(e.getSource()==showOutputButton||e.getSource()==attributeValueInputter){
			if(oldReturnType.equals("Text")){
				if(classOption.isSelected()){
					String websiteAddressLoaderString=websiteAddressLoader.getText();
					String elementInputterString=elementInputter.getText();
					String classInputterString=classInputter.getText();
					showResult(websiteAddressLoaderString,elementInputterString, "",classInputterString,"class");
				}
				if(attributeOption.isSelected()){
					String websiteAddressLoaderString=websiteAddressLoader.getText();
					String elementInputterString=elementInputter.getText();
					String attributeNameInputterString=attributeInputter.getText();
					String attributeValueInputterString=attributeValueInputter.getText();
					showResult(websiteAddressLoaderString,elementInputterString, attributeNameInputterString,attributeValueInputterString,"attribute");
					
				}
			}else{
					String websiteAddressLoaderString=websiteAddressLoader.getText();
					String elementInputterString=elementInputter.getText();
					 String attributeNameInputterString=attributeInputter.getText();
					String classInputterString=classInputter.getText();
					 showResult(websiteAddressLoaderString,elementInputterString, attributeNameInputterString,classInputterString,"class");
			}
		}
		

	
	}
	
	public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
		if(e.getSource()==classOption){
			attributeInputter.setEnabled(false);
			attributeValueInputter.setEnabled(false);
			classInputter.setEnabled(true);
		}
    }
    else if (e.getStateChange() == ItemEvent.DESELECTED) {
		if(e.getSource()==classOption){
			attributeInputter.setEnabled(true);
			attributeValueInputter.setEnabled(true);

			classInputter.setEnabled(false);
		}
    }
}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 15));
		g.drawString("Load",30,35);
		g.drawString("Website address",380,35);
		g.drawString("Select",30,70);
		g.drawString("element with",200,105);
		g.drawString("Attribute value = ",30,225);
		g.drawString("Result:",30,265);

		g.drawString("Return type:",290,72);
	}
	
	public void showResult(String website,String elementName, String attributeName,String classOrAttributeValue,String classOrAttribute){
		try{

			Document doc = Jsoup.connect(website).get();
			if(oldReturnType.equals("Attribute value")){
				String parameterToPass=""+elementName;
				if(classOrAttributeValue.length()>0){
					parameterToPass+="."+classOrAttributeValue;
				}
				Element time=doc.select(parameterToPass).first();
				textArea.setText(time.attr(attributeName));
			}else if(classOrAttribute.equals("class")){
				Elements time=doc.select(elementName+"."+classOrAttributeValue);
				textArea.setText(time.first().text());
			}else{
				Elements time2=doc.select(elementName+"["+attributeName+"="+classOrAttributeValue+"]");
				textArea.setText(time2.first().text());
			}
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}



	


}
MIT License

Copyright (c) 2017 Surya Kamal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
	
public class Test{

	static String prev,next;	
	static ArrayList<String> P = new ArrayList<String>();
	static ArrayList<String> N = new ArrayList<String>();

	public static void bind(final int in)throws Exception{
		new Thread(){
			public void run(){
				try{
					String a,b,c;
					for(int i=in;i>=0;i--){
						if(N.get(i).substring(1,2).equals(":")){
							a=N.get(i).substring(9,10);
							b=N.get(i+1).substring(13,14);
							c=N.get(in).substring(17,18);		
							System.out.println(a+"-"+b+"."+c);
							String add="";							
							try{
								int cint=Integer.parseInt(c);
								add="\'"+a+"-"+b+"."+c+"\'";
							}
							catch(Exception e){
								add="\'"+a+"-"+b+"\'";
							}
							System.out.println(add);
							String command="echo "+ add +" > /sys/bus/usb/drivers/usb/unbind";
							Process p1 =Runtime.getRuntime().exec(new String[]{"bash","-c",command});
							command="echo "+ add +" > /sys/bus/usb/drivers/usb/bind";
							new Password(command,in);
							break;
						}
					}
				}catch(Exception e){System.out.println(e.toString());}
			}
		}.start();
	}

	public static void exec()throws Exception{
		String p[] = prev.split("\n");
		String n[] = next.split("\n"); 
		P.clear();
		N.clear();
		for(int i=0;i<p.length;i++){
			P.add(p[i]);
		}
		for(int i=0;i<n.length;i++){
			N.add(n[i]);
		}
		for(int i=0;i<N.size();i++){
			if(!P.contains(N.get(i))){
				bind(i);
			}
		}
		prev="";
		for(int i=0;i<N.size();i++){
			prev+=N.get(i)+"\n";
		}
		while(Thread.activeCount()!=1){}
	}
	
	public static void remove(){
		try{
			Process p0 = Runtime.getRuntime().exec("rm out.txt");
		}catch(Exception e){System.out.println(e.toString());}
	}

	public static void check()throws Exception{
		String tmp="";
		remove();
		Process p1 = Runtime.getRuntime().exec(new String[]{ "bash" , "-c" , "lsusb -t" });		
		BufferedReader br1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
		next="";
		tmp="";
		while((tmp = br1.readLine())!=null)
			next+=tmp+"\n";
		
		if(!prev.equals(next)){
			exec();
		}
	}

	public static void init()throws Exception{
		Process p1 = Runtime.getRuntime().exec("sh out.sh");		
		remove();
		p1 = Runtime.getRuntime().exec(new String[]{ "bash" , "-c" , "lsusb -t" });		
		BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
		prev="";
		String tmp="";
		while((tmp = br.readLine())!=null)
			prev+=tmp+"\n";

		while(true){
			try{
				check();
			}catch(Exception e){System.out.println(e.toString());}
		}	
	}

	public static void main(String []args)throws Exception{
		while(true){
			try{
				init();
			}catch(Exception e){System.out.println(e.toString());}
		}
	}
}

class Password extends Frame implements ActionListener{

	private Panel p1,p4;
	private Label lbl;
	private TextField pass;
	private Button submitBtn,exitBtn;

	int count;
	String Command;
	int index;

	public Password( String Command , int index){
		this.Command=Command;
		this.index=index;
	
		setLayout(new BorderLayout());
		lbl=new Label("Enter The Token");
		p1=new Panel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		p1.add(lbl);
		add("North",p1);
	
		p4=new Panel();
		p4.setLayout(new FlowLayout());
		pass=new TextField("",25);
		pass.setEditable(true);
		pass.setEchoChar('*');
		p4.add(pass);
		submitBtn=new Button("Submit");
		p4.add(submitBtn);
		exitBtn=new Button("Exit");
		p4.add(exitBtn);
		add("South",p4);

		pass.addActionListener(this);
		submitBtn.addActionListener(this);
		exitBtn.addActionListener(this);
	
		setSize(550,150);
	
		setTitle("Pen-Drive By-Pass");
	
		p1.setVisible(true);
		p4.setVisible(true);
		setVisible(true);
	}

	public void submitbtn()throws Exception{
		if(pass.getText().equals("what")){
			Process p1 =Runtime.getRuntime().exec(new String[]{"bash","-c",Command});		
			dispose();
		}
		else if(count<3){
			lbl.setText("Wrong Token."+ ++count);
		}
		else{
			dispose();
			Test.N.remove(index);
			pass.setEditable(false);
		}
	}	
	
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource()==submitBtn){
			try{
				submitbtn();
			}catch(Exception e){}
		}
		else if(evt.getSource()==pass){
			try{
				submitbtn();
			}catch(Exception e){}
		}
		else if(evt.getSource()==exitBtn){
			dispose();
		}
	}	
}

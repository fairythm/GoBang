package GoBang;

import javax.swing.*;
import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.*;
import java.awt.event.*;

public class MAIN {
	//ȫ�ֱ�����
		//static Graphics pencil;
		public int i,j;
		int [][] buff;							//���̾���
		JTextField Screen;						//���÷�����Ϣ�Ի���
		JFrame f;
		JButton buttons[];
		JButton takebackmove;					//���û��尴ť
	
	

	
	public static void main(String [] args){
		MAIN gui=new MAIN();
		gui.go();
	}
	
	public void go(){
		
		buff=new int[15][15];										//�������̾���
		for(i=0;i<15;i++)for(j=0;j<15;j++)buff[i][j]=0;
		
		log.move=new int[225];										//������־��ʼ��
		for(i=0;i<225;i++)log.move[i]=-1;
		
		log.acumulator=0;											//ͨ�ü���������
		i=0;		
		
		f=new JFrame();												//���̴��ڳ�ʼ��
		f.setLayout(null);
		f.setTitle("������");										//������Ϸ����
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//���ùر�
		f.setBackground(Color.black);
		
		Screen=new JTextField();									//������Ϣ�Ի�����
		Screen.add(new JTextField(60));
		Screen.setBounds(0, 0, 900, 32);
		Screen.setBackground(Color.white);
		f.add(Screen);

		takebackmove=new JButton("Take Back Move ");				//���û��尴ť
		takebackmoveListener t=new takebackmoveListener();			//������尴ť����������
		moveListener[]m=new moveListener[225];						//��ʼ���������������
		takebackmove.addActionListener(t);							//ע����尴ť������
		takebackmove.setBounds(0, 930, 900, 20);
		takebackmove.setBackground(Color.white);
		
		buttons=new JButton[225];									//�������Ӱ���
		for(i=0;i<225;i++){
			buttons[i]=new JButton();								//button��ʼ��
			buttons[i].setBounds((i/15)*60, (i%15)*60+30, 60, 60);	//button�Ű�
			buttons[i].setBackground(Color.yellow);
			//pencil.drawLine(15, 15, 50, 50);
				
			m[i]=new moveListener();								//��ʼ��ÿһ��������
			m[i].subscript=i;										//���ü������±��Ա�������ܱ������İ�ť
			buttons[i].addActionListener(m[i]);						//ע�������
			f.getContentPane().add(buttons[i]);						//�����̰�ť��������
		}

		f.add(takebackmove);										//�����尴ť������������
		f.setSize(918,997);											//����������Ļ�ֱ���

		f.setVisible(true);											//����Ϸ����
	}

	
	private class takebackmoveListener implements ActionListener	//���������
   	{
		public void actionPerformed(ActionEvent event)
		{
			if(log.acumulator==0){
				Screen.setText("����㻹û������");
				return;
			}
			Screen.setText("������ˣ���ɳܣ����� ");
          	
          	buttons[log.move[log.acumulator]].setBackground(Color.yellow);;
          	log.move[log.acumulator]=-1;
          	log.acumulator--;
		}

     }
	public class moveListener implements ActionListener				//�������
    	{
			public void actionPerformed(ActionEvent event){			//���������¼�
				//String buttonName = event.getActionCommand();		//������ͬһ��λ���Ϸ�������
				if (buff[subscript%15][subscript/15]==1||buff[subscript%15][subscript/15]==-1)
					return;
				if(log.acumulator%2==0){
					//buttons[subscript].setText("X");
					buttons[subscript].setBackground(Color.black);
					Screen.setText("��������  ��������:"+(log.acumulator+1));
					buff[subscript%15][subscript/15]=1;
					if(panduan(subscript))
						Screen.setText("����Ӯ");
				}
				else{
					//buttons[subscript].setText("O");
					buttons[subscript].setBackground(Color.white);
					Screen.setText("��������  ��������:"+(log.acumulator+1));
					buff[subscript%15][subscript/15]=-1;
					if(panduan(subscript))
						Screen.setText("����Ӯ");
				}
				
				log.acumulator++;
				log.move[log.acumulator]=subscript;					//Ϊ������־���:��acumulator����־��������subscriptλ��
			}
			int subscript;
			
      }
	public boolean panduan(int subscript)							//Ӯ���ж���
	   {
			int x,y;
			x=subscript%15;
			y=subscript/15;
			
		   if(log.acumulator%2==0)
		   {
			   int a=0,w=0,s=0,d=0;
			   int i=0;

			  while(x+i+1<15&&buff[x+i+1][y]==1)
			   {
				   w++;
				   i++;
			   }
			   i=0;

			   while(x-i-1>=0&&buff[x-i-1][y]==1)
			   {
				   w++;
				   i++;
			   }
			   i=0;

			   while(x+i+1<15&&y+i+1<15&&buff[x+i+1][y+i+1]==1)
			   {
				   a++;
				   i++;
			   }
			   i=0;
			   while(x-i-1>=0&&y-i-1>=0&&buff[x-i-1][y-i-1]==1)
			   {
				   a++;
				   i++;
			   }
			   i=0;
			   while(x-i-1>=0&&y+i+1<15&&buff[x-i-1][y+i+1]==1)
			   {
				   d++;
				   i++;
			   }
			   i=0;
			   while(x+i+1<15&&y-i-1>=0&&buff[x+i+1][y-i-1]==1)
			   {
				   d++;
				   i++;
			   }
			   i=0;
			   while(y+i+1<15&&buff[x][y+i+1]==1)
			   {
				   s++;
				   i++;
			   }
			   i=0;
			   while(y-i-1>=0&&buff[x][y-i-1]==1)
			   {
				   s++;
				   i++;
			   }
			   if(a>=4||w>=4||d>=4||s>=4)
			   {
				   return true;
			   }
		   }
		   else if (log.acumulator%2==1) {
			   int a=0,w=0,s=0,d=0;
			   int i=0;
			   while(x+i+1<15&&buff[x+i+1][y]==-1)
			   {
				   w++;
				   i++;
			   }
			   i=0;

			   while(x-i-1>=0&&buff[x-i-1][y]==-1)
			   {
				   w++;
				   i++;
			   }
			   i=0;

			   while(x+i+1<15&&y+i+1<15&&buff[x+i+1][y+i+1]==-1)
			   {
				   a++;
				   i++;
			   }
			   i=0;
			   while(x-i-1>=0&&y-i-1>=0&&buff[x-i-1][y-i-1]==-1)
			   {
				   a++;
				   i++;
			   }
			   i=0;
			   while(x-i-1>=0&&y+i+1<15&&buff[x-i-1][y+i+1]==-1)
			   {
				   d++;
				   i++;
			   }
			   i=0;
			   while(x+i+1<15&&y-i-1>=0&&buff[x+i+1][y-i-1]==-1)
			   {
				   d++;
				   i++;
			   }
			   i=0;
			   while(y+i+1<15&&buff[x][y+i+1]==-1)
			   {
				   s++;
				   i++;
			   }
			   i=0;
			   while(y-i-1>=0&&buff[x][y-i-1]==-1)
			   {
				   s++;
				   i++;
			   }
			   if(a>=4||w>=4||d>=4||s>=4)
			   {
				   return true;
			   }
		}
		   return false;
		   
	   }
	public static class log{										//������־
		public static int acumulator;
		public static int move[];
	}

}
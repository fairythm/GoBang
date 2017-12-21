package GoBang;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
//import javax.swing.event.*;
public class MAIN {
	private int i,j;
	private int [][] buff;											//棋盘矩阵
	private JTextField Screen;										//设置反馈信息对话框
	private final ThreadLocal<JFrame> f = new ThreadLocal<JFrame>();
	private JButton buttons[];
	private JButton takebackmove;									//设置悔棋按钮
	private boolean GameOver;

	public static void main(String [] args){
		MAIN gui=new MAIN();
		gui.go();
	}

	private void go(){
		GameOver=false;
		buff=new int[15][15];										//构建棋盘矩阵
		for(i=0;i<15;i++)for(j=0;j<15;j++)buff[i][j]=0;

		log.move=new int[225];										//下棋日志初始化
		for(i=0;i<225;i++)log.move[i]=-1;

		log.acumulator=0;											//通用计数器归零
		i=0;

		f.set(new JFrame());												//棋盘窗口初始化

		f.get().setTitle("GoBang");											//设置游戏标题
		f.get().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//设置关闭
		f.get().setBackground(Color.black);

		Screen=new JTextField();										//设置信息对话窗口
		Screen.add(new JTextField(60));
		Screen.setBounds(0, 0, 900, 32);
		Screen.setBackground(Color.white);
		f.get().add(Screen);

		takebackmove=new JButton("");								//设置悔棋按钮
		TakeBackMoveListener t=new TakeBackMoveListener();				//定义悔棋按钮监听器对象
		moveListener[]m=new moveListener[225];							//初始化下棋监听器数组
		takebackmove.addActionListener(t);								//注册悔棋按钮监听器
		takebackmove.setBounds(0, 930, 450, 40);		//悔棋按钮排版
		takebackmove.setBackground(Color.white);
		f.get().add(takebackmove);										//将悔棋按钮放置于棋盘中

		NewGameListener newgame=new NewGameListener();
		JButton newGame = new JButton("New Game");
		newGame.setBounds(450, 930, 450, 40);
		newGame.addActionListener(newgame);
		newGame.setBackground(Color.white);
		f.get().add(newGame);

		buttons=new JButton[225];									//设置落子按键
		for(i=0;i<225;i++){
			buttons[i]=new JButton();								//button初始化
			buttons[i].setBounds((i/15)*60, (i%15)*60+30, 60, 60);	//button排版
			buttons[i].setBackground(Color.yellow);
			//pencil.drawLine(15, 15, 50, 50);

			m[i]=new moveListener();								//初始化每一个监听器
			m[i].subscript=i;										//设置监听器下标以便监听器能辨认它的按钮
			buttons[i].addActionListener(m[i]);						//注册监听器
			f.get().getContentPane().add(buttons[i]);						//将棋盘按钮载入棋盘
		}

		f.get().setSize(918,1017);							//设置棋盘屏幕分辨率

		f.get().setVisible(true);											//打开游戏界面
	}

	private class TakeBackMoveListener implements ActionListener	//悔棋监听器
	{
		public void actionPerformed(ActionEvent event)
		{
			if(!GameOver){
				if(log.acumulator==0){
					return;
				}
				if(log.acumulator!=1) {
					if (log.acumulator % 2 == 0) {
						Screen.setText("White took back a move");
						takebackmove.setText("Undo");
					} else {
						Screen.setText("Black took back a move");
						takebackmove.setText("Undo");
					}
				}
				else takebackmove.setText("");

				buttons[log.move[log.acumulator]].setBackground(Color.yellow);
				buff[log.move[log.acumulator]%15][log.move[log.acumulator]/15]=0;
				log.move[log.acumulator]=-1;
				log.acumulator--;
			}
			else Screen.setText("Game Over");
		}
	}
	private class NewGameListener implements ActionListener			//新游戏按钮监听器
	{
		public void actionPerformed(ActionEvent event)
		{
			int i,j;
			for(i=0;i<15;i++)for(j=0;j<15;j++)buff[i][j]=0;
			for(i=0;i<225;i++){
				buttons[i].setBackground(Color.yellow);
				log.move[i]=-1;
			}
			Screen.setText("New Game");
			log.acumulator=0;
			GameOver=false;
		}
	}
	private class moveListener implements ActionListener			//下棋监听
	{
		public void actionPerformed(ActionEvent event){			//设置下棋事件
			if(!GameOver)
			{
				//String buttonName = event.getActionCommand();		//避免在同一个位置上反复下棋
				if (buff[subscript%15][subscript/15]==1||buff[subscript%15][subscript/15]==-1)
					return;
				if(log.acumulator%2==0){
					//buttons[subscript].setText("X");
					takebackmove.setText("Undo");
					buttons[subscript].setBackground(Color.black);
					Screen.setText("Black dropped  Total drops:"+(log.acumulator+1));
					//System.out.println("黑棋落子 总落子数:"+(log.acumulator+1));
					buff[subscript%15][subscript/15]=1;
					if(JudgeWinner(subscript)){
						//JOptionPane.showMessageDialog(frame,"按钮1 被点击");
						Screen.setText("Black wins");
						GameOver=true;
					}
				}
				else{
					//buttons[subscript].setText("O");
					takebackmove.setText("Undo");
					buttons[subscript].setBackground(Color.white);
					Screen.setText("White dropped  Total drops:"+(log.acumulator+1));
					buff[subscript%15][subscript/15]=-1;
					if(JudgeWinner(subscript)){
						Screen.setText("White wins");
						GameOver=true;
					}
				}

				log.acumulator++;
				log.move[log.acumulator]=subscript;					//为下棋日志添加:第acumulator个日志是落子于subscript位置
			}
			else Screen.setText("Game is already over");
		}
		int subscript;
	}
	private boolean JudgeWinner(int subscript)						//赢棋判断器
	{
		int x,y;
		x=subscript%15;
		y=subscript/15;

		if(log.acumulator%2==0) {
			int a=0,w=0,s=0,d=0,i=0;
			while(x+i+1<15&&buff[x+i+1][y]==1) { w++;i++; }i=0;
			while(x-i-1>=0&&buff[x-i-1][y]==1) { w++;i++; }i=0;
			while(x+i+1<15&&y+i+1<15&&buff[x+i+1][y+i+1]==1) { a++;i++; }i=0;
			while(x-i-1>=0&&y-i-1>=0&&buff[x-i-1][y-i-1]==1) { a++;i++; }i=0;
			while(x-i-1>=0&&y+i+1<15&&buff[x-i-1][y+i+1]==1) { d++;i++; }i=0;
			while(x+i+1<15&&y-i-1>=0&&buff[x+i+1][y-i-1]==1) { d++;i++; }i=0;
			while(y+i+1<15&&buff[x][y+i+1]==1) { s++;i++; }i=0;
			while(y-i-1>=0&&buff[x][y-i-1]==1) { s++;i++; }
			if(a>=4||w>=4||d>=4||s>=4) return true;
		}
		if(log.acumulator%2!=0){
			int a=0,w=0,s=0,d=0,i=0;
			while(x+i+1<15&&buff[x+i+1][y]==-1) { w++;i++; }i=0;
			while(x-i-1>=0&&buff[x-i-1][y]==-1) { w++;i++; }i=0;
			while(x+i+1<15&&y+i+1<15&&buff[x+i+1][y+i+1]==-1) { a++;i++; }i=0;
			while(x-i-1>=0&&y-i-1>=0&&buff[x-i-1][y-i-1]==-1) { a++;i++; }i=0;
			while(x-i-1>=0&&y+i+1<15&&buff[x-i-1][y+i+1]==-1) { d++;i++; }i=0;
			while(x+i+1<15&&y-i-1>=0&&buff[x+i+1][y-i-1]==-1) { d++;i++; }i=0;
			while(y+i+1<15&&buff[x][y+i+1]==-1){ s++;i++; }i=0;
			while(y-i-1>=0&&buff[x][y-i-1]==-1) { s++;i++; }
			if(a>=4||w>=4||d>=4||s>=4) return true;
		}
		return false;
	}
	private static class log{										//下棋日志
		private static int acumulator;
		private static int move[];
	}
}
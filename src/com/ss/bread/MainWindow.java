/*join 문이란?
 * -정규화에 의해 물리적으로 분리된 테이블을 마치 하나의 테이블처럼
 * 보여줄 수 있는 쿼리 
 * 
 * 지금까지 써온것은 inner join이었다.
 * outer join도 있다.
 * 
 * inner join : 조인대상이 되는 테이블간 공통적인 레코드만 가져온다.
 * 		주의할점: 공통적인 레코드가 아닌 경우 누락시킨다.!!	
 * 
 *tip:개발자들이 outer join을 많이 물어본다.
 * outer join : 조인대상이 되는 테이블간 공통된 레코드뿐만 아니라,
 * 					지정한 테이블의 레코드는 모두 가져온다. 
 * 
 * */

package com.ss.bread;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements ItemListener, ActionListener{
	JPanel p_west, p_center, p_east;
	JPanel p_center_up, p_center_down;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;
	
	//서쪽영역
	Choice ch_top, ch_sub;
	JTextField t_name_west, t_price_west;
	Canvas can_west;
	BufferedImage image=null;
	JButton bt_regist;
	
	//동쪽영역
	Canvas can_east;
	
	JTextField t_id_east, t_name_east, t_price_east;
	JButton bt_update, bt_delete;
	
	DBManager manager;
	Connection con;
	ArrayList<Top_category> topList=new ArrayList<Top_category>();
	ArrayList<Sub_category> subList=new ArrayList<Sub_category>();
	
	//Tablemodel 객체들
	UpModel upModel;
	DownModel downModel;
	
	JFileChooser chooser;
	File file;
	
	
	public MainWindow() {
		p_west=new JPanel();
		p_center=new JPanel();
		p_east=new JPanel();
		p_center_up=new JPanel();
		p_center_down=new JPanel();
		table_up=new JTable();
		table_down=new JTable();
		scroll_up=new JScrollPane(table_up);
		scroll_down=new JScrollPane(table_down);
				
		//서쪽영역
		ch_top=new Choice();
		ch_sub=new Choice();
		t_name_west=new JTextField(10);
		t_price_west=new JTextField(10);
		
		try {
			URL url=this.getClass().getResource("/noimg.png");
			image=ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		can_west=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage((Image)image, 0, 0, 135, 135, this); //내부익명클래스는 final로 선언된 지역변수만 받을수 있으므로 멤버변수로 돌리자
			}
		};
		can_west.setPreferredSize(new Dimension(135, 135));
		
		
		bt_regist=new JButton("등록");
					
		//동쪽영역
		can_east=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135, 135, this);
			}		
		};
		can_east.setPreferredSize(new Dimension(135, 135));
		
		t_id_east=new JTextField(10);
		t_name_east=new JTextField(10);
		t_price_east=new JTextField(10);
		bt_update=new JButton("수정");
		bt_delete=new JButton("삭제");
				
		//각 패널의 색상 지정
		p_west.setBackground(Color.CYAN);
		p_center.setBackground(Color.RED);
		p_east.setBackground(Color.YELLOW);
		p_center_up.setBackground(Color.ORANGE);
		p_center_down.setBackground(Color.GREEN);
		
		//각 패널들의 크기지정
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		ch_top.setPreferredSize(new Dimension(135, 40));
		ch_sub.setPreferredSize(new Dimension(135, 40));
		ch_top.add("상위목록 선택▼");
		ch_sub.add("하위목록 선택▼");
		
		chooser=new JFileChooser("E:/git/java_workspace3/project_day041_0410/res/");
		
		//서쪽 부착
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name_west);
		p_west.add(t_price_west);
		p_west.add(can_west);
		p_west.add(bt_regist);

		//가운데에 스크롤 부착
		p_center_up.setLayout(new BorderLayout()); //딱달라붙게 border로
		p_center_down.setLayout(new BorderLayout());		
		p_center_up.add(scroll_up);
		p_center_down.add(scroll_down);
		
		//동쪽 부착
		p_east.add(can_east);
		t_id_east.setEditable(false);//수정못하게
		p_east.add(t_id_east);		
		p_east.add(t_name_east);
		p_east.add(t_price_east);
		p_east.add(bt_update);
		p_east.add(bt_delete);		
		
		//센터의 그리드 적용하고 위 아래 구성
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_center_up);
		p_center.add(p_center_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east, BorderLayout.EAST);
				
		//초이스와 리스너 연결
		ch_top.addItemListener(this);
		bt_regist.addActionListener(this);	
		
		//캔버스에 마우스 리스너 연결
		can_west.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				preView();
			}			
		});
		
		//다운테이블과 리스너 연결
		table_up.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row=table_up.getSelectedRow();
				int col=0;
				String sub_category_id=(String) table_up.getValueAt(row, col);				
				System.out.println(sub_category_id);
				
				//구해진 id를 아래의 모델에 적용하자.
				downModel.getList(Integer.parseInt(sub_category_id));			
				table_down.updateUI();
				
			}
		});
		
		table_down.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row=table_down.getSelectedRow();
				
				//이차원 벡터에 들어 있는 벡터를 얻어오자.
				Vector vec=downModel.data.get(row);
				//System.out.println(vec.get(2));
				getDetail(vec);
			}			
		});
				
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		init();
		getTop();
		getUpList(); //위쪽 테이블 처리
		getDownList();
	}
	
	//데이터베이스 커넥션 얻기
	public void init(){
		manager=DBManager.getInstance();
		con=manager.getConnection();
		System.out.println(con);
		
	}
	
	//최상위 카테고리 얻기
	public void getTop(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
				
		String sql="select * from top_category order by top_category_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				//ch_top.add(rs.getString("top_category_name")); dto를 써보자
				Top_category dto=new Top_category();
				dto.setTop_category_id(rs.getInt("top_category_id"));
				dto.setTop_category_name(rs.getString("top_category_name"));
			
				topList.add(dto);//리스트에 탑재
				ch_top.add(dto.getTop_category_name());
			}
			
			/*for (int i = 0; i < topList.size(); i++) {
				ch_top.add(topList.get(i).getTop_category_name());
			}*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}			
		}		
	}
	
	//위쪽 테이블 데이터 처리
	public void getUpList(){
		table_up.setModel(upModel=new UpModel(con));
		table_up.updateUI();		
	}
	
	//아래쪽 테이블 데이터 처리
	public void getDownList(){
		table_down.setModel(downModel=new DownModel(con));
		table_down.updateUI();
	}
	
	//바인드 변수
	public void getSub(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from sub_category where top_category_id=?"; //bind변수 이용하자
		try {
			pstmt=con.prepareStatement(sql);
			//바인드 변수값 지정
			int index=ch_top.getSelectedIndex();
			if(index-1>=0){
				Top_category dto=topList.get(index-1);
				pstmt.setInt(1, dto.getTop_category_id());//첫번째 발견된 바인드변수를, 지정된 값으로 정한다. 
				rs=pstmt.executeQuery();
								
				//담기전에 지우기
				subList.removeAll(subList);
				ch_sub.removeAll();
				
				while (rs.next()) {
					Sub_category subDto=new Sub_category();
					subDto.setSub_category_id(rs.getInt("sub_category_id"));
					subDto.setTop_category_id(rs.getInt("top_category_id"));
					subDto.setSub_cateogry_name(rs.getString("sub_category_name"));
					
					subList.add(subDto);
					ch_sub.add(subDto.getSub_cateogry_name());
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		//하위카테고리 구하기
		getSub();		
		
	}
	
	/*---------------------------------------------------------
	 * 상품등록
	 * ----------------------------------------------------------*/	
	public void regist(){
		PreparedStatement pstmt=null;		
				
		String sql="insert into product(product_id, sub_category_id, product_name, product_price, product_img)";
		sql+="values(seq_product.nextVal, ?, ?, ? ,?)";
		
		try {
			pstmt=con.prepareStatement(sql);
			
			//arraylist안에 들어있는 subcategory dto를 추출하여 fk값을 넣어주자
			/*int index=ch_sub.getSelectedIndex();
			Sub_category vo=subList.get(index);
			int sub_id=vo.getSub_category_id();*/
			
			//바인드 변수에 들어갈 값 결정
			pstmt.setInt(1, subList.get(ch_sub.getSelectedIndex()).getSub_category_id());
			pstmt.setString(2, t_name_west.getText());
			pstmt.setInt(3, Integer.parseInt(t_price_west.getText()));
			pstmt.setString(4, file.getName());
			
			System.out.println(sql);
			//executeUpdate메소드는 쿼리문 수행 후 반영된 레코드의 갯수를 반환해준다.
			//따라서 insert문의 경우 언제나 성공했다면 1건, update는 1건 이상, delete 1건
			//결론) insert시 반환값이 0이라면 insert 실패
			int result=pstmt.executeUpdate();
			if (result!=0) {
				JOptionPane.showMessageDialog(this, "등록성공");
								
				upModel.getList(); //db를 새롭게 가져와 이차원 벡터 변경
				table_up.updateUI();
				
				copy();
				
			}else{
				JOptionPane.showMessageDialog(this, "등록실패");								
			}			
						
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//캔버스에 이미지 반영하기
	public void preView(){
		int result=chooser.showOpenDialog(this);
		if (result==JFileChooser.APPROVE_OPTION) {
			//캔버스에 이미지 그리자!
			file=chooser.getSelectedFile(); //복사할때 쓰게 멤버변수로
			
			//얻어진 파일을 기존의 이미지로 대체한다.
			//image=파일과 관련한 새로운이미지;
			try {
				image=ImageIO.read(file);
				can_west.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//복사 메소드 정의
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream("E:/git/java_workspace3/project_day041_0410/data/"+file.getName());			
			
			byte[] b=new byte[1024];			
			int flag; //-1인지 판단
			while (true) {
				flag=fis.read(b);
				if (flag == -1) break;
				fos.write(b);
			}
			System.out.println("이미지 복사 완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//상세정보 보여주기
	public void getDetail(Vector vec){
		t_id_east.setText(vec.get(0).toString());
		t_name_east.setText(vec.get(2).toString());
		t_price_east.setText(vec.get(3).toString());
		try {
			image=ImageIO.read(new File("E:/git/java_workspace3/project_day041_0410/data/"+vec.get(4)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		can_east.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		regist();
	}
		
	public static void main(String[] args) {
		new MainWindow();
		
	}



}

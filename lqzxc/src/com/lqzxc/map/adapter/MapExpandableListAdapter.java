package com.lqzxc.map.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKStep;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.modal.RouteResult;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * �Զ��� ExpandableListAdapter
 * @author QQ472950043
 **/  
public class MapExpandableListAdapter extends BaseExpandableListAdapter {

	Context mContext;
	AppContext mAppContext;
	LayoutInflater mInflater;
	MKPlanNode stNode;//���
	MKPlanNode enNode;//�յ�
	String[] plan_index;
	String[] plan_name;
	String[] plan_cost;
	int[][] line_logo;
	String[][] line_content;
	ArrayList<HashMap<String, String>> transitResults;//������·�б�
	HashMap<String,String> map;
	
    //��дExpandableListAdapter�еĸ�������
	public MapExpandableListAdapter(Context context) {
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		mAppContext = (AppContext) mContext.getApplicationContext();
	}
	
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return plan_index.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return plan_index[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return line_content[groupPosition].length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return line_content[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View view, ViewGroup parent) {
        // TODO Auto-generated method stub
    	view = mInflater.inflate(R.layout.baidu_map_group, parent, false);
    	TextView index = (TextView) view.findViewById(R.id.plan_index);
    	TextView name = (TextView) view.findViewById(R.id.plan_name);
    	TextView cost = (TextView) view.findViewById(R.id.plan_cost);
    	ImageView spread = (ImageView) view.findViewById(R.id.plan_spread);
    	index.setText(plan_index[groupPosition]);
    	name.setText(plan_name[groupPosition]);
    	cost.setText(plan_cost[groupPosition]);
    	if(!isExpanded){
    		spread.setImageResource(R.drawable.baidu_map_btn_icon_arrow_down);
    	}
    	else{
    		spread.setImageResource(R.drawable.baidu_map_btn_icon_arrow_up);
        } 
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
    	view = mInflater.inflate(R.layout.baidu_map_child, parent, false);
    	ImageView logo = (ImageView) view.findViewById(R.id.line_logo);
    	TextView content = (TextView) view.findViewById(R.id.line_content);
    	logo.setImageResource(line_logo[groupPosition][childPosition]);
    	content.setText(Html.fromHtml(line_content[groupPosition][childPosition]));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

	public void setData(MKTransitRouteResult transitRouteResult) {
        // TODO Auto-generated method stub
		//׼���б�����
		stNode = transitRouteResult.getStart();// ���
		enNode = transitRouteResult.getEnd();// �յ�
		int numPlan = transitRouteResult.getNumPlan();// ������
		plan_index = new String[numPlan];// ���ڵ�:��������
		plan_name = new String[numPlan];// ���ڵ�:������
		plan_cost = new String[numPlan];// ���ڵ�:��������
		line_logo = new int[numPlan][];// �ӽڵ�:·���ڵ�ͼƬ
		line_content = new String[numPlan][];// �ӽڵ�:·���ڵ�����
		// �������з���
		for (int i = 0; i < numPlan; i++) {
			// �õ���i������
			MKTransitRoutePlan plan = transitRouteResult.getPlan(i);
			// ���ڵ�
			// ���÷��������ı�
			plan_index[i] = getPlanIndex(i);
			// ���÷�����
			plan_name[i] = plan.getContent().replaceAll("_", "��");
			// ���÷�������
			plan_cost[i] = getPlanCost(plan.getTime(), plan.getDistance());
	        
			//׼���ø��ڵ����ӽڵ������
			initTransitResults(plan, plan.getNumLines(), plan.getNumRoute());
			int num = plan.getNumRoute() + plan.getNumLines();
			line_logo[i]=new int[num];
			line_content[i]=new String[num];
			//�����ø��ڵ��µ�num���ӽڵ�
			for(int j=0;j<num;j++){
				if(transitResults.get(j).get("type").equals("2")) {//����
					line_logo[i][j]=R.drawable.baidu_map_icon_nav_foot;
					line_content[i][j]=transitResults.get(j).get("tip");
				}else{
					line_logo[i][j]=R.drawable.baidu_map_icon_nav_bus;
					line_content[i][j]=transitResults.get(j).get("tip");
				}
			}
		}
		notifyDataSetChanged();
	}

	/**
	 * ���ڵ�:��ʽ���÷�������
	 * @param index 
	 * @author QQ472950043
	 */
	public String getPlanIndex(int index) {
		// TODO Auto-generated method stub
		if(index<9)
			return "0"+(index+1);
		else
			return ""+(index+1);
	}
	
	/**
	 * ���ڵ�:��ʽ���÷�������
	 * @param time
	 * @param distance
	 * @return ʱ��/���루��ౣ��һλС����
	 * @author QQ472950043
	 */
	public String getPlanCost(int time, int distance) {
		// TODO Auto-generated method stub
		String cost = "";
		if (time < 3600) {
			cost = cost + time / 60 + "��";
		} else {
			cost = cost + time / 3600 + "Сʱ" + time % 3600 / 60 + "��";
		}
		cost = cost + "/";
		if (distance < 1000) {
			cost = cost + distance + "��";
		} else {
			cost = cost + String.format("%.1f����", distance * 1.0 / 1000);
		}
		return cost;
	}

	/**
	 * �ӽڵ�:����·�����ݳ�ʼ��
	 * @param plan
	 * @param linenum
	 * @param routenum
	 * @author QQ472950043
	 */
	public void initTransitResults(MKTransitRoutePlan plan, int linenum, int routenum) {
		System.out.println("����ǰ:");
		transitResults = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < linenum; i++) {
			map = new HashMap<String, String>();
			map.put("type", "" + plan.getLine(i).getType());// ��i��MKLine
			map.put("tip", plan.getLine(i).getTip());// ���ͣ�1��ʾ�������ǵ�����·
			// �����й�����·��line
			transitResults.add(map);
			System.out.println(plan.getLine(i).getType() + " " + plan.getLine(i).getTip());
		}
		for (int i = 0; i < routenum; i++) {
			map = new HashMap<String, String>();
			map.put("type", "" + plan.getRoute(i).getRouteType());// ��i��MKLine
			map.put("tip", plan.getRoute(i).getTip());// ���ͣ�1��ʾ�������ǵ�����·
			// ����·���ڷ����е�����λ�ã�����plan.getRoute(i).getIndex()��ʾ��λ�ò��뵽transitResults��
			transitResults.add(plan.getRoute(i).getIndex() + 1 + i, map);
			System.out.println(plan.getRoute(i).getRouteType() + " " + plan.getRoute(i).getTip());
		}
		System.out.println("�����");
		for (int i = 0; i < transitResults.size(); i++) {
			System.out.println("" + transitResults.get(i).get("type") + " " + transitResults.get(i).get("tip"));
		}
	}

	/**
	 * �ݳ�������ʹ�õ����ݽṹ��ͬ���������Ϊ�ݳ����У��ڵ����������ͬ
	 * @param route
	 * @param stNode
	 * @param enNode
	 * @author QQ472950043
	 */
	public void setData() {
        // TODO Auto-generated method stub
		// ׼���б�����
		int numPlan = mAppContext.routeResults.size();
		plan_index=new String[numPlan];
		plan_name=new String[numPlan];
		plan_cost=new String[numPlan];
		line_logo=new int[numPlan][];
		line_content=new String[numPlan][];
		for(int i=0;i<numPlan;i++){
			stNode = mAppContext.routeResults.get(i).getStart();
			enNode = mAppContext.routeResults.get(i).getEnd();
			// �õ���i������
			RouteResult routeResult = mAppContext.routeResults.get(i);
			// ���ڵ�
			// ���÷��������ı�
			plan_index[i] = getPlanIndex(i);
			// ���÷�����
			plan_name[i] = routeResult.getRoute().getTip().replaceAll("_", "��");
			// ���÷�������
			plan_cost[i] = getPlanCost(routeResult.getTime(), routeResult.getDistance());

			// �ݳ��Ͳ��е����ݽṹ�� MKStep����route.get(i).getStep(nodeIndex)
			MKRoute route = routeResult.getRoute();
			int numStep = route.getNumSteps();
			line_logo[i] = new int[numStep];
			line_content[i] = new String[numStep];
			// ��������������route.get(i).getNumSteps()��
			for (int j = 0; j < numStep; j++) {
				MKStep step = route.getStep(j);
				// �����ӽڵ������
				String content = step.getContent();
				line_content[i][j] = formatTip(content);
				//�����ӽڵ��ͼ��
				line_logo[i][j]=getLogo(i,j,content);
				
			}
		}
		notifyDataSetChanged();
	}

	/**
	 * ��ʽ��Tip������Ϣ
	 */
	public String formatTip(String tip) {
		// �滻���
		if (tip.indexOf("���") >= 0) 
			tip=tip.replace("���", "��"+stNode.name+" ��");

		// �滻�յ�
		if (tip.indexOf("�����յ�վ") >= 0) 
			tip=tip.replace("�����յ�վ", "����"+"��"+enNode.name+" ��");
		else if (tip.indexOf("�����յ�") >= 0) 
			tip=tip.replace("�����յ�", "����"+"��"+enNode.name+" ��");
		
		// ���滻�����ģ���ο�SDK��RouteOverlay��setData������Դ��
//		if (tip.indexOf("����") >= 0) {
//			tip.replace("����", "����<b>");
//			if (tip.indexOf("��") >= 0)
//				tip.replace("��", "��</b>");
//		}
//		if (tip.indexOf("����") >= 0) {
//			tip.replace("����", "����<b>");
//			if (tip.indexOf("����") >= 0)
//				tip.replaceAll("����", "����<b>");
//			if (tip.indexOf("վ") >= 0)
//				tip.replaceAll("վ", "վ</b>");
//		}
//		if (tip.indexOf("����") >= 0) {
//			tip.replace("����", "����<b>");
//			if (tip.indexOf("·") >= 0)
//				tip.replace("·", "·</b>");
//		}
		return tip;
	}

	public int getLogo(int i,int j,String content){
		// TODO Auto-generated method stub
		if(content.indexOf("����")>=0)
			return R.drawable.baidu_map_nav_ic_route_list_start_point;
		else if(content.indexOf("��ת")>=0)
			return R.drawable.baidu_map_nav_turn_right_s;
		else if(content.indexOf("��ת")>=0)
			return R.drawable.baidu_map_nav_turn_left_s;
		else if(content.indexOf("��ͷ")>=0)
			return R.drawable.baidu_map_nav_turn_back_s;
		else if(content.indexOf("��ǰ��ת")>=0)
			return R.drawable.baidu_map_nav_turn_right_front_s;
		else if(content.indexOf("��ǰ��ת")>=0)
			return R.drawable.baidu_map_nav_turn_left_front_s;
		else if(content.indexOf("�Һ�ת")>=0)
			return R.drawable.baidu_map_nav_turn_right_back_s;
		else if(content.indexOf("���ת")>=0)
			return R.drawable.baidu_map_nav_turn_left_back_s;
		else if(content.indexOf("����")>=0)
			return R.drawable.baidu_map_nav_turn_right_side_s;
		else if(content.indexOf("���м�")>=0)
			return R.drawable.baidu_map_nav_turn_branch_center_s;
		else if(content.indexOf("����")>=0)
			return R.drawable.baidu_map_nav_turn_left_side_s;
		else if(content.indexOf("����")>=0)
			return R.drawable.baidu_map_nav_turn_ring_s;
		else if(content.indexOf("����")>=0||content.indexOf("ֱ")>=0||content.indexOf("��")>=0)
			return R.drawable.baidu_map_nav_turn_front_s;
		else if(content.indexOf("�����յ�")>=0)
			return R.drawable.baidu_map_nav_ic_route_list_end_point;
		return R.drawable.baidu_map_icon_geo;
	}
	
}
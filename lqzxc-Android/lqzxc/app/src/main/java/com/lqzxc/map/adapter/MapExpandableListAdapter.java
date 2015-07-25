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
 * 自定义 ExpandableListAdapter
 * @author QQ472950043
 **/  
public class MapExpandableListAdapter extends BaseExpandableListAdapter {

	Context mContext;
	AppContext mAppContext;
	LayoutInflater mInflater;
	MKPlanNode stNode;//起点
	MKPlanNode enNode;//终点
	String[] plan_index;
	String[] plan_name;
	String[] plan_cost;
	int[][] line_logo;
	String[][] line_content;
	ArrayList<HashMap<String, String>> transitResults;//公交线路列表
	HashMap<String,String> map;
	
    //重写ExpandableListAdapter中的各个方法
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
		//准备列表数据
		stNode = transitRouteResult.getStart();// 起点
		enNode = transitRouteResult.getEnd();// 终点
		int numPlan = transitRouteResult.getNumPlan();// 方案数
		plan_index = new String[numPlan];// 父节点:方案索引
		plan_name = new String[numPlan];// 父节点:方案名
		plan_cost = new String[numPlan];// 父节点:方案代价
		line_logo = new int[numPlan][];// 子节点:路径节点图片
		line_content = new String[numPlan][];// 子节点:路径节点描述
		// 遍历所有方案
		for (int i = 0; i < numPlan; i++) {
			// 得到第i个方案
			MKTransitRoutePlan plan = transitRouteResult.getPlan(i);
			// 父节点
			// 设置方案索引文本
			plan_index[i] = getPlanIndex(i);
			// 设置方案名
			plan_name[i] = plan.getContent().replaceAll("_", "→");
			// 设置方案代价
			plan_cost[i] = getPlanCost(plan.getTime(), plan.getDistance());
	        
			//准备该父节点下子节点的数据
			initTransitResults(plan, plan.getNumLines(), plan.getNumRoute());
			int num = plan.getNumRoute() + plan.getNumLines();
			line_logo[i]=new int[num];
			line_content[i]=new String[num];
			//遍历该父节点下的num个子节点
			for(int j=0;j<num;j++){
				if(transitResults.get(j).get("type").equals("2")) {//步行
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
	 * 父节点:格式化该方案索引
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
	 * 父节点:格式化该方案代价
	 * @param time
	 * @param distance
	 * @return 时间/距离（最多保留一位小数）
	 * @author QQ472950043
	 */
	public String getPlanCost(int time, int distance) {
		// TODO Auto-generated method stub
		String cost = "";
		if (time < 3600) {
			cost = cost + time / 60 + "分";
		} else {
			cost = cost + time / 3600 + "小时" + time % 3600 / 60 + "分";
		}
		cost = cost + "/";
		if (distance < 1000) {
			cost = cost + distance + "米";
		} else {
			cost = cost + String.format("%.1f公里", distance * 1.0 / 1000);
		}
		return cost;
	}

	/**
	 * 子节点:公交路线数据初始化
	 * @param plan
	 * @param linenum
	 * @param routenum
	 * @author QQ472950043
	 */
	public void initTransitResults(MKTransitRoutePlan plan, int linenum, int routenum) {
		System.out.println("排序前:");
		transitResults = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < linenum; i++) {
			map = new HashMap<String, String>();
			map.put("type", "" + plan.getLine(i).getType());// 第i条MKLine
			map.put("tip", plan.getLine(i).getTip());// 类型，1表示公交或是地铁线路
			// 方案中公交车路线line
			transitResults.add(map);
			System.out.println(plan.getLine(i).getType() + " " + plan.getLine(i).getTip());
		}
		for (int i = 0; i < routenum; i++) {
			map = new HashMap<String, String>();
			map.put("type", "" + plan.getRoute(i).getRouteType());// 第i条MKLine
			map.put("tip", plan.getRoute(i).getTip());// 类型，1表示公交或是地铁线路
			// 步行路线在方案中的索引位置，按照plan.getRoute(i).getIndex()提示的位置插入到transitResults中
			transitResults.add(plan.getRoute(i).getIndex() + 1 + i, map);
			System.out.println(plan.getRoute(i).getRouteType() + " " + plan.getRoute(i).getTip());
		}
		System.out.println("排序后：");
		for (int i = 0; i < transitResults.size(); i++) {
			System.out.println("" + transitResults.get(i).get("type") + " " + transitResults.get(i).get("tip"));
		}
	}

	/**
	 * 驾车、步行使用的数据结构相同，因此类型为驾车或步行，节点浏览方法相同
	 * @param route
	 * @param stNode
	 * @param enNode
	 * @author QQ472950043
	 */
	public void setData() {
        // TODO Auto-generated method stub
		// 准备列表数据
		int numPlan = mAppContext.routeResults.size();
		plan_index=new String[numPlan];
		plan_name=new String[numPlan];
		plan_cost=new String[numPlan];
		line_logo=new int[numPlan][];
		line_content=new String[numPlan][];
		for(int i=0;i<numPlan;i++){
			stNode = mAppContext.routeResults.get(i).getStart();
			enNode = mAppContext.routeResults.get(i).getEnd();
			// 得到第i个方案
			RouteResult routeResult = mAppContext.routeResults.get(i);
			// 父节点
			// 设置方案索引文本
			plan_index[i] = getPlanIndex(i);
			// 设置方案名
			plan_name[i] = routeResult.getRoute().getTip().replaceAll("_", "→");
			// 设置方案代价
			plan_cost[i] = getPlanCost(routeResult.getTime(), routeResult.getDistance());

			// 驾车和步行的数据结构是 MKStep类型route.get(i).getStep(nodeIndex)
			MKRoute route = routeResult.getRoute();
			int numStep = route.getNumSteps();
			line_logo[i] = new int[numStep];
			line_content[i] = new String[numStep];
			// 从起点出发，共有route.get(i).getNumSteps()步
			for (int j = 0; j < numStep; j++) {
				MKStep step = route.getStep(j);
				// 设置子节点的文字
				String content = step.getContent();
				line_content[i][j] = formatTip(content);
				//设置子节点的图标
				line_logo[i][j]=getLogo(i,j,content);
				
			}
		}
		notifyDataSetChanged();
	}

	/**
	 * 格式化Tip文字信息
	 */
	public String formatTip(String tip) {
		// 替换起点
		if (tip.indexOf("起点") >= 0) 
			tip=tip.replace("起点", "【"+stNode.name+" 】");

		// 替换终点
		if (tip.indexOf("到达终点站") >= 0) 
			tip=tip.replace("到达终点站", "到达"+"【"+enNode.name+" 】");
		else if (tip.indexOf("到达终点") >= 0) 
			tip=tip.replace("到达终点", "到达"+"【"+enNode.name+" 】");
		
		// 想替换其他的，请参考SDK的RouteOverlay的setData方法的源码
//		if (tip.indexOf("步行") >= 0) {
//			tip.replace("步行", "步行<b>");
//			if (tip.indexOf("米") >= 0)
//				tip.replace("米", "米</b>");
//		}
//		if (tip.indexOf("到达") >= 0) {
//			tip.replace("到达", "到达<b>");
//			if (tip.indexOf("经过") >= 0)
//				tip.replaceAll("经过", "经过<b>");
//			if (tip.indexOf("站") >= 0)
//				tip.replaceAll("站", "站</b>");
//		}
//		if (tip.indexOf("乘坐") >= 0) {
//			tip.replace("乘坐", "乘坐<b>");
//			if (tip.indexOf("路") >= 0)
//				tip.replace("路", "路</b>");
//		}
		return tip;
	}

	public int getLogo(int i,int j,String content){
		// TODO Auto-generated method stub
		if(content.indexOf("出发")>=0)
			return R.drawable.baidu_map_nav_ic_route_list_start_point;
		else if(content.indexOf("右转")>=0)
			return R.drawable.baidu_map_nav_turn_right_s;
		else if(content.indexOf("左转")>=0)
			return R.drawable.baidu_map_nav_turn_left_s;
		else if(content.indexOf("调头")>=0)
			return R.drawable.baidu_map_nav_turn_back_s;
		else if(content.indexOf("右前方转")>=0)
			return R.drawable.baidu_map_nav_turn_right_front_s;
		else if(content.indexOf("左前方转")>=0)
			return R.drawable.baidu_map_nav_turn_left_front_s;
		else if(content.indexOf("右后方转")>=0)
			return R.drawable.baidu_map_nav_turn_right_back_s;
		else if(content.indexOf("左后方转")>=0)
			return R.drawable.baidu_map_nav_turn_left_back_s;
		else if(content.indexOf("靠右")>=0)
			return R.drawable.baidu_map_nav_turn_right_side_s;
		else if(content.indexOf("靠中间")>=0)
			return R.drawable.baidu_map_nav_turn_branch_center_s;
		else if(content.indexOf("靠左")>=0)
			return R.drawable.baidu_map_nav_turn_left_side_s;
		else if(content.indexOf("环岛")>=0)
			return R.drawable.baidu_map_nav_turn_ring_s;
		else if(content.indexOf("继续")>=0||content.indexOf("直")>=0||content.indexOf("沿")>=0)
			return R.drawable.baidu_map_nav_turn_front_s;
		else if(content.indexOf("到达终点")>=0)
			return R.drawable.baidu_map_nav_ic_route_list_end_point;
		return R.drawable.baidu_map_icon_geo;
	}
	
}
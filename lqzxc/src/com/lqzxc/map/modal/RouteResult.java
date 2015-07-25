package com.lqzxc.map.modal;

import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKRouteAddrResult;
/**
 * 自定义MKRoute驾车/步行/公交路线数据的结构
 * @author QQ472950043
 **/
public class RouteResult{
	
	MKRouteAddrResult addrResult;// 起点或终点的地址信息结果
	int numPlan;// 方案数目
	MKRoute route;// 线路
	MKPlanNode start; // 起点
	MKPlanNode end;// 终点
	int time;// 方案总距离
	int distance;// 路线预计花费时间
	int taxiPrice;// 打车费用参考值
	
	public MKRouteAddrResult getAddrResult() {
		return addrResult;
	}
	public void setAddrResult(MKRouteAddrResult addrResult) {
		this.addrResult = addrResult;
	}
	public int getNumPlan() {
		return numPlan;
	}
	public void setNumPlan(int numPlan) {
		this.numPlan = numPlan;
	}
	public MKRoute getRoute() {
		return route;
	}
	public void setRoute(MKRoute route) {
		this.route = route;
	}
	public MKPlanNode getStart() {
		return start;
	}
	public void setStart(MKPlanNode start) {
		this.start = start;
	}
	public MKPlanNode getEnd() {
		return end;
	}
	public void setEnd(MKPlanNode end) {
		this.end = end;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTaxiPrice() {
		return taxiPrice;
	}
	public void setTaxiPrice(int taxiPrice) {
		this.taxiPrice = taxiPrice;
	}
	
}
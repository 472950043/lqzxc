package com.lqzxc.map.modal;

import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKRouteAddrResult;
/**
 * �Զ���MKRoute�ݳ�/����/����·�����ݵĽṹ
 * @author QQ472950043
 **/
public class RouteResult{
	
	MKRouteAddrResult addrResult;// �����յ�ĵ�ַ��Ϣ���
	int numPlan;// ������Ŀ
	MKRoute route;// ��·
	MKPlanNode start; // ���
	MKPlanNode end;// �յ�
	int time;// �����ܾ���
	int distance;// ·��Ԥ�ƻ���ʱ��
	int taxiPrice;// �򳵷��òο�ֵ
	
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
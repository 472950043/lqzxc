package com.lqzxc.modal;

import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * �Զ���վ�����ݽṹ
 * @author QQ472950043
 **/
public class BikeStie{
	int id;// ˳���(�ⲿ���)
	String mid;// ������(�ڲ����)
	String gpsx;// ����
	String gpsy;// γ��
	GeoPoint mGeoPoint;// ��γ�ȱ�ʶ
	String netName;// ������
	String netType;// ��������
	String netStatus;// ����״̬
	String netLevel;// ����ȼ�
	String address;// ��ַ
	String trafficInfo;// �ܱ߹�����Ϣ
	int bicycleCapacity;// ����
	int bicycleNum;// ���
	String imageAttach;// ͼƬ���·��
	int visited;// ������
	String time;// ����ʱ��

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * ����
	 */
	public String getGpsx() {
		return gpsx;
	}
	/**
	 * ����
	 */
	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}
	/**
	 * γ��
	 */
	public String getGpsy() {
		return gpsy;
	}
	/**
	 * γ��
	 */
	public void setGpsy(String gpsy) {
		this.gpsy = gpsy;
	}
	public GeoPoint getmGeoPoint() {
		return mGeoPoint;
	}
	public void setmGeoPoint(GeoPoint mGeoPoint) {
		this.mGeoPoint = mGeoPoint;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getNetStatus() {
		return netStatus;
	}
	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}
	public String getNetLevel() {
		return netLevel;
	}
	public void setNetLevel(String netLevel) {
		this.netLevel = netLevel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTrafficInfo() {
		return trafficInfo;
	}
	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}
	public int getBicycleCapacity() {
		return bicycleCapacity;
	}
	public void setBicycleCapacity(int bicycleCapacity) {
		this.bicycleCapacity = bicycleCapacity;
	}
	public int getBicycleNum() {
		return bicycleNum;
	}
	public void setBicycleNum(int bicycleNum) {
		this.bicycleNum = bicycleNum;
	}
	public String getImageAttach() {
		return imageAttach;
	}
	public void setImageAttach(String imageAttach) {
		this.imageAttach = imageAttach;
	}
	public int getVisited() {
		return visited;
	}
	public void setVisited(int visited) {
		this.visited = visited;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}

package com.lqzxc.modal;

import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 自定义站点数据结构
 * @author QQ472950043
 **/
public class BikeStie{
	int id;// 顺序号(外部编号)
	String mid;// 网点编号(内部编号)
	String gpsx;// 经度
	String gpsy;// 纬度
	GeoPoint mGeoPoint;// 经纬度标识
	String netName;// 网点名
	String netType;// 网点类型
	String netStatus;// 网点状态
	String netLevel;// 网点等级
	String address;// 地址
	String trafficInfo;// 周边公交信息
	int bicycleCapacity;// 总数
	int bicycleNum;// 库存
	String imageAttach;// 图片相对路径
	int visited;// 访问数
	String time;// 访问时间

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
	 * 经度
	 */
	public String getGpsx() {
		return gpsx;
	}
	/**
	 * 经度
	 */
	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}
	/**
	 * 纬度
	 */
	public String getGpsy() {
		return gpsy;
	}
	/**
	 * 纬度
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

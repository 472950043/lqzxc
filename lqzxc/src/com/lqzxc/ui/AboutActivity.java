package com.lqzxc.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 关于界面
 * @author QQ472950043
 **/
public class AboutActivity extends Activity{

	AppContext mAppContext;
	TextView textView_title_back;
	TextView textView_title;
	TextView textView_title_right;
	LinearLayout btn_back;
	LinearLayout btn_right;
	
	LinearLayout btn_version;
	LinearLayout btn_feedback;
	TextView textView_version;
	ImageView imageView_update;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
		mAppContext = (AppContext) this.getApplicationContext();
		textView_title_back = (TextView) findViewById(R.id.textview_widget_title_back);
		textView_title = (TextView) findViewById(R.id.textview_widget_title);
		textView_title_right = (TextView) findViewById(R.id.textview_widget_title_right);
		btn_back = (LinearLayout) findViewById(R.id.btn_widget_title_back);
		btn_right = (LinearLayout) findViewById(R.id.btn_widget_title_right);
		textView_title.setText("关于");
		textView_title_back.setText("返回");
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btn_right.setVisibility(View.GONE);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());//年月日 时分秒 格式

		int updateCount = mAppContext.data.getInt("updateCount", 0);
		int updateVisit = mAppContext.data.getInt("updateVisit", 0);
		int updateFlow = mAppContext.data.getInt("updateFlow", 0);
		String updateTime = mAppContext.data.getString("updateTime", "" + System.currentTimeMillis());
		updateTime=df.format(new Date(Long.parseLong(updateTime)));
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(Html.fromHtml("" +
			"公共自行车服务系统是公共交通和文明交通的组成部分，是城市公交系统的辅助和补充，解决了城市“最后一公里”难题，符合“公交优先”战略，对于缓解交通压力、低碳出行和提升城市形象等具有重要意义。" +
			"请您遵守社会公德和相关法律，爱惜公共自行车和其他公共设施，蓄意破坏、偷窃等行为不仅会受到强烈的舆论谴责，而且可能会受到法律的严惩。" +
			
			"<h3><font color=\"#FF3300\">免责声明</font></h3>" +
			"在使用路桥自行车（以下简称本软件）前，请您务必仔细阅读并透彻理解本声明。您可以选择不使用本软件，但如果您使用，您的使用行为将被视作已无条件接受本声明所涉全部内容。<br><br>" +
			"<h5><font color=\"#FF3300\">1.合法性</font></h5>" +
			"本软件的的设计目的是方便用户使用路桥公共自行车出行，无意侵犯任何组织和单位的合法权益。" +
			"本软件将为用户提供永久的非营利服务，因此不接受任何商业广告植入，并禁止任何人将本软件用于营利用途。" +
			"本软件的著作权为QQ472950043（以下简称作者）所有，如果您对本软件的合法性存有质疑或者以学习为目的，请联系作者索要全部的源代码。" +
			"<h5><font color=\"#FF3300\">2.安全性</font></h5>" +
			"作者保证未经您的允许不会获取和公布任何隐私信息，但不保数据的正确性、及时性，本软件所有实时数据均来源于路桥公共自行车官方网站。" +
			"如果实时数据错误，请您致电官方服务热线，您的反馈将使更多用户受益！请通过百度应用、91助手、豌豆荚等第三方手机应用商店（通过杀毒检测）" +
			"下载本软件，为了您的手机安全请勿通过其他非可信渠道获取。如果本软件的使用过程中发生卡死、强退、实时数据错误等并不会影响您手机的其他功能，如有发生，请确认您使用的为最新版本。" +
			"作者不承担此上意外任何责任，并保留更改此声明的权力。当然如果您对本软件有意见或建议，请通过下方的按钮给作者留言。" +

			"<h3><font color=\"#FF3300\">常见问题及解答</font></h3>" +
			
			"<h5><font color=\"#FF3300\">1、借车：</font></h5>" +
			"1、将借车卡斜45度角或平放于公共自行车锁住感应区上刷卡。 <br>" +
			"2、听到“喀”的一声并在语音提示“请取车”后，将车拉出即借车成功。<br> " +
			"3、一张借车卡，只能借一辆公共自行车，市民卡、公交卡也不能用于借车，反之借车卡也不能用来乘公交车。 <br> " +
			"4、路桥区的借车卡目前只能租借路桥区的自行车，椒江、黄岩、温岭的公共自行车属于不同公司、不同租赁系统，三区公共自行车暂不能通借通还。" +
			
			"<h5><font color=\"#FF3300\">2、还车：</font></h5>" +
			"1、先将公共自行车推入锁止器。<br> " +
			"2、将借车卡斜45度角或平放于公共自行车锁住感应区，听到语音提示“还车成功”后，将卡收回，即还车成功。<br> " +
			"3、还车未刷卡会造成借车卡暂停使用，第二次无法正常借车。<br> " +
			"4、若还车后忘记刷卡，可在还车十分钟后至任意网点的锁柱上，刷卡进行修卡申请，锁柱提示“修卡，请稍候刷卡”" +
			"（此时只刷卡一次便可，不要至每个锁柱上一一刷卡）。在等待修卡申请后1-3分钟左右，在原修卡申请的锁柱上再次刷卡进行修卡确认，提示“修卡成功”，该卡便可再次使用。" +
			
			"<h5><font color=\"#FF3300\">3、无法借车处理方法:</font></h5>" +
			"1、刷卡太快或刷卡时未放好。请将卡放置在感应区域内，待语音提示“请取车”后取车。<br> " +
			"2、自行车未停好。将自行车扶正后，往前轻推。刷卡取车。<br>" +
			"3、该借车卡已借用公共自行车，仍未还车。请还好车后，再借车。 <br>" +
			"4、借车刷卡时，未有语音提示或指示灯未亮，请更换锁柱刷卡借车。<br> " +
			"5、卡内余额不足，请充值后再借车。" +
			
			"<h5><font color=\"#FF3300\">4、无法还车的处理方法:</font></h5>" +
			"1、还车时无任何锁车工作，可更换锁住还车。<br> " +
			"2、也可拨打热线，维护人员将到现场解决。" +
			
			"<h5><font color=\"#FF3300\">5、办卡窗口地址及受理时间：</font></h5>" +
			"第一窗口：（接受团体业务及个人业务）<br>" +
			"文化路2号建设局门口管理亭（建设局网点）<br>" +
			"第二窗口：（仅接受个人业务）<br>" +
			"富仕广场南侧南官苑管理亭（富仕广场网点）<br>" +
			"办卡时间：<br>" +
			"上午8:30~11:30下午14:00~18:00(夏令时)<br>" +
			"上午8:30~11:30下午13:00~17:00(冬令时)<br>" +
			"具体以管理亭上的公告为准。办卡请携带身份证复印件及足够的现金，管理亭不提供复印和刷卡服务。" +
			
			"<h5><font color=\"#FF3300\">6、办卡注意事项：</font></h5>" +
			"1、年龄在12周岁至70周岁（16周岁以下须由监护人陪同方可办卡）。<br>" +
			"2、本地居民凭二代身份证原件及复印件或户口本等有效证件原件及复印件办理，非本地居民凭有效暂住证原件及复印件与二代身份证原件及复印件。（如代办则需出示办卡人及代办人身份证及复印件）<br>" +
			"3、拥有11位能联系到申请人的手机号。<br>" +
			"4、申请人需缴纳350元（自行车保证金300元和违约保证金50元），有低保证明可免缴纳保证金人民币300元（办卡时须出示低保证明及复印件）。用户的收款收据及协议“红色联”做为收取保证金凭证及退卡依据，请妥善保管。<br>" +
			"5、借车卡初始必须缴纳违约保证金。若卡内违约保证金金额为零，则无法借用车辆。" +
			
			"<h5><font color=\"#FF3300\">7、遗失补卡：</font></h5>" +
			"1、用户需致电官方客服电话进行卡挂失并确保租借的公共租赁自行车已归还。<br>" +
			"2、用户在挂失的三天后，携带本人身份证（如代办则须出示补卡人及代办人身份证）到窗口支付10元补卡工本费并签名领取新补办的“借车卡”。（补办新卡后，原卡信息及余额自动转移到新卡账户上）。" +
			
			"<h5><font color=\"#FF3300\">8、换卡：</font></h5>" +
			"借车卡因持卡人弯折、烘烤、打孔、浸水、遗失等人为原因导致不能正常使用或卡面严重磨损的，确保租借的公共自行车已归还后，用户须在最后一次使用租赁车的三日后，携带本人身份证（如代办则须出示补卡人及代办人身份证）及10元换卡费至窗口进行换卡。非以上因素的，换卡免费。<br>" +
			
			"<h5><font color=\"#FF3300\">9、退卡：</font></h5>" +
			"1、“借车卡”办卡满一个月后并确保该卡所租借的公共租赁自行车已归还的用户方可办理退卡手续。<br>" +
			"2、用户须携带收取保证金凭证（协议中“红色客户联”及收款收据第二联）和“借车卡”以及本人身份证原件到窗口办理退卡领取保证金手续。如代退，则须提供退卡人及代办人身份证手机号且留下复印件。<br>" +
			"3、如用户遗失收款收据，则当场不办理退卡，用户登记资料并等待工作人员通知方可办理退卡。<br>" +
			"4、如用户遗失“借车卡”的, 按“补卡流程”处理后方可退卡。" +
			
			"<h5><font color=\"#FF3300\">10、损坏、遗失赔偿：</font></h5>" +
			"1、自行车损坏或遗失后，请到办卡窗口，办理损坏、遗失赔偿手续。<br>" +
			"2、具体赔偿按《台州市路桥区公共自行车损坏、遗失赔偿标准》实行。" +
			
			"<h5><font color=\"#FF3300\">11、收费标准：</font></h5>" +
			"1、为提高使用效率，减少恶意占用车辆。公共自行车实行：<br>" +
			"单次借还车在2个小时以内为免费（含2小时）；<br>" +
			"单次借还车时长超过2小时则收取违约保证金1元/小时；<br>" +
			"单次借还车时长超过24小时后则收取违约保证金3元/小时。<br>" +
			"如超过1个月未还车则视同在使用过程中将车辆丢失须按赔偿标准进行赔偿。<br>" +
			"2、租金在卡上直接扣除，当余额不足时，卡自动停用，充值后可继续使用。" +

			"<h3><font color=\"#FF3300\">企业档案</font></h3>" +
			"企业名称：台州市路桥区公共自行车发展有限公司<br>" +
			"企业性质：国有企业<br>" +
			"业务管理：隶属于台州市住房和城乡建设规划局路桥分局<br>" +
			"经营范围：自行车租赁、管理；智能交通系统应用和技术管理；智能IC卡应用管理；设计、制作、代理、发布国内各类广告；广告位租赁。<br>" +
			"官方网站：http://www.luqiaobike.com <br>" +
			"法人代表：蔡敦强（总经理）<br>" +
			"成立日期：2013年5月20日<br>" +
			"公司地址：<br>" +
			"台州市路桥区文化路2号（博爱医院东北面）<br>" +
			"（门口有租车点和管理亭）政府办事中心一楼大厅进门右拐<br>" +
			"服务时间：<br>" +
			"上午8:30~11:30下午14:00~18:00(夏令时)<br>" +
			"上午8:30~11:30下午13:00~17:00(冬令时)<br>" +
			"服务热线：<br>" +
			"0576-80290112<br>" +
			"400-820-1898（拨打仅支付当地市话费）<br>" +
			"信息发布：<br>" +
			"路桥区人力资源和社会保障局网（招聘信息）<br>" +
			"http://lqrs.luqiao.gov.cn <br>" +
			"台州市住房和城乡建设规划局路桥分局（公告）<br>" +
			"http://jianshe.luqiao.gov.cn <br>" +

			"<h3><font color=\"#FF3300\">其他相关</font></h3>" +
			"1、2013年9月22日，由路桥区公共自行车发展有限公司、路桥区农村合作银行主办的路桥公共自行车启动仪式在路桥永安广场举行。" +
			"区人大常委会副主任高萍宣布启动，副区长管秉阳致辞，区政协副主席郭栋材、区住建分局局长蒋新才、区农村信用社董事长金时江、上海永久自行车公司总经理朱佳瑜等领导参加仪式。" +
			"一期工程建成锁车器1,566个，路桥区农村合作银行捐献自行车1,300辆，网点主要分布在路桥街道、路南街道和路北街道的住宅区、商场等人流量较大的地方。<br>" +
			"2、2013年12月09日，路桥公共自行车街心公园网点遭到恶意破坏，有三辆公共自行车被暴力扭曲变形无法使用，" +
			"另外应的三个锁车器也不同程度受损。公共自行车频频遭到恶意损坏，激起了广大市民对此丧失社会公德行为的强烈义愤。" +
			"如果发现故意损坏自行车的不文明现象，可拨打80290112进行举报。对于严重损坏公共自行车的，市民可以拨打110报警，将由公安部门介入，" +
			"根据《中华人民共和国治安管理处罚法》的有关规定进行处罚。<br>" +
			"3、截止2014年01月05日，路桥公共自行车办卡量已达到10350张，每天租用公共自行车约有4500人次。公共自行车已成了路桥人出行的主要交通工具之一。" +
			"投资1100万元的路桥公共自行车二期工程已经开建，二期建成后，路桥的公共自行车将新增81个网点，达到134个，投入公共自行车共4000辆。" +
			"该项目总共分三期实施，总投资估算为28866.58万元，其中一期建设费用365.438万元，5年运营费用为564.864万元。" +
			
			"<h5><font color=\"#FF3300\">数据统计</font></h5>" +
			"1、本软件的数据均来源于网络<br>" +
			"2、每次打开软件默认自动同步一次<br>" +
			"3、每次同步最新数据大约会消耗130KB的流量<br>" +
			"4、自行车网点图片优先加载本地缓存，缓存路径:SD卡/Android/data/com.lqzxc<br>" +
			"5、如果您只想查看自行车网点分布请使用离线地图功能<br>" +
			"6、开启GPS定位可以更精确地定位您的位置<br><br>" +
			"同步成功总次数：" +updateCount+"<br>" +
			"查询总次数：" +updateVisit+"<br>" +
			"数据流量总计：" +mAppContext.formatDataSize(updateFlow)+"（不计地图、图片、WIFI）<br>" +
			"最近更新时间：" +updateTime+"<br>" +
			"网点总数:" +mAppContext.mBikeSties.size()+"<br>" +
			"当前锁车器总量:"+mAppContext.totalCapacity+"<br>" +
			"当前可借总量:"+mAppContext.totalNum+""));
		btn_version = (LinearLayout) findViewById(R.id.btn_setting_version);
		btn_feedback = (LinearLayout) findViewById(R.id.btn_setting_feedback);
		btn_version.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//	    		MobclickAgent.onEvent(AboutActivity.this,"setting_version");
				//statusCode 更新回调接口返回状态： 0 有更新 ;1 没有更新; 2 非wifi状态; 3 超时;
				//updateInfo 更新回调返回数据，包涵App更新信息，属性: updateLog 更新日志 ; version 最新版本 ; path 最新版下载链接
//				UmengUpdateAgent.update(AboutActivity.this);
				mAppContext.showLoading(AboutActivity.this);
				btn_version.setClickable(false);
				UmengUpdateAgent.forceUpdate(AboutActivity.this);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				        @Override
				        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
				        	mAppContext.hideLoading();
							btn_version.setClickable(true);
				            switch (updateStatus) {
				            case 0: // has update
				                UmengUpdateAgent.showUpdateDialog(AboutActivity.this, updateInfo);
				                break;
				            case 1: // has no update
				            	try {
				            		mAppContext.ToastMessage(AboutActivity.this,"当前版本V" + 
				            				getPackageManager().getPackageInfo("com.lqzxc", 0).versionName + "，已经是最新版");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				                break;
				            case 2: // none wifi
				            	mAppContext.ToastMessage(AboutActivity.this, "没有wifi连接， 只在wifi下更新");
				                break;
				            case 3: // time out
				            	mAppContext.ToastMessage(AboutActivity.this, "超时");
				                break;
				            }
				        }
				});
			}
		});
		btn_feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
			    agent.startFeedbackActivity();
			}
		});
		textView_version = (TextView) findViewById(R.id.textView_setting_version);
		textView_version.setText("");
		imageView_update = (ImageView) findViewById(R.id.imageview_setting_update);
//		imageView_update.setVisibility(View.GONE);
    }
	
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		System.out.println("AboutActivity onResume");
	}

	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		System.out.println("AboutActivity onPause");
	}
}

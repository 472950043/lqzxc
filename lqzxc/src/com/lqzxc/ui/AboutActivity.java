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
 * ���ڽ���
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
		textView_title.setText("����");
		textView_title_back.setText("����");
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btn_right.setVisibility(View.GONE);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss", Locale.getDefault());//������ ʱ���� ��ʽ

		int updateCount = mAppContext.data.getInt("updateCount", 0);
		int updateVisit = mAppContext.data.getInt("updateVisit", 0);
		int updateFlow = mAppContext.data.getInt("updateFlow", 0);
		String updateTime = mAppContext.data.getString("updateTime", "" + System.currentTimeMillis());
		updateTime=df.format(new Date(Long.parseLong(updateTime)));
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(Html.fromHtml("" +
			"�������г�����ϵͳ�ǹ�����ͨ��������ͨ����ɲ��֣��ǳ��й���ϵͳ�ĸ����Ͳ��䣬����˳��С����һ������⣬���ϡ��������ȡ�ս�ԣ����ڻ��⽻ͨѹ������̼���к�������������Ⱦ�����Ҫ���塣" +
			"����������ṫ�º���ط��ɣ���ϧ�������г�������������ʩ�������ƻ���͵�Ե���Ϊ�������ܵ�ǿ�ҵ�����Ǵ�𣬶��ҿ��ܻ��ܵ����ɵ��ϳ͡�" +
			
			"<h3><font color=\"#FF3300\">��������</font></h3>" +
			"��ʹ��·�����г������¼�Ʊ������ǰ�����������ϸ�Ķ���͸����Ȿ������������ѡ��ʹ�ñ�������������ʹ�ã�����ʹ����Ϊ�������������������ܱ���������ȫ�����ݡ�<br><br>" +
			"<h5><font color=\"#FF3300\">1.�Ϸ���</font></h5>" +
			"������ĵ����Ŀ���Ƿ����û�ʹ��·�Ź������г����У������ַ��κ���֯�͵�λ�ĺϷ�Ȩ�档" +
			"�������Ϊ�û��ṩ���õķ�Ӫ��������˲������κ���ҵ���ֲ�룬����ֹ�κ��˽����������Ӫ����;��" +
			"�����������ȨΪQQ472950043�����¼�����ߣ����У�������Ա�����ĺϷ��Դ������ɻ�����ѧϰΪĿ�ģ�����ϵ������Ҫȫ����Դ���롣" +
			"<h5><font color=\"#FF3300\">2.��ȫ��</font></h5>" +
			"���߱�֤δ�������������ȡ�͹����κ���˽��Ϣ�����������ݵ���ȷ�ԡ���ʱ�ԣ����������ʵʱ���ݾ���Դ��·�Ź������г��ٷ���վ��" +
			"���ʵʱ���ݴ��������µ�ٷ��������ߣ����ķ�����ʹ�����û����棡��ͨ���ٶ�Ӧ�á�91���֡��㶹�Եȵ������ֻ�Ӧ���̵꣨ͨ��ɱ����⣩" +
			"���ر������Ϊ�������ֻ���ȫ����ͨ�������ǿ���������ȡ������������ʹ�ù����з���������ǿ�ˡ�ʵʱ���ݴ���Ȳ�����Ӱ�����ֻ����������ܣ����з�������ȷ����ʹ�õ�Ϊ���°汾��" +
			"���߲��е����������κ����Σ����������Ĵ�������Ȩ������Ȼ������Ա������������飬��ͨ���·��İ�ť���������ԡ�" +

			"<h3><font color=\"#FF3300\">�������⼰���</font></h3>" +
			
			"<h5><font color=\"#FF3300\">1���賵��</font></h5>" +
			"1�����賵��б45�Ƚǻ�ƽ���ڹ������г���ס��Ӧ����ˢ���� <br>" +
			"2��������������һ������������ʾ����ȡ�����󣬽����������賵�ɹ���<br> " +
			"3��һ�Ž賵����ֻ�ܽ�һ���������г������񿨡�������Ҳ�������ڽ賵����֮�賵��Ҳ���������˹������� <br> " +
			"4��·�����Ľ賵��Ŀǰֻ�����·���������г������������ҡ�����Ĺ������г����ڲ�ͬ��˾����ͬ����ϵͳ�������������г��ݲ���ͨ��ͨ����" +
			
			"<h5><font color=\"#FF3300\">2��������</font></h5>" +
			"1���Ƚ��������г�������ֹ����<br> " +
			"2�����賵��б45�Ƚǻ�ƽ���ڹ������г���ס��Ӧ��������������ʾ�������ɹ����󣬽����ջأ��������ɹ���<br> " +
			"3������δˢ������ɽ賵����ͣʹ�ã��ڶ����޷������賵��<br> " +
			"4��������������ˢ�������ڻ���ʮ���Ӻ�����������������ϣ�ˢ�������޿����룬������ʾ���޿������Ժ�ˢ����" +
			"����ʱֻˢ��һ�α�ɣ���Ҫ��ÿ��������һһˢ�������ڵȴ��޿������1-3�������ң���ԭ�޿�������������ٴ�ˢ�������޿�ȷ�ϣ���ʾ���޿��ɹ������ÿ�����ٴ�ʹ�á�" +
			
			"<h5><font color=\"#FF3300\">3���޷��賵������:</font></h5>" +
			"1��ˢ��̫���ˢ��ʱδ�źá��뽫�������ڸ�Ӧ�����ڣ���������ʾ����ȡ������ȡ����<br> " +
			"2�����г�δͣ�á������г���������ǰ���ơ�ˢ��ȡ����<br>" +
			"3���ý賵���ѽ��ù������г�����δ�������뻹�ó����ٽ賵�� <br>" +
			"4���賵ˢ��ʱ��δ��������ʾ��ָʾ��δ�������������ˢ���賵��<br> " +
			"5���������㣬���ֵ���ٽ賵��" +
			
			"<h5><font color=\"#FF3300\">4���޷������Ĵ�����:</font></h5>" +
			"1������ʱ���κ������������ɸ�����ס������<br> " +
			"2��Ҳ�ɲ������ߣ�ά����Ա�����ֳ������" +
			
			"<h5><font color=\"#FF3300\">5���쿨���ڵ�ַ������ʱ�䣺</font></h5>" +
			"��һ���ڣ�����������ҵ�񼰸���ҵ��<br>" +
			"�Ļ�·2�Ž�����ſڹ���ͤ����������㣩<br>" +
			"�ڶ����ڣ��������ܸ���ҵ��<br>" +
			"���˹㳡�ϲ��Ϲ�Է����ͤ�����˹㳡���㣩<br>" +
			"�쿨ʱ�䣺<br>" +
			"����8:30~11:30����14:00~18:00(����ʱ)<br>" +
			"����8:30~11:30����13:00~17:00(����ʱ)<br>" +
			"�����Թ���ͤ�ϵĹ���Ϊ׼���쿨��Я�����֤��ӡ�����㹻���ֽ𣬹���ͤ���ṩ��ӡ��ˢ������" +
			
			"<h5><font color=\"#FF3300\">6���쿨ע�����</font></h5>" +
			"1��������12������70���꣨16�����������ɼ໤����ͬ���ɰ쿨����<br>" +
			"2�����ؾ���ƾ�������֤ԭ������ӡ���򻧿ڱ�����Ч֤��ԭ������ӡ�������Ǳ��ؾ���ƾ��Ч��ס֤ԭ������ӡ����������֤ԭ������ӡ����������������ʾ�쿨�˼����������֤����ӡ����<br>" +
			"3��ӵ��11λ����ϵ�������˵��ֻ��š�<br>" +
			"4�������������350Ԫ�����г���֤��300Ԫ��ΥԼ��֤��50Ԫ�����еͱ�֤��������ɱ�֤�������300Ԫ���쿨ʱ���ʾ�ͱ�֤������ӡ�������û����տ��վݼ�Э�顰��ɫ������Ϊ��ȡ��֤��ƾ֤���˿����ݣ������Ʊ��ܡ�<br>" +
			"5���賵����ʼ�������ΥԼ��֤��������ΥԼ��֤����Ϊ�㣬���޷����ó�����" +
			
			"<h5><font color=\"#FF3300\">7����ʧ������</font></h5>" +
			"1���û����µ�ٷ��ͷ��绰���п���ʧ��ȷ�����Ĺ����������г��ѹ黹��<br>" +
			"2���û��ڹ�ʧ�������Я���������֤������������ʾ�����˼����������֤��������֧��10Ԫ���������Ѳ�ǩ����ȡ�²���ġ��賵�������������¿���ԭ����Ϣ������Զ�ת�Ƶ��¿��˻��ϣ���" +
			
			"<h5><font color=\"#FF3300\">8��������</font></h5>" +
			"�賵����ֿ������ۡ��濾����ס���ˮ����ʧ����Ϊԭ���²�������ʹ�û�������ĥ��ģ�ȷ�����Ĺ������г��ѹ黹���û��������һ��ʹ�����޳������պ�Я���������֤������������ʾ�����˼����������֤����10Ԫ�����������ڽ��л��������������صģ�������ѡ�<br>" +
			
			"<h5><font color=\"#FF3300\">9���˿���</font></h5>" +
			"1�����賵�����쿨��һ���º�ȷ���ÿ������Ĺ����������г��ѹ黹���û����ɰ����˿�������<br>" +
			"2���û���Я����ȡ��֤��ƾ֤��Э���С���ɫ�ͻ��������տ��վݵڶ������͡��賵�����Լ��������֤ԭ�������ڰ����˿���ȡ��֤������������ˣ������ṩ�˿��˼����������֤�ֻ��������¸�ӡ����<br>" +
			"3�����û���ʧ�տ��վݣ��򵱳��������˿����û��Ǽ����ϲ��ȴ�������Ա֪ͨ���ɰ����˿���<br>" +
			"4�����û���ʧ���賵������, �����������̡�����󷽿��˿���" +
			
			"<h5><font color=\"#FF3300\">10���𻵡���ʧ�⳥��</font></h5>" +
			"1�����г��𻵻���ʧ���뵽�쿨���ڣ������𻵡���ʧ�⳥������<br>" +
			"2�������⳥����̨����·�����������г��𻵡���ʧ�⳥��׼��ʵ�С�" +
			
			"<h5><font color=\"#FF3300\">11���շѱ�׼��</font></h5>" +
			"1��Ϊ���ʹ��Ч�ʣ����ٶ���ռ�ó������������г�ʵ�У�<br>" +
			"���ν軹����2��Сʱ����Ϊ��ѣ���2Сʱ����<br>" +
			"���ν軹��ʱ������2Сʱ����ȡΥԼ��֤��1Ԫ/Сʱ��<br>" +
			"���ν軹��ʱ������24Сʱ������ȡΥԼ��֤��3Ԫ/Сʱ��<br>" +
			"�糬��1����δ��������ͬ��ʹ�ù����н�������ʧ�밴�⳥��׼�����⳥��<br>" +
			"2������ڿ���ֱ�ӿ۳���������ʱ�����Զ�ͣ�ã���ֵ��ɼ���ʹ�á�" +

			"<h3><font color=\"#FF3300\">��ҵ����</font></h3>" +
			"��ҵ���ƣ�̨����·�����������г���չ���޹�˾<br>" +
			"��ҵ���ʣ�������ҵ<br>" +
			"ҵ�����������̨����ס���ͳ��罨��滮��·�ŷ־�<br>" +
			"��Ӫ��Χ�����г����ޡ��������ܽ�ͨϵͳӦ�úͼ�����������IC��Ӧ�ù�����ơ������������������ڸ����棻���λ���ޡ�<br>" +
			"�ٷ���վ��http://www.luqiaobike.com <br>" +
			"���˴����̶�ǿ���ܾ���<br>" +
			"�������ڣ�2013��5��20��<br>" +
			"��˾��ַ��<br>" +
			"̨����·�����Ļ�·2�ţ�����ҽԺ�����棩<br>" +
			"���ſ����⳵��͹���ͤ��������������һ¥���������ҹ�<br>" +
			"����ʱ�䣺<br>" +
			"����8:30~11:30����14:00~18:00(����ʱ)<br>" +
			"����8:30~11:30����13:00~17:00(����ʱ)<br>" +
			"�������ߣ�<br>" +
			"0576-80290112<br>" +
			"400-820-1898�������֧�������л��ѣ�<br>" +
			"��Ϣ������<br>" +
			"·����������Դ����ᱣ�Ͼ�������Ƹ��Ϣ��<br>" +
			"http://lqrs.luqiao.gov.cn <br>" +
			"̨����ס���ͳ��罨��滮��·�ŷ־֣����棩<br>" +
			"http://jianshe.luqiao.gov.cn <br>" +

			"<h3><font color=\"#FF3300\">�������</font></h3>" +
			"1��2013��9��22�գ���·�����������г���չ���޹�˾��·����ũ��������������·�Ź������г�������ʽ��·�������㳡���С�" +
			"���˴�ί�ḱ���θ�Ƽ�����������������ܱ����´ǣ�����Э����ϯ�����ġ���ס���־־ֳ����²š���ũ�������維�³���ʱ�����Ϻ��������г���˾�ܾ������褵��쵼�μ���ʽ��" +
			"һ�ڹ��̽���������1,566����·����ũ��������о������г�1,300����������Ҫ�ֲ���·�Žֵ���·�Ͻֵ���·���ֵ���סլ�����̳����������ϴ�ĵط���<br>" +
			"2��2013��12��09�գ�·�Ź������г����Ĺ�԰�����⵽�����ƻ����������������г�������Ť�������޷�ʹ�ã�" +
			"����Ӧ������������Ҳ��ͬ�̶����𡣹������г�ƵƵ�⵽�����𻵣������˹������Դ�ɥʧ��ṫ����Ϊ��ǿ����ߡ�" +
			"������ֹ��������г��Ĳ��������󣬿ɲ���80290112���оٱ������������𻵹������г��ģ�������Բ���110���������ɹ������Ž��룬" +
			"���ݡ��л����񹲺͹��ΰ��������������йع涨���д�����<br>" +
			"3����ֹ2014��01��05�գ�·�Ź������г��쿨���Ѵﵽ10350�ţ�ÿ�����ù������г�Լ��4500�˴Ρ��������г��ѳ���·���˳��е���Ҫ��ͨ����֮һ��" +
			"Ͷ��1100��Ԫ��·�Ź������г����ڹ����Ѿ����������ڽ��ɺ�·�ŵĹ������г�������81�����㣬�ﵽ134����Ͷ�빫�����г���4000����" +
			"����Ŀ�ܹ�������ʵʩ����Ͷ�ʹ���Ϊ28866.58��Ԫ������һ�ڽ������365.438��Ԫ��5����Ӫ����Ϊ564.864��Ԫ��" +
			
			"<h5><font color=\"#FF3300\">����ͳ��</font></h5>" +
			"1������������ݾ���Դ������<br>" +
			"2��ÿ�δ����Ĭ���Զ�ͬ��һ��<br>" +
			"3��ÿ��ͬ���������ݴ�Լ������130KB������<br>" +
			"4�����г�����ͼƬ���ȼ��ر��ػ��棬����·��:SD��/Android/data/com.lqzxc<br>" +
			"5�������ֻ��鿴���г�����ֲ���ʹ�����ߵ�ͼ����<br>" +
			"6������GPS��λ���Ը���ȷ�ض�λ����λ��<br><br>" +
			"ͬ���ɹ��ܴ�����" +updateCount+"<br>" +
			"��ѯ�ܴ�����" +updateVisit+"<br>" +
			"���������ܼƣ�" +mAppContext.formatDataSize(updateFlow)+"�����Ƶ�ͼ��ͼƬ��WIFI��<br>" +
			"�������ʱ�䣺" +updateTime+"<br>" +
			"��������:" +mAppContext.mBikeSties.size()+"<br>" +
			"��ǰ����������:"+mAppContext.totalCapacity+"<br>" +
			"��ǰ�ɽ�����:"+mAppContext.totalNum+""));
		btn_version = (LinearLayout) findViewById(R.id.btn_setting_version);
		btn_feedback = (LinearLayout) findViewById(R.id.btn_setting_feedback);
		btn_version.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//	    		MobclickAgent.onEvent(AboutActivity.this,"setting_version");
				//statusCode ���»ص��ӿڷ���״̬�� 0 �и��� ;1 û�и���; 2 ��wifi״̬; 3 ��ʱ;
				//updateInfo ���»ص��������ݣ�����App������Ϣ������: updateLog ������־ ; version ���°汾 ; path ���°���������
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
				            		mAppContext.ToastMessage(AboutActivity.this,"��ǰ�汾V" + 
				            				getPackageManager().getPackageInfo("com.lqzxc", 0).versionName + "���Ѿ������°�");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				                break;
				            case 2: // none wifi
				            	mAppContext.ToastMessage(AboutActivity.this, "û��wifi���ӣ� ֻ��wifi�¸���");
				                break;
				            case 3: // time out
				            	mAppContext.ToastMessage(AboutActivity.this, "��ʱ");
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

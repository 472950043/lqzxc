package com.lqzxc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * ����ͳ��
 * @author QQ472950043
 **/
public class CodeCount {

    private static int count_java = 0;
    private static int count_xml = 0;
    private static int note_java = 0;
    private static int note_xml = 0;

	/**
	 * �����ļ�
	 * @param path
	 */
	private static void listFile(String path){
		File file = new File(path);
		if(!file.exists()){
			System.err.println(file.getAbsolutePath() + "·��������");
		}
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f:files){
				listFile(f.getAbsolutePath());
			}
		}else{
			countLine(file);
		}
	}

    private static int java = 0;//java�ļ�����
    private static int xml = 0;//xml�ļ�����
	//�ֱ������Ŀjava�ļ���xml�ļ���������
	private static void countLine(File file){
		if(file.exists()){
			if(file.getName().endsWith(".java")){
				countLine4Java(file);//������Ŀjava�ļ���������
				java++;
			}else if(file.getName().endsWith(".xml")){
				countLine4Xml(file);//������Ŀxml�ļ���������
				xml++;
			}
		}
	}

	/**
	 * ������Ŀ��java�ļ���������
	 * @param file
	 */
	private static void countLine4Java(File file){
		if(!file.exists() || !file.getName().endsWith(".java")){
			return;
		}
		BufferedReader in = null;
		String str = null;
		try {
			in = new BufferedReader(new FileReader(file));
			while((str=in.readLine()) != null){
				//ȥ��tab�ո�
				str = str.replaceAll("\t", "");
				//ȥ�����׿ո�
				str = removeSpace(str);
				//�ų�����
				if(str.equals("")){
					continue;
				}
				//�ų�����ע��
				if(str.startsWith("//")){
					continue;
				}
				//�ų�����ע��
				if(str.startsWith("/*") || str.startsWith("*")){
					note_java++;
					continue;
				}
//				System.out.println(str);
				count_java++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ������Ŀxml�ļ���������
	 * @param file
	 */
	private static void countLine4Xml(File file){
		if(!file.exists() || !file.getName().endsWith(".xml")){
			return;
		}
		boolean flag = true;
		BufferedReader in = null;
		String str = null;
		try {
			in = new BufferedReader(new FileReader(file));
			while((str=in.readLine()) != null){
				//ȥ��tab�ո�
				str = str.replaceAll("\t", "");
				//ȥ���ո�
				str = removeSpace(str);
				//�ų�����
				if(str.equals("")){
					continue;
				}
				//�ų�ע��
				if(str.startsWith("<!--")){
					flag = false;
				}
				if(flag){
//					System.out.println(str);
					count_xml++;
				}else{
					note_xml++;
				}
				if(str.startsWith("-->") || str.endsWith("-->")){
					flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//�Ƴ��ַ����Ŀ�ʼ�ո�
	public static String removeSpace(String s){
		String str = s;
		if(str.equals("") || str == null){
			return "";
		}
		if(!str.startsWith(" ")){
			return str;
		}
		str = str.replaceFirst(" ", "");
		return removeSpace(str);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		listFile("D:\\workspace_new\\lqzxc");
		long endTime = System.currentTimeMillis();
		System.out.println("java�ļ�������" + java);
		System.out.println("java������������" + count_java);
		System.out.println("javaע����������" + note_java);
		System.out.println("xml�ļ�������" + xml);
		System.out.println("xml������������" + count_xml);
		System.out.println("xmlע����������" + note_xml);
		System.out.println("��������ʱ�䣺" + (endTime-startTime));
	}

}
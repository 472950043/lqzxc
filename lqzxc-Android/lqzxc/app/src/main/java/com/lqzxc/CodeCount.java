package com.lqzxc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * 代码统计
 * @author QQ472950043
 **/
public class CodeCount {

    private static int count_java = 0;
    private static int count_xml = 0;
    private static int note_java = 0;
    private static int note_xml = 0;

	/**
	 * 遍历文件
	 * @param path
	 */
	private static void listFile(String path){
		File file = new File(path);
		if(!file.exists()){
			System.err.println(file.getAbsolutePath() + "路径不存在");
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

    private static int java = 0;//java文件数量
    private static int xml = 0;//xml文件数量
	//分别计算项目java文件和xml文件代码数量
	private static void countLine(File file){
		if(file.exists()){
			if(file.getName().endsWith(".java")){
				countLine4Java(file);//计算项目java文件代码数量
				java++;
			}else if(file.getName().endsWith(".xml")){
				countLine4Xml(file);//计算项目xml文件代码数量
				xml++;
			}
		}
	}

	/**
	 * 计算项目中java文件代码数量
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
				//去掉tab空隔
				str = str.replaceAll("\t", "");
				//去除行首空格
				str = removeSpace(str);
				//排除空行
				if(str.equals("")){
					continue;
				}
				//排除单行注释
				if(str.startsWith("//")){
					continue;
				}
				//排除多行注释
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
	 * 计算项目xml文件代码数量
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
				//去掉tab空隔
				str = str.replaceAll("\t", "");
				//去除空格
				str = removeSpace(str);
				//排除空行
				if(str.equals("")){
					continue;
				}
				//排除注释
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

	//移除字符串的开始空格
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
		System.out.println("java文件数量：" + java);
		System.out.println("java代码总行数：" + count_java);
		System.out.println("java注释总行数：" + note_java);
		System.out.println("xml文件数量：" + xml);
		System.out.println("xml代码总行数：" + count_xml);
		System.out.println("xml注释总行数：" + note_xml);
		System.out.println("程序运行时间：" + (endTime-startTime));
	}

}
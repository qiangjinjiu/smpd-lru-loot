package org.os.dpms.client;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import guru.springframework.domain.Slide;

public class Md5sumRunner180421NDP {
	public static List<Slide> slides = new ArrayList<Slide>();
	public static void main(String[] args) throws Exception {
		String filepath="e:\\zhengjunjie\\金域检验\\肾脏病理\\2018-04-21-2\\";
		//String filepath="e:\\zhengjunjie\\金域检验\\肾脏病理\\2018-04-18\\KB1804682史诗智\\";
		
		File szbl = new File(filepath);
		
		Md5sumRunner180421NDP md5 = new Md5sumRunner180421NDP();
		md5.getAllWsi(szbl);
/*
		for(Slide s: slides) {
			System.out.println(s);
		}*/
		md5.doPatchMeta();
	}
	
	private void doPatchMeta() throws Exception{
		String url="jdbc:mariadb://10.28.13.152:3306/wsi";

		String user="root";

		String password="secret";
		Connection con = null;
		PreparedStatement psI= null;
		
		
		try {
		con = DriverManager.getConnection(url,user,password);
		String insertSql = "insert into t_slide_180421_2 (case_id, slide_id, file,title, md5sum, rdv_url, vendor) values (?,?,?,?,?,?,?)";
		psI = con.prepareStatement(insertSql);
		
		for(Slide s:slides) {
			psI = con.prepareStatement(insertSql);
			psI.setString(1, s.getCaseId());
			psI.setString(2, s.getSlideId());
			psI.setString(3, s.getFile());
			psI.setString(4, s.getTitle());
			psI.setString(5, s.getMd5sum());
			psI.setString(6, s.getRdvUrl());
			psI.setString(7, "Hamamatsu");
			
			int i = psI.executeUpdate();
			System.out.println(i);
		}
		}catch(Exception e) {
			e.printStackTrace();;
		}finally {
			psI.close();
			con.close();
		}
	}
	
	private void getAllWsi(File szbl) {
		File[] list = szbl.listFiles();
		for(File f: list) {
			if(f.isDirectory()) {
				getAllWsi(f);
			}else {
				if(f.getName().endsWith("ndpi")) {
					System.out.println(f.getAbsolutePath());
					String caseId;
					String slideId;
					String md5sum;
					String title;
					String filename = f.getName();
					System.out.println(filename);
					caseId = filename.substring(0, 9);
					slideId = filename.substring(0,filename.indexOf("."));
					title = filename.substring(9,filename.indexOf(".")); 
					
					System.out.println(caseId);
					System.out.println(slideId);
					md5sum = BigFileMD5.getMD5(f);
					Slide s = new Slide();
					s.setCaseId(caseId);
					s.setSlideId(slideId);
					s.setFile(filename);
					s.setFileSize(f.length());
					s.setMd5sum(md5sum);
					s.setTitle(title);
					
					String rdvUrl = "http://epathology.kingmed.com.cn:8888/sso?caseId="+caseId+"&slideId="+slideId+"&type=RDV&token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTUyMDMyNzM5Miwicm9sZXMiOlsiV3JpdGVPd24iLCJSZWFkQWxsIiwiV3JpdGVBbGwiLCJNYW5hZ2VTbGlkZXMiLCJNYW5hZ2VFdmVudHMiXSwiaXNzIjoidGVzdC5qd3Quc2VydmVyIiwiZXhwIjoxNTM1ODc5MzkyfQ.hmRQVH2b3EPBfiHsHFvDJlyC5LpzEvfVC_2Ta5tcp1_I86SbffnRT-67q_QY7DFf5Itfh5HeleC2lHABWftUwvruiHp9dnSkOTpK9dpSRqi91WrM1PondaJlr-ErX4YoG_hxaFXMxw93QJgayUXTLOwdfNncFSjHvdgZ3mNq_mKsOPl7MeVRFFaSvBQBkb8Ojr-9F6HtEtV4vUdkxJcOM1FCl2MZvfIkprma5rUk67CcQaQxzRA0dlgRDvynziuKwo7MgtcgpuCZLj1J6j7kaGwKSMx2OLuwEDVvPn9aubVk9iXOy2V2jNIADvTtpGq3B5HBEagfrA7_8RU2sYd13A";
					s.setRdvUrl(rdvUrl);
					s.setIngestCmd("cmd");
					slides.add(s);
				}
			}
		}
	}

}

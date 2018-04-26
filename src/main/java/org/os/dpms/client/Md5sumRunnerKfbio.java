package org.os.dpms.client;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import guru.springframework.domain.Slide;

public class Md5sumRunnerKfbio {
	private static final String VENDOR_KFBIO = "kfbio";
	private static final String EXT = "kfb";
	public static List<Slide> slides = new ArrayList<Slide>();
	public static int counter=0;
	public static int batch_no=1;
	public static String filepath="e:\\zhengjunjie\\金域检验\\dp\\上传切片到DPMS\\皮肤扫描片\\水疱性皮肤病04\\";
	
	public static void main(String[] args) throws Exception {
		
		File szbl = new File(filepath);
		
		Md5sumRunnerKfbio md5 = new Md5sumRunnerKfbio();
		
		//重命名到本地数字切片
		//md5.doStage1(szbl);
		
		//计算meta
		md5.doStage2(szbl);
		
	/*	for(Slide s: slides) {
			System.out.println(s);
		}*/

		//更新meta到数据库
		md5.doState3();
		
		//更新rdv_url 和 ingest到数据库
		md5.doStage4();
	}
	
	/**
	 * 更新meta到数据库
	 * @throws Exception
	 */
	private void doState3() throws Exception{
		String url="jdbc:mariadb://10.28.13.152:3306/wsi";

		String user="root";

		String password="secret";
		Connection con = null;
		PreparedStatement ps= null;
		
		
		try {
		con = DriverManager.getConnection(url,user,password);
		String sql = "update t_slide set file = ?, fileSize= ?, md5sum= ?, vendor=?, local_filepath=? where folder=? and slide_id=?";
		ps = con.prepareStatement(sql);
		
		for(Slide s:slides) {
			ps = con.prepareStatement(sql);
			ps.setString(1, s.getFile());
			ps.setLong(2, s.getFileSize());
			ps.setString(3, s.getMd5sum());
			ps.setString(4, VENDOR_KFBIO);
			ps.setString(5, s.getLocalFilepath());
			ps.setString(6, s.getFolder());
			ps.setString(7, s.getSlideId());
			
			int i = ps.executeUpdate();
			if( i< i) {
				System.err.println("执行SQL的结果失败:" + i);
			}
		}
		}catch(Exception e) {
			e.printStackTrace();;
		}finally {
			ps.close();
			con.close();
		}
	}
	
	/**
	 * 生成rdv_url，ingest命令
	 */
	
	private void doStage4() throws Exception{
		String url="jdbc:mariadb://10.28.13.152:3306/wsi";

		String user="root";

		String password="secret";
		Connection con = null;
		PreparedStatement ps= null;
		PreparedStatement psU= null;
		ResultSet rs =null;
		
		try {
			con = DriverManager.getConnection(url,user,password);
			String sql = "select case_id, slide_id,file,vendor,md5sum,fileSize,expiry,title from t_slide where wsi_batch_no=?";
			String updateSql = "update t_slide set rdv_url =?, ingest_cmd=? where case_id=? and slide_id=? and wsi_batch_no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, batch_no);
			psU = con.prepareStatement(updateSql);
			
			
			rs = ps.executeQuery();
			while(rs.next()) {
				String caseId = rs.getString(1);
				String slideId = rs.getString(2);
				String file = rs.getString(3);
				String vendor = rs.getString(4);
				String md5sum = rs.getString(5);
				Long fileSize = rs.getLong(6);
				int expiry = rs.getInt(7);
				String title = rs.getString(8);
				
				String rdvUrl = "http://epathology.kingmed.com.cn:8888/sso?caseId="+caseId+"&slideId="+slideId+"&type=RDV&token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTUyMDMyNzM5Miwicm9sZXMiOlsiV3JpdGVPd24iLCJSZWFkQWxsIiwiV3JpdGVBbGwiLCJNYW5hZ2VTbGlkZXMiLCJNYW5hZ2VFdmVudHMiXSwiaXNzIjoidGVzdC5qd3Quc2VydmVyIiwiZXhwIjoxNTM1ODc5MzkyfQ.hmRQVH2b3EPBfiHsHFvDJlyC5LpzEvfVC_2Ta5tcp1_I86SbffnRT-67q_QY7DFf5Itfh5HeleC2lHABWftUwvruiHp9dnSkOTpK9dpSRqi91WrM1PondaJlr-ErX4YoG_hxaFXMxw93QJgayUXTLOwdfNncFSjHvdgZ3mNq_mKsOPl7MeVRFFaSvBQBkb8Ojr-9F6HtEtV4vUdkxJcOM1FCl2MZvfIkprma5rUk67CcQaQxzRA0dlgRDvynziuKwo7MgtcgpuCZLj1J6j7kaGwKSMx2OLuwEDVvPn9aubVk9iXOy2V2jNIADvTtpGq3B5HBEagfrA7_8RU2sYd13A";
				String ingestCmd="http --print Bb POST http://10.28.13.224/DPMS/slide X-Token:eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTUyNDEyMTQwMywicm9sZXMiOlsiV3JpdGVPd24iLCJSZWFkQWxsIiwiV3JpdGVBbGwiLCJNYW5hZ2VTbGlkZXMiLCJNYW5hZ2VFdmVudHMiXSwiaXNzIjoidGVzdC5qd3Quc2VydmVyIiwiZXhwIjoxNTM5NjczNDAzfQ.gETu2dxLQLofuyX_ewLbUMpY4k1w-V3JdWh-dp3tSsKtWNAFSliOo0GXl4PVtn4fcTDa4JmAEx4euXP7FdGKj-C9q7M1sPRW-QNxOznVbXp0jDjzmkXj81k15LJqvOq_XVvtLg33fHaMN1ctYALt2LuCcHYIkACec3PpMc9AZFjmU-mW_lnGXY3pwPUzyW8kesdudjMJoLpUChZFfFWk702fk_fYn-034V519dpJ4Dp9OeKIvuc9vTFBS4DrTFIByT-NuiDosl7oD5EgKyHgjtej9dE6ki3T9QP_tBmGU_fi75B0VZRItfAwBTv9sdionBIG5kxkbvUTKhjf76DG7A caseId="+caseId+" slideId="+slideId+" file="+file + " vendor="+vendor + " md5sum="+md5sum+" fileSize:="+fileSize +" expiry:="+expiry+" title="+ title;
				
				psU.setString(1, rdvUrl);
				psU.setString(2, ingestCmd);
				psU.setString(3, caseId);
				psU.setString(4, slideId);
				psU.setInt(5,batch_no);
				
				int i = psU.executeUpdate();
				System.out.println("caseId="+caseId +", slideId=" + slideId +",设置url的结果 " + i);
			}

		}catch(Exception e) {
			e.printStackTrace();;
		}finally {
			ps.close();
			con.close();
		}
	}
	/**
	 * 处理数字切片文件名
	 * 去除中文字符、空格
	 * 以excel或者文件名包含的Id来命名
	 * excel中需要包含文件名和case_id slide_id的对应关系
	 * @param szbl
	 */
	private void doStage1(File szbl) {
		File[] list = szbl.listFiles();
		for(File f: list) {
			if(f.isDirectory()) {
				doStage2(f);
			}else {
				if(f.getName().endsWith(EXT)) {
					String slideId;
					String filename = f.getName();
					int splitIdx = filename.indexOf("-");
					slideId = filename.substring(splitIdx + 1, filename.indexOf("."));
					
					//重命名数字切片，删除掉数字切片的文件名包含中文字符
					File newFile = new File(f.getParentFile(),slideId+"."+EXT);
					if(splitIdx > 0) {
						f.renameTo(newFile);
						System.out.println("重命名" +f.getAbsolutePath() + " renamed to " + newFile.getAbsolutePath());
					}
				}
			}
		}
	}
	
	/**
	 * 生成uploadmeta
	 * @param szbl
	 */
	private void doStage2(File szbl) {
		File[] list = szbl.listFiles();
		for(File f: list) {
			if(f.isDirectory()) {
				doStage2(f);
			}else {
				if(f.getName().endsWith(EXT)) {
					String folder = f.getParentFile().getName();
					
					System.out.println(f.getAbsolutePath());
					String slideId;
					String md5sum="";
					String filename = f.getName();
					System.out.println(filename);
					int splitIdx = filename.lastIndexOf(".");
					slideId = filename.substring(0, splitIdx);
					md5sum = BigFileMD5.getMD5(f);
					
					Slide s = new Slide();
					s.setSlideId(slideId);
					s.setFile(f.getName());
					s.setFileSize(f.length());
					s.setMd5sum(md5sum);
					s.setFolder(folder);
					s.setLocalFilepath(f.getAbsolutePath());
					
					slides.add(s);
				}
			}
		}
	}

}

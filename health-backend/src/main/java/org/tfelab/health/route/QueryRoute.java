package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;
import org.tfelab.health.ServiceWrapper;
import org.tfelab.health.model.Doctor;
import org.tfelab.health.model.DoctorService;
import org.tfelab.health.model.Hospital;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class QueryRoute {
	
	public static final Logger logger = LogManager.getLogger(QueryRoute.class.getName());
	
	private static double EARTH_RADIUS = 6378.137D;//地球半径
	private static double rad(double d){
		return d * Math.PI / 180.0D;
	}
	
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2), 2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = ((double) Math.round(s * 100)) / 100;
		return s;
	}
	
	public QueryRoute() {
		
		post("/query", (request, response) -> {

			try {
				
				Query q = JSON.fromJSON(request.queryParams("_q"), Query.class);
				
				logger.info(JSON.toJson(q));
				
				if(q.preference == 1){
					return new Msg<List<DoctorService>>(Msg.SUCCESS, getNearestDoctors(q));
				}
				else if(q.preference == 2){
					return new Msg<List<DoctorService>>(Msg.SUCCESS, getCheapestDoctors(q));
				}
				else if(q.preference == 3){
					return new Msg<List<DoctorService>>(Msg.SUCCESS, getBestDoctors(q));
				}
				else {
					return new Msg<List<DoctorService>>(Msg.SUCCESS, getDoctors(q));
				}
				
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.FAILURE);
			}
			
		}, new MsgTransformer());
		
	}
	
	/**
	 * 获得最近的
	 * @param query
	 * @return
	 * @throws Exception
	 */
	static List<DoctorService> getNearestDoctors(Query query) throws Exception{
		
		Dao<Hospital, String> hospitalDao = OrmLiteDaoManager.getDao(Hospital.class);//DAO(Data Acess Object,数据访问对象，对数据库进行访问的基类。)
		
		QueryBuilder<Hospital, String> qb1 = hospitalDao.queryBuilder();
		qb1.orderByRaw("POWER(hospitals.longitude - " + query.log + ", 2) + POWER(hospitals.latitude - " + query.lat + ", 2)");//POWER返回给定表达式乘指定次方的值;
		//语法：POWER ( numeric_expression , y )
		//参数：numeric_expression：是精确数字或近似数字数据类型类别的表达式（bit 数据类型除外）。y：numeric_expression 的次方。y 可以是精确数字或近似数字数据类型类别的表达式（bit 数据类型除外）。

		Dao<DoctorService, String> doctorServiceDao = OrmLiteDaoManager.getDao(DoctorService.class);
		
		QueryBuilder<DoctorService, String> qb2 = doctorServiceDao.queryBuilder();
		qb2.where().eq("name", query.service);
		
		Dao<Doctor, String> doctorDao = OrmLiteDaoManager.getDao(Doctor.class);
		
		QueryBuilder<Doctor, String> qb = doctorDao.queryBuilder();
		Where<Doctor, String> where = qb.where();
		where.eq("section_id", query.section_id);

		
//		for(Doctor doctor: qb.join(qb1).query()){
//			double distance = getDistance(query.lat, query.log, doctor.hospital.latitude, doctor.hospital.longitude);
//			System.err.println(doctor.name);
//			System.err.println(doctor.hospital.name);
//			System.err.println(distance);
//		}
		//在查询条件比较复杂时，需要调用PreparedQuery
		PreparedQuery<DoctorService> pq = qb2.join(
					qb.join(qb1)
						.orderByRaw("POWER(hospitals.longitude - " + query.log + ", 2) + POWER(hospitals.latitude - " + query.lat + ", 2)")
				)
				.offset(query.offset).limit(query.limit)
				.prepare();
		
		List<DoctorService> result = doctorServiceDao.query(pq);
		
		//List<DoctorService> result = qb2.join(qb.join(qb1).orderByRaw("POWER(`hospitals`.longitude - " + query.log + ", 2) + POWER(`hospitals`.latitude - " + query.lat + ", 2)")).offset(query.offset).limit(query.limit).query();
		
		// 计算距离
		for(DoctorService ds : result){
			
//			logger.info(query.lat);
//			logger.info(query.log);
//			logger.info(ds.doctor.hospital.latitude);
//			logger.info(ds.doctor.hospital.longitude);
			
			ds.distance = getDistance(query.lat, query.log, ds.doctor.hospital.latitude, ds.doctor.hospital.longitude);

		}
		
		return result;
	}
	
	/**
	 * 获得收费最低的
	 * @param query
	 * @return
	 * @throws Exception
	 */
	static List<DoctorService> getCheapestDoctors(Query query) throws Exception{
		
		Dao<Doctor, String> doctorDao = OrmLiteDaoManager.getDao(Doctor.class);
		QueryBuilder<Doctor, String> qb = doctorDao.queryBuilder();
		Where<Doctor, String> where = qb.where();
		where.eq("section_id", query.section_id);
		
		Dao<DoctorService, String> doctorServiceDao = OrmLiteDaoManager.getDao(DoctorService.class);
		
		QueryBuilder<DoctorService, String> qb2 = doctorServiceDao.queryBuilder();
		qb2.join(qb).orderBy("min_charge", true).offset(query.offset).limit(query.limit).where().eq("name", query.service);
		
		List<DoctorService> result = doctorServiceDao.query(qb2.prepare());

		// 计算距离
		for(DoctorService ds : result){
			ds.distance = getDistance(query.lat, query.log, ds.doctor.hospital.latitude, ds.doctor.hospital.longitude);
		}
		
		return result;
	}
	
	/**
	 * 获得评分最高的
	 * @param query
	 * @return
	 * @throws Exception
	 */
	static List<DoctorService> getBestDoctors(Query query) throws Exception{
		
		Dao<DoctorService, String> doctorServiceDao = OrmLiteDaoManager.getDao(DoctorService.class);
		
		QueryBuilder<DoctorService, String> qb2 = doctorServiceDao.queryBuilder();
		qb2.offset(query.offset).limit(query.limit).where().eq("name", query.service);
		
		Dao<Doctor, String> doctorDao = OrmLiteDaoManager.getDao(Doctor.class);
		
		QueryBuilder<Doctor, String> qb = doctorDao.queryBuilder();
		
		Where<Doctor, String> where = qb.where();
		where.eq("section_id", query.section_id);
		
		qb.orderBy("rating", false);
		
		List<DoctorService> result = doctorServiceDao.query(qb2.join(qb).prepare());

		// 计算距离
		for(DoctorService ds : result){
			ds.distance = getDistance(query.lat, query.log, ds.doctor.hospital.latitude, ds.doctor.hospital.longitude);

		}
		
		return result;
	}
	
	/**
	 * 综合排序
	 * @param query
	 * @return
	 * @throws Exception
	 */
	static List<DoctorService> getDoctors(Query query) throws Exception{
		
		String sql = "SELECT  "+
				"	t.id,  "+
				"	10 * (t1.r1 - t.r1) / t1.r1 + (t.r2 - t2.r2) + 10 * (t3.r3 - t.r3) / t3.r3 as r "+
				"FROM ( "+
				"	SELECT  "+
				"		`doctor_services`.id as id,  "+
				"		POWER(POWER(hospitals.longitude - " + query.log + ", 2) + POWER(hospitals.latitude - " + query.lat + ", 2), 0.5) as r1, "+
				"		`doctors`.rating as r2, "+
				"		`doctor_services`.min_charge as r3 "+
				"	FROM `doctor_services`  "+
				"	INNER JOIN `doctors` ON `doctor_services`.`doctor_id` = `doctors`.`id`  "+
				"	INNER JOIN `hospitals` ON `doctors`.`hospital_id` = `hospitals`.`id`  "+
				"	WHERE `doctor_services`.`name` = '" + query.service + "'  " +
				"		AND `doctors`.`section_id` = '" + query.section_id + "'  "+
				") t, "+
				"(SELECT MAX(POWER(POWER(longitude - " + query.log + ", 2) + POWER(latitude - " + query.lat + ", 2), 0.5)) as r1 FROM hospitals) t1, "+
				"(SELECT MIN(rating) as r2 FROM doctors) t2, "+
				"(SELECT MAX(min_charge) as r3 FROM doctor_services) t3 "+
				"ORDER BY r DESC " +
				"LIMIT " + query.limit + " OFFSET " + query.offset + ";";
		
		logger.info(sql);
		
		Dao<DoctorService, String> doctorServiceDao = OrmLiteDaoManager.getDao(DoctorService.class);
		GenericRawResults<String[]> records = doctorServiceDao.queryRaw(sql);
		
		List<DoctorService> result = new ArrayList<DoctorService>();
		for(String[] record : records) {
			
			System.err.println(record);
			
			DoctorService ds = doctorServiceDao.queryForId(record[0]);
			
			ds.distance = getDistance(query.lat, query.log, ds.doctor.hospital.latitude, ds.doctor.hospital.longitude);

			result.add(ds);
		}
		
		
		return result;
	}
	
	public static void main(String[] args){
		
		Query q = new Query();
		q.section_id = 1;
		q.service = "图文问诊";
		q.preference = 0;
		q.log = 116.2839127;
		q.lat = 39.9115982;
		
		try {
			for(DoctorService ds : getDoctors(q)){
				System.err.println(JSON.toJson(ds));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Query implements JSONable<DoctorService> {
	
	int section_id;
	String service;
	int preference;
	double log;
	double lat;
	long offset = 0;
	long limit = 1;
	
	@Override
	public String toJSON() {
		
		return JSON.toJSON(this);
	}
}

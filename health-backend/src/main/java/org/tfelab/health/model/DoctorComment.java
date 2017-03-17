package org.tfelab.health.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.DBName;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "doctor_comments")
@DBName(value = "health")
public class DoctorComment implements JSONable<Doctor>{
	
	private static final Logger logger = LogManager.getLogger(DoctorComment.class.getName());
	
	@DatabaseField(columnName = "id", dataType = DataType.INTEGER, canBeNull = false, generatedId = true)
	public int id;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	public Doctor doctor;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	public User user;
	
	@DatabaseField(dataType = DataType.FLOAT, canBeNull = false, defaultValue = "0")
	public float rating;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = true)
	public String disease;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = true)
	public String content;
	
	@DatabaseField(columnName = "insert_time", dataType = DataType.DATE, canBeNull = false)
	public transient Date insert_time = new Date();
	
	public DoctorComment(){}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insert() throws Exception{
		
		Dao<DoctorComment, String> dao = OrmLiteDaoManager.getDao(DoctorComment.class);
		
		if (dao.create(this) == 1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean delete() throws Exception {
		
		Dao<DoctorComment, String> dao = OrmLiteDaoManager.getDao(DoctorComment.class);
		
		if (dao.delete(this) == 1) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param doctor_id
	 * @param limit
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public static List<DoctorComment> getCommentsByDoctorId(int doctor_id, long limit, long offset) throws Exception {
		
		List<DoctorComment> comments;
		
		Dao<DoctorComment, String> dao = OrmLiteDaoManager.getDao(DoctorComment.class);
		
		QueryBuilder<DoctorComment, String> qb = dao.queryBuilder();
		qb.offset(offset).limit(limit).where().eq("doctor_id", doctor_id);
		
		comments = qb.query();
		
		if(comments != null) {
			return comments;
		} else {
			return new ArrayList<DoctorComment>();
		}
		
	}

	@Override
	public String toJSON() {
		return JSON.toJSON(this);
	}
	
}

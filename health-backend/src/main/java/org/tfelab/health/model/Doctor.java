package org.tfelab.health.model;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.DBName;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "doctors")
@DBName(value = "health")
public class Doctor implements JSONable<Doctor>{
	
	private static final Logger logger = LogManager.getLogger(Doctor.class.getName());
	
	@DatabaseField(columnName = "id", dataType = DataType.INTEGER, canBeNull = false, generatedId = true)
	public int id;
	
	@DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
	public int hospital_id;
	
	@DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
	public int section_id;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = false)
	public String name;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String title;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String position;
	
	@DatabaseField(dataType = DataType.FLOAT, canBeNull = true)
	public float rating = 0;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = false)
	public String good_at;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = true)
	public String brief_intro;
	
	@DatabaseField(columnName = "insert_time", dataType = DataType.DATE, canBeNull = false)
	public Date insert_time = new Date();
	
	@DatabaseField(dataType = DataType.DATE, columnName = "update_time", canBeNull = false)
    public Date update_time = new Date();
	
	public Doctor(){}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insert() throws Exception{
		
		Dao<Doctor, String> dao = OrmLiteDaoManager.getDao(Doctor.class);
		
		if (dao.create(this) == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean update(){
		
		this.update_time = new Date();
		
		Dao<Doctor, String> dao;
		
		try {
			
			dao = OrmLiteDaoManager.getDao(Doctor.class);
			
			if (dao.update(this) == 1) {
				
				return true;
			}
			
		} catch (Exception e) {
			logger.error("Error Update doctor.", e);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean delete() throws Exception{
		
		Dao<Doctor, String> dao = OrmLiteDaoManager.getDao(Doctor.class);
		
		if (dao.delete(this) == 1) {
			return true;
		}
		
		return false;
	}

	@Override
	public String toJSON() {
		return JSON.toJSON(this);
	}
	
}

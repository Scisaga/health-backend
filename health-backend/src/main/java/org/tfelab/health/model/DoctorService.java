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

@DatabaseTable(tableName = "doctor_services")
@DBName(value = "health")
public class DoctorService implements JSONable<DoctorService>{
	
	private static final Logger logger = LogManager.getLogger(DoctorService.class.getName());
	
	@DatabaseField(columnName = "id", dataType = DataType.INTEGER, canBeNull = false, generatedId = true)
	public int id;
	
	@DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
	public int doctor_id;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String name;
	
	@DatabaseField(dataType = DataType.FLOAT, canBeNull = true)
	public float min_charge;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = true)
	public String memo;
	
	@DatabaseField(columnName = "insert_time", dataType = DataType.DATE, canBeNull = false)
	public Date insert_time = new Date();
	
	@DatabaseField(dataType = DataType.DATE, columnName = "update_time", canBeNull = false)
    public Date update_time = new Date();
	
	public DoctorService(){}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insert() throws Exception{
		
		Dao<DoctorService, String> dao = OrmLiteDaoManager.getDao(DoctorService.class);
		
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
		
		Dao<DoctorService, String> dao;
		
		try {
			
			dao = OrmLiteDaoManager.getDao(DoctorService.class);
			
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
		
		Dao<DoctorService, String> dao = OrmLiteDaoManager.getDao(DoctorService.class);
		
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

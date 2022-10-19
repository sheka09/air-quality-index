package org.be.airqualitymonitoring.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="breakpointaqi")
public class BreakpointAqi implements Serializable {

	private static final long serialVersionUID = -2762008553275403478L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Double ozone1hrMin; 
	private Double ozone1hrMax;
	private Double ozone8hrMin; 
	private Double ozone8hrMax;
	private Double carbonMonoxideMin;
	private Double carbonMonoxideMax;
	private Double nitrogenDioxideMin;
	private Double nitrogenDioxideMax;
	private Double sulfurDioxideMin;
	private Double sulfurDioxideMax;
	private Double pm25Min;
	private Double pm25Max;
	private Double pm10Min;
	private Double pm10Max;
	private Integer aqiMin;
	private Integer aqiMax;
	private String category;

}

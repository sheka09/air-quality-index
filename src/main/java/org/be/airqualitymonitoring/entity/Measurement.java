package org.be.airqualitymonitoring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name="measurement")
public class Measurement implements Serializable {

	private static final long serialVersionUID = -6173564313258276196L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateTime;
	private Double ozone;
	private Double carbonMonoxide;
	private Double sulfurDioxide;
	private Double nitrogenDioxide;
	private Double pm25;
	private Double pm10;

    @JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sensor_id",nullable=false)
	private  Sensor sensor;
    
   	
}

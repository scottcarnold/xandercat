package org.xandercat.ofeapps.teslamodels;

import java.io.Serializable;
import java.util.Date;

import org.xandercat.ofe.Candidate;

/**
 * Class to represent a Tesla Model S listed in the Tesla CPO inventory.
 * 
 * @author Scott Arnold
 */
public class CPOModelS implements Candidate, Comparable<CPOModelS>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String vin;
	private String url;
	private String cpoInv;
	private String location;
	private String trim;
	private String aptp;
	private Boolean dualMotor;
	private Boolean rearFacingSeats;
	private Boolean coldWeatherPackage;
	private Boolean soundStudio;
	private Boolean superchargerEnabled;
	private Boolean smartAirSuspension;
	private Boolean dualChargers;
	private String color;
	private String roof;
	private String wheels;
	private String interior;
	private Integer year;
	private Integer miles;
	private Integer price;
	private Date dateAdded;
	
	public CPOModelS() {
		this(null);
	}
	public CPOModelS(String vin) {
		setVin(vin);
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCpoInv() {
		return cpoInv;
	}
	public void setCpoInv(String cpoInv) {
		this.cpoInv = cpoInv;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTrim() {
		return trim;
	}
	public void setTrim(String trim) {
		this.trim = trim;
	}
	public String getAptp() {
		return aptp;
	}
	public void setAptp(String aptp) {
		this.aptp = aptp;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getRoof() {
		return roof;
	}
	public void setRoof(String roof) {
		this.roof = roof;
	}
	public String getWheels() {
		return wheels;
	}
	public void setWheels(String wheels) {
		this.wheels = wheels;
	}
	public String getInterior() {
		return interior;
	}
	public void setInterior(String interior) {
		this.interior = interior;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMiles() {
		return miles;
	}
	public void setMiles(Integer miles) {
		this.miles = miles;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Boolean getDualMotor() {
		return dualMotor;
	}
	public void setDualMotor(Boolean dualMotor) {
		this.dualMotor = dualMotor;
	}
	public Boolean getRearFacingSeats() {
		return rearFacingSeats;
	}
	public void setRearFacingSeats(Boolean rearFacingSeats) {
		this.rearFacingSeats = rearFacingSeats;
	}
	public Boolean getColdWeatherPackage() {
		return coldWeatherPackage;
	}
	public void setColdWeatherPackage(Boolean coldWeatherPackage) {
		this.coldWeatherPackage = coldWeatherPackage;
	}
	public Boolean getSoundStudio() {
		return soundStudio;
	}
	public void setSoundStudio(Boolean soundStudio) {
		this.soundStudio = soundStudio;
	}
	public Boolean getSuperchargerEnabled() {
		return superchargerEnabled;
	}
	public void setSuperchargerEnabled(Boolean superchargerEnabled) {
		this.superchargerEnabled = superchargerEnabled;
	}
	public Boolean getSmartAirSuspension() {
		return smartAirSuspension;
	}
	public void setSmartAirSuspension(Boolean smartAirSuspension) {
		this.smartAirSuspension = smartAirSuspension;
	}
	public Boolean getDualChargers() {
		return dualChargers;
	}
	public void setDualChargers(Boolean dualChargers) {
		this.dualChargers = dualChargers;
	}
	@Override
	public String getUniqueId() {
		return vin;
	}
	@Override
	public String getShortDescription() {
		return url;
	}
	@Override
	public String getFullDescription() {
		return toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aptp == null) ? 0 : aptp.hashCode());
		result = prime
				* result
				+ ((coldWeatherPackage == null) ? 0 : coldWeatherPackage
						.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((cpoInv == null) ? 0 : cpoInv.hashCode());
		result = prime * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result
				+ ((dualChargers == null) ? 0 : dualChargers.hashCode());
		result = prime * result
				+ ((dualMotor == null) ? 0 : dualMotor.hashCode());
		result = prime * result
				+ ((interior == null) ? 0 : interior.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ ((rearFacingSeats == null) ? 0 : rearFacingSeats.hashCode());
		result = prime * result + ((roof == null) ? 0 : roof.hashCode());
		result = prime
				* result
				+ ((smartAirSuspension == null) ? 0 : smartAirSuspension
						.hashCode());
		result = prime * result
				+ ((soundStudio == null) ? 0 : soundStudio.hashCode());
		result = prime
				* result
				+ ((superchargerEnabled == null) ? 0 : superchargerEnabled
						.hashCode());
		result = prime * result + ((trim == null) ? 0 : trim.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((wheels == null) ? 0 : wheels.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPOModelS other = (CPOModelS) obj;
		if (aptp == null) {
			if (other.aptp != null)
				return false;
		} else if (!aptp.equals(other.aptp))
			return false;
		if (coldWeatherPackage == null) {
			if (other.coldWeatherPackage != null)
				return false;
		} else if (!coldWeatherPackage.equals(other.coldWeatherPackage))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (cpoInv == null) {
			if (other.cpoInv != null)
				return false;
		} else if (!cpoInv.equals(other.cpoInv))
			return false;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (dualChargers == null) {
			if (other.dualChargers != null)
				return false;
		} else if (!dualChargers.equals(other.dualChargers))
			return false;
		if (dualMotor == null) {
			if (other.dualMotor != null)
				return false;
		} else if (!dualMotor.equals(other.dualMotor))
			return false;
		if (interior == null) {
			if (other.interior != null)
				return false;
		} else if (!interior.equals(other.interior))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (rearFacingSeats == null) {
			if (other.rearFacingSeats != null)
				return false;
		} else if (!rearFacingSeats.equals(other.rearFacingSeats))
			return false;
		if (roof == null) {
			if (other.roof != null)
				return false;
		} else if (!roof.equals(other.roof))
			return false;
		if (smartAirSuspension == null) {
			if (other.smartAirSuspension != null)
				return false;
		} else if (!smartAirSuspension.equals(other.smartAirSuspension))
			return false;
		if (soundStudio == null) {
			if (other.soundStudio != null)
				return false;
		} else if (!soundStudio.equals(other.soundStudio))
			return false;
		if (superchargerEnabled == null) {
			if (other.superchargerEnabled != null)
				return false;
		} else if (!superchargerEnabled.equals(other.superchargerEnabled))
			return false;
		if (trim == null) {
			if (other.trim != null)
				return false;
		} else if (!trim.equals(other.trim))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (wheels == null) {
			if (other.wheels != null)
				return false;
		} else if (!wheels.equals(other.wheels))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CPOModelS [vin=" + vin + ", url=" + url + ", cpoInv=" + cpoInv
				+ ", location=" + location + ", trim=" + trim + ", aptp="
				+ aptp + ", dualMotor=" + dualMotor + ", rearFacingSeats="
				+ rearFacingSeats + ", coldWeatherPackage="
				+ coldWeatherPackage + ", soundStudio=" + soundStudio
				+ ", superchargerEnabled=" + superchargerEnabled
				+ ", smartAirSuspension=" + smartAirSuspension
				+ ", dualChargers=" + dualChargers + ", color=" + color
				+ ", roof=" + roof + ", wheels=" + wheels + ", interior="
				+ interior + ", year=" + year + ", miles=" + miles + ", price="
				+ price + ", dateAdded=" + dateAdded + "]";
	}
	@Override
	public int compareTo(CPOModelS ms) {
		return vin.compareTo(ms.getVin());
	}
}

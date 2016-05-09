package storm.vstorm.spout;

public class VClientInfo {
	
	String id;
	String ip;
	String buffer_size;
	String bandwidth;
	String framerate;
	

	public VClientInfo(String id, String ip, String buffer_size, String bandwidth, String framerate) {
		// TODO Auto-generated constructor stub
		this.id =id;
		this.ip =ip;
		this.buffer_size =buffer_size;
		this.bandwidth = bandwidth;
		this.framerate = framerate;
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getBuffer_size() {
		return buffer_size;
	}


	public void setBuffer_size(String buffer_size) {
		this.buffer_size = buffer_size;
	}


	public String getBandwidth() {
		return bandwidth;
	}


	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}


	public String getFramerate() {
		return framerate;
	}


	public void setFramerate(String framerate) {
		this.framerate = framerate;
	}

}

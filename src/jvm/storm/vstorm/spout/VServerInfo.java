package storm.vstorm.spout;

public class VServerInfo {
	
	String id; 
	String ip; 
	String port;
	
	public VServerInfo(String id, String ip, String port){
		this.id = id;
		this.ip = ip;
		this.port =port;
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}

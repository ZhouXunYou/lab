package lab.storm.kqi;

import lab.storm.model.Datapackage;

public class ExecutorApi {
	private Datapackage datapackage;
	public ExecutorApi(Datapackage datapackage){
		this.datapackage = datapackage;
	}
	public Datapackage getDatapackage() {
		return datapackage;
	}
}

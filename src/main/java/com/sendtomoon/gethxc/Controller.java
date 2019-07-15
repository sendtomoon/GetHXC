package com.sendtomoon.gethxc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.servie.DownloadService;
import com.sendtomoon.gethxc.servie.HXCInfoService;

@Component
public class Controller {

	@Autowired
	private HXCInfoService is;

	@Autowired
	private DownloadService ds;

	public void startDownload() {
		ds.mainDown();
	}

	public void renewFile() {
		is.renewFile();
	}

	public void renewTag() {
		is.renewTag();
	}

	public void updateUrl() {
		is.updateUrl();
	}

	public void mainService() {
		is.mainService();
	}

}

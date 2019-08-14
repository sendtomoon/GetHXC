package com.sendtomoon.gethxc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.servie.DownloadService;
import com.sendtomoon.gethxc.servie.HXCInfoService;
import com.sendtomoon.gethxc.servie.MergeService;

@Component
@PropertySource("classpath:config.properties")
public class Controller {

	@Autowired
	private HXCInfoService is;

	@Autowired
	private DownloadService ds;
	
	@Autowired
	private MergeService ms;

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
	
	public void mergeFiles() {
		ms.mergeFiles();
	}

}

package com.sendtomoon.gethxc;

import java.util.List;

import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.mongo.MongoDAO;

public class UpdateCount {

	public static void main(String[] args) {
		List<VideoDTO> list = MongoDAO.getOrderBySeeCount();
		int no = 1;
		for (VideoDTO dto : list) {
			String filename = "";
			String strNo = String.valueOf(no);
			while (strNo.length() < 5) {
				strNo = "0" + strNo;
			}
			filename = strNo + "_" + dto.getName().replace(" ", "_");
			dto.setFileName(filename);
			dto.setSeq(no);
			MongoDAO.updateFilename(dto.getID(), dto.getFileName(), dto.getSeq());
			no++;
		}
	}

}

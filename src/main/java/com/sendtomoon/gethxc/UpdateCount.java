package com.sendtomoon.gethxc;

import java.util.List;

import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtomoon.gethxc.mongo.MongoDAO;

public class UpdateCount {

	public static void main(String[] args) {
		List<GetListByTagRespDataDTO> list = MongoDAO.getOrderBySeeCount();
		int no = 1;
		for (GetListByTagRespDataDTO dto : list) {
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

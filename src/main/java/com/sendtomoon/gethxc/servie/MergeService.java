package com.sendtomoon.gethxc.servie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.DAO;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.utils.DateUtils;

@Component
public class MergeService {
	@Autowired
	DAO dao;

	public boolean mergeFiles() {
		File tempFile = new File(Config.value("tempDir"));
		File[] tempFiles = tempFile.listFiles();
		for (File idFile : tempFiles) {
			try {
				if (idFile.exists()) {
					idFile.delete();
				}
				Integer id = Integer.valueOf(idFile.getName());
				VideoDTO dto = dao.getDTOById(id);
				System.err.println(DateUtils.date() + " 当前：" + dto.getSeq());
				File[] tss = idFile.listFiles();
				File videoFile = new File("x:/success/" + dto.getFileName() + ".ts");
				try {
					FileOutputStream fs = new FileOutputStream(videoFile, true);
					FileChannel resultFileChannel = fs.getChannel();
					FileInputStream tfs;
					for (int i = 0; i < tss.length; i++) {
						tfs = new FileInputStream(tss[i]);
						FileChannel blk = tfs.getChannel();
						resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
						tfs.close();
						blk.close();
					}
					fs.close();
					resultFileChannel.close();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return true;
	}
}

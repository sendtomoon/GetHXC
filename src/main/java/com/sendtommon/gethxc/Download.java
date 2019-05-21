package com.sendtommon.gethxc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.List;

import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.M3U8DTO;
import com.sendtommon.gethxc.mongo.MongoDAO;
import com.sendtommon.gethxc.utils.DateUtils;
import com.sendtommon.gethxc.utils.HttpUtils;

public class Download {
	public static int connTimeout = 60 * 1000;
	public static int readTimeout = 60 * 1000;

	public static void main(String[] args) {
//		List<GetListByTagRespDataDTO> list = MongoDAO.firstName();
		List<GetListByTagRespDataDTO> list = MongoToOracle.getWaitDown();
		for (int i = 0; i < list.size(); i++) {
			GetListByTagRespDataDTO dto = list.get(i);
			System.err.println(DateUtils.date() + " 开始下载第：" + dto.getSeq() + "。数量：" + dto.getSeeCount());
			try {
				Download.download(dto.getUrl(), dto.getFileName());
				MongoDAO.updateDownRes(dto.getID(), 1);// 下载成功，则修改状态为已下载
			} catch (M3U8NotFundException e) {
				e.printStackTrace();
				MongoDAO.updateDownRes(dto.getID(), 2);// 找不到M3U8
				MongoDAO.updateFail(dto.getID(), 1);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				MongoDAO.updateFail(dto.getID(), 1);
				continue;
			}
		}

	}

	private static void download(String m3u8url, String fileName) throws Exception {
		deleteDir(new File(Config.value("tempDir")));
		File tfile = new File(Config.value("tempDir"));
		if (!tfile.exists()) {
			tfile.mkdirs();
		}
		M3U8DTO M3U8 = getM3U8ByURL(m3u8url);
		if (null == M3U8) {
			throw new M3U8NotFundException("M3U8获取失败" + "   " + fileName);
		}
		String basePath = M3U8.getBasepath();
		int size = M3U8.getTsList().size();
		for (int i = 0; i < size; i++) {
			M3U8DTO.Ts ts = M3U8.getTsList().get(i);
			File file = new File(Config.value("tempDir") + File.separator + ts.getFile());
			if (!file.exists()) {
				try {
					System.err.println(DateUtils.date() + "当前进度" + new BigDecimal(i)
							.divide(new BigDecimal(size), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100))
							+ "%");
					HttpUtils.download(basePath + ts.getFile(), "127.0.0.1:1080", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		M3U8.getTsList().stream().parallel().forEach(ts -> {
//			File file = new File(Config.value("tempDir") + File.separator + ts.getFile());
//			if (!file.exists()) {
//				try {
//					HttpUtils.download(basePath + ts.getFile(), "127.0.0.1:1080", file);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		System.out.println(DateUtils.date() + "下载成功！");
		mergeFiles(tfile.listFiles(), Config.value("downloadDir") + fileName + ".ts");
	}

	public static M3U8DTO getM3U8ByURL(String m3u8URL) {
		try {
			return HttpUtils.getM3u8(m3u8URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean mergeFiles(File[] fpaths, String resultPath) {
		if (fpaths == null || fpaths.length < 1) {
			return false;
		}

		if (fpaths.length == 1) {
			return fpaths[0].renameTo(new File(resultPath));
		}
		for (int i = 0; i < fpaths.length; i++) {
			if (!fpaths[i].exists() || !fpaths[i].isFile()) {
				return false;
			}
		}
		File resultFile = new File(resultPath);

		try {
			FileOutputStream fs = new FileOutputStream(resultFile, true);
			FileChannel resultFileChannel = fs.getChannel();
			FileInputStream tfs;
			for (int i = 0; i < fpaths.length; i++) {
				tfs = new FileInputStream(fpaths[i]);
				FileChannel blk = tfs.getChannel();
				resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
				tfs.close();
				blk.close();
			}
			fs.close();
			resultFileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}

package com.sendtomoon.gethxc.servie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendtomoon.gethxc.DAO;
import com.sendtomoon.gethxc.M3U8NotFundException;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.dto.M3U8DTO;
import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.utils.DownloadUtils;

@Service
public class DownloadService {
	public static int connTimeout = 60 * 1000;
	public static int readTimeout = 60 * 1000;

	@Autowired
	DAO dao;

	@Autowired
	private DownloadUtils du;

	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	public void mainDown() {
		List<VideoDTO> list = dao.getWaitDown();
		for (int i = 0; i < list.size(); i++) {
			VideoDTO dto = list.get(i);
			System.out.println("----开始下载第：" + dto.getSeq() + "。SEQ：" + dto.getSeq());
			try {
				this.download(dto.getUrl(), dto.getFileName(), String.valueOf(dto.getID()));
				dto.setDownloaded(1);
				dao.update(dto);
			} catch (M3U8NotFundException e) {
				e.printStackTrace();
				dto.setDownloaded(2);
				dao.update(dto);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				dto.setDownloaded(1);
				dao.update(dto);
				continue;
			}
		}

	}

	public void download(String m3u8url, String fileName, String id) throws Exception {
		File tfile = new File(Config.value("tempDir") + "/" + id + "/");
		tfile.mkdirs();
		M3U8DTO M3U8 = getM3U8ByURL(m3u8url);
		if (null == M3U8) {
			throw new M3U8NotFundException("M3U8获取失败" + "   " + fileName);
		}
//		String basePath = M3U8.getBasepath();
		M3U8.getTsList().stream().parallel().forEach(ts -> {
			String[] arr = ts.getFile().split("/");
			File file = new File(Config.value("tempDir") + "/" + id + "/" + File.separator + arr[arr.length - 1]);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(125);
				} catch (InterruptedException e1) {
				}
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							du.download(ts.getFile(), null, file);
						} catch (Exception e) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", id);
							map.put("url", ts.getFile());
							map.put("file", file.getAbsolutePath());
							dao.addError(map);
							e.printStackTrace();
						}
					}
				});
			}
		});
		System.out.println("SEQ:" + id + "----加入下载队列成功");
//		mergeFiles(tfile.listFiles(), Config.value("downloadDir") + fileName + ".ts");
	}

	public M3U8DTO getM3U8ByURL(String m3u8URL) {
		try {
			return du.getM3u8(m3u8URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean mergeFiles(File[] fpaths, String resultPath) {
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
}

package com.sendtomoon.gethxc.servie;

import java.io.File;
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
import com.sendtomoon.gethxc.utils.ThreadSleepUtils;

@Service
public class DownloadService {
	public static int connTimeout = 60 * 1000;
	public static int readTimeout = 60 * 1000;

	@Autowired
	DAO dao;

	@Autowired
	private DownloadUtils du;

	@Autowired
	ThreadSleepUtils tsu;

//	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);

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
			tsu.sleep(8000);
			String[] arr = ts.getFile().split("/");
			File file = new File(Config.value("tempDir") + "/" + id + "/" + File.separator + arr[arr.length - 1]);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							tsu.sleep();
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
	}

	public M3U8DTO getM3U8ByURL(String m3u8URL) {
		try {
			return du.getM3u8(m3u8URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

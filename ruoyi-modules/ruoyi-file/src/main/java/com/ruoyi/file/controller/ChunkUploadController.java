package com.ruoyi.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.file.FileTypeUtils;
import com.ruoyi.common.core.utils.uuid.Seq;
import org.apache.commons.io.FilenameUtils;

@RestController
@RequestMapping("/chunk")
public class ChunkUploadController
{
    private static final Logger log = LoggerFactory.getLogger(ChunkUploadController.class);

    @Value("${file.path}")
    private String localFilePath;

    @Value("${file.domain}")
    private String domain;

    @Value("${file.prefix}")
    private String localFilePrefix;

    @PostMapping("/upload")
    public R<?> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("chunkIndex") Integer chunkIndex,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam("fileName") String fileName)
    {
        try
        {
            String chunkDir = localFilePath + File.separator + "chunks" + File.separator + fileMd5;
            File dir = new File(chunkDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            String chunkFileName = chunkIndex + ".part";
            File chunkFile = new File(chunkDir + File.separator + chunkFileName);
            file.transferTo(chunkFile);

            Map<String, Object> result = new HashMap<>();
            result.put("chunkIndex", chunkIndex);
            result.put("uploaded", true);
            return R.ok(result);
        }
        catch (Exception e)
        {
            log.error("分片上传失败", e);
            return R.fail("分片上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public R<?> checkChunks(@RequestParam("fileMd5") String fileMd5,
                            @RequestParam("totalChunks") Integer totalChunks)
    {
        try
        {
            String chunkDir = localFilePath + File.separator + "chunks" + File.separator + fileMd5;
            File dir = new File(chunkDir);

            Map<String, Object> result = new HashMap<>();
            result.put("fileMd5", fileMd5);

            if (!dir.exists() || !dir.isDirectory())
            {
                result.put("uploadedChunks", new Integer[0]);
                result.put("complete", false);
                return R.ok(result);
            }

            Integer[] uploadedChunks;
            try (Stream<Path> paths = Files.list(dir.toPath()))
            {
                uploadedChunks = paths
                    .filter(p -> p.toString().endsWith(".part"))
                    .map(p -> {
                        String name = p.getFileName().toString();
                        return Integer.parseInt(name.replace(".part", ""));
                    })
                    .toArray(Integer[]::new);
            }

            result.put("uploadedChunks", uploadedChunks);
            result.put("complete", uploadedChunks.length == totalChunks);
            return R.ok(result);
        }
        catch (Exception e)
        {
            log.error("检查分片失败", e);
            return R.fail("检查分片失败: " + e.getMessage());
        }
    }

    @PostMapping("/merge")
    public R<?> mergeChunks(
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("fileName") String fileName,
            @RequestParam("totalChunks") Integer totalChunks)
    {
        try
        {
            String chunkDir = localFilePath + File.separator + "chunks" + File.separator + fileMd5;
            File dir = new File(chunkDir);

            if (!dir.exists())
            {
                return R.fail("分片目录不存在");
            }

            String extension = FileTypeUtils.getExtension(fileName);
            String datePath = DateUtils.datePath();
            String baseName = FilenameUtils.getBaseName(fileName);
            String finalFileName = datePath + "/" + baseName + "_" + Seq.getId(Seq.uploadSeqType) + "." + extension;
            String finalFilePath = localFilePath + File.separator + finalFileName;

            File finalFile = new File(finalFilePath);
            if (!finalFile.getParentFile().exists())
            {
                finalFile.getParentFile().mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(finalFile))
            {
                for (int i = 0; i < totalChunks; i++)
                {
                    File chunkFile = new File(chunkDir + File.separator + i + ".part");
                    if (!chunkFile.exists())
                    {
                        return R.fail("分片 " + i + " 不存在，合并失败");
                    }
                    try (FileInputStream fis = new FileInputStream(chunkFile))
                    {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = fis.read(buffer)) != -1)
                        {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }

            for (int i = 0; i < totalChunks; i++)
            {
                File chunkFile = new File(chunkDir + File.separator + i + ".part");
                chunkFile.delete();
            }
            dir.delete();

            String url = domain + localFilePrefix + "/" + finalFileName;

            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("name", fileName);
            return R.ok(result);
        }
        catch (Exception e)
        {
            log.error("合并分片失败", e);
            return R.fail("合并分片失败: " + e.getMessage());
        }
    }
}

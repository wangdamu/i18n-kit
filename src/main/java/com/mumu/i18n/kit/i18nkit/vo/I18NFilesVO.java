package com.mumu.i18n.kit.i18nkit.vo;

import java.util.List;

/**
 * 国际化文件列表 <br>
 * 国际化文件列表
 * <p>
 * Copyright: Copyright (c) 17-10-13 下午4:04
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Peter peter.wang@mulberrylearning.cn
 * @version 1.0
 */
public class I18NFilesVO {
    private Resource source;

    private List<Resource> targetedResources;

    public Resource getSource() {
        return source;
    }

    public void setSource(Resource source) {
        this.source = source;
    }

    public List<Resource> getTargetedResources() {
        return targetedResources;
    }

    public void setTargetedResources(List<Resource> targetedResources) {
        this.targetedResources = targetedResources;
    }

    public static class Resource{
        private String filePath;
        private String language;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}

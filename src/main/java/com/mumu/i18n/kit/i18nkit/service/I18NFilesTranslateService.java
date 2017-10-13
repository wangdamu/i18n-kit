package com.mumu.i18n.kit.i18nkit.service;

import com.mumu.i18n.kit.i18nkit.vo.I18NFilesVO;

/**
 * 注释概要 <br>
 * 注释说明
 * <p>
 * Copyright: Copyright (c) 17-10-13 下午4:01
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Peter peter.wang@mulberrylearning.cn
 * @version 1.0
 */
public interface I18NFilesTranslateService {

    /**
     * 翻译
     * @param i18NFiles
     */
    void translate(I18NFilesVO i18NFiles);
}

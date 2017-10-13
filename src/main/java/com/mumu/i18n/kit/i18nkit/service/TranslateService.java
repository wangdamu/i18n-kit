package com.mumu.i18n.kit.i18nkit.service;

/**
 * 翻译Service <br>
 * 翻译Service
 * <p>
 * Copyright: Copyright (c) 17-10-13 下午3:19
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Peter peter.wang@mulberrylearning.cn
 * @version 1.0
 */
public interface TranslateService {

    /**
     * 翻译
     * @param text 要翻译的文字
     * @param from 源语音
     * @param to 目标语言
     * @return
     */
    String translate(String text, String from, String to);
}

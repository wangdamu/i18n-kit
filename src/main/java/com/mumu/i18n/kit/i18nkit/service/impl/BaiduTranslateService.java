package com.mumu.i18n.kit.i18nkit.service.impl;

import com.jayway.jsonpath.JsonPath;
import com.mumu.i18n.kit.i18nkit.service.TranslateService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 百度翻译 <br>
 * 百度翻译
 * <p>
 * Copyright: Copyright (c) 17-10-13 下午3:21
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Peter peter.wang@mulberrylearning.cn
 * @version 1.0
 */
@Service
public class BaiduTranslateService implements TranslateService{
    private static String API_URL = "http://fanyi.baidu.com/v2transapi";

    @Override
    public String translate(String text, String from, String to) {
        try {
            HttpResponse resp = Request.Post(API_URL)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("Referer", "http://fanyi.baidu.com/translate?aldtype=16047&query=%E6%89%8B%E6%9C%BA%E9%AA%8C%E8%AF%81%E7%A0%81%E9%94%99%E8%AF%AF&keyfrom=baidu&smartresult=dict&lang=auto2zh")
                    .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36")
                    .bodyForm(
                            Arrays.asList(
                                    new BasicNameValuePair("from", from),
                                    new BasicNameValuePair("to", to),
                                    new BasicNameValuePair("query", text),
                                    new BasicNameValuePair("transtype", "translang"),
                                    new BasicNameValuePair("simple_means_flag", "3")),
                            Charset.forName("UTF-8")
                    ).execute().returnResponse();

            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                InputStream is = resp.getEntity().getContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                IOUtils.copy(is, baos);

                String content = new String(baos.toByteArray(), Charset.forName("UTF-8"));
                String value = JsonPath.read(content, "$.trans_result.data[0].dst");
                return value;
            }else{
                throw new RuntimeException("Request Failed. code=" + resp.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

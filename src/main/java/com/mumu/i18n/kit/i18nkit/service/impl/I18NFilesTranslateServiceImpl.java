package com.mumu.i18n.kit.i18nkit.service.impl;

import com.mumu.i18n.kit.i18nkit.service.I18NFilesTranslateService;
import com.mumu.i18n.kit.i18nkit.service.TranslateService;
import com.mumu.i18n.kit.i18nkit.vo.I18NFilesVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 注释概要 <br>
 * 注释说明
 * <p>
 * Copyright: Copyright (c) 17-10-13 下午4:13
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Peter peter.wang@mulberrylearning.cn
 * @version 1.0
 */
@Service
public class I18NFilesTranslateServiceImpl implements I18NFilesTranslateService{

    @Autowired
    private TranslateService translateService;

    @Override
    public void translate(I18NFilesVO i18NFiles) {
        List<IEntry> sourceEntryList = readI18NFile(i18NFiles.getSource().getFilePath(), true);
        i18NFiles.getTargetedResources().forEach(t->{
            List<IEntry> entries = readI18NFile(t.getFilePath(), false);
            Map<String, String> map = toKeyValueMap(entries);

            List<IEntry> targetList = sourceEntryList.stream().map(entry->{
                if(entry instanceof Pair){
                    Pair pair = (Pair) entry;
                    String existedValue = map.get(pair.getKey());
                    if(existedValue != null){
                        return new Pair(pair.getKey(), existedValue);
                    }
                    String translated = translateService.translate(pair.getValue(), i18NFiles.getSource().getLanguage(), t.getLanguage());
                    System.out.println(pair.getKey() + "=====" + translated);
                    return new Pair(pair.getKey(), translated);
                }else{
                    return entry;
                }
            }).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            targetList.forEach(target->{
                sb.append(target.getText()).append("\n");
            });

            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(t.getFilePath()), Charset.forName("UTF-8")))){
                bw.append(sb.toString());
                //ps.println(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Map<String, String> toKeyValueMap(List<IEntry> entryList){
        Map<String, String> map = new HashMap<>();
        entryList.stream().filter(t->t instanceof Pair).forEach(t->{
            Pair pair = (Pair)t;
            map.put(pair.getKey(), pair.getValue());
        });
        return map;
    }

    /**
     * 读取i8n文件
     * @param filePath
     * @return
     */
    private List<IEntry> readI18NFile(String filePath, boolean unicodeToStr){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")))){
            String line = null;
            List<IEntry> entryList = new ArrayList<>();
            while((line = br.readLine()) != null){
                if(StringUtils.isBlank(line)){
                    entryList.add(new Comment(line));
                }else if(line.trim().startsWith("#")){
                    entryList.add(new Comment(line));
                }else{
                    String[] kv = StringUtils.split(line, "=", 2);
                    String value = unicodeToStr?unicodeToChina(kv[1]):kv[1];
                    entryList.add(new Pair(kv[0], value));
                }
            }
            return entryList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String unicodeToChina(String str) {
        Charset set = Charset.forName("UTF-16");
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher( str );
        int start = 0 ;
        int start2 = 0 ;
        StringBuffer sb = new StringBuffer();
        while( m.find( start ) ) {
            start2 = m.start() ;
            if( start2 > start ){
                String seg = str.substring(start, start2) ;
                sb.append( seg );
            }
            String code = m.group( 1 );
            int i = Integer.valueOf( code , 16 );
            byte[] bb = new byte[ 4 ] ;
            bb[ 0 ] = (byte) ((i >> 8) & 0xFF );
            bb[ 1 ] = (byte) ( i & 0xFF ) ;
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append( String.valueOf( set.decode(b) ).trim() );
            start = m.end() ;
        }
        start2 = str.length() ;
        if( start2 > start ){
            String seg = str.substring(start, start2) ;
            sb.append( seg );
        }
        return sb.toString() ;
    }

    private interface IEntry{
        String getText();
    }

    private class Comment implements IEntry{
        private final String comment;

        public Comment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        @Override
        public String getText() {
            return comment;
        }
    }

    private class Pair implements IEntry{
        private final String key;
        private final String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String getText() {
            return key + "=" + value;
        }
    }
}

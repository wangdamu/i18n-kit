package com.mumu.i18n.kit.i18nkit;

import com.mumu.i18n.kit.i18nkit.service.I18NFilesTranslateService;
import com.mumu.i18n.kit.i18nkit.service.TranslateService;
import com.mumu.i18n.kit.i18nkit.vo.I18NFilesVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class I18nKitApplicationTests {

	@Autowired
	private TranslateService translateService;

	@Autowired
	private I18NFilesTranslateService i18NFilesTranslateService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testBaiduTranslate(){
		String result = translateService.translate("删除失败", "zh", "cht");
		System.out.println(result);
	}

	@Test
	public void testI18NFiles(){
		String sourceFilePath = "/media/peter/Data/workspace/pims/src/trunk/pims-backend/pims-rest/api-centre/src/main/resources/i18n/validation_zh.properties";

		String sourceLanguage = "zh";

		String[] targetFilePathes = {
				"/media/peter/Data/workspace/pims/src/trunk/pims-backend/pims-rest/api-centre/src/main/resources/i18n/validation_en.properties",
				"/media/peter/Data/workspace/pims/src/trunk/pims-backend/pims-rest/api-centre/src/main/resources/i18n/validation_zh_HK.properties",
				"/media/peter/Data/workspace/pims/src/trunk/pims-backend/pims-rest/api-centre/src/main/resources/i18n/validation_zh_TW.properties"
		};

		String[] targetLanguages = {"en", "cht", "cht"};

		I18NFilesVO.Resource source = new I18NFilesVO.Resource();
		source.setFilePath(sourceFilePath);
		source.setLanguage(sourceLanguage);

		List<I18NFilesVO.Resource> targetList = new ArrayList<>();
		for(int i = 0; i < targetFilePathes.length; i++){
			I18NFilesVO.Resource target = new I18NFilesVO.Resource();
			target.setFilePath(targetFilePathes[i]);
			target.setLanguage(targetLanguages[i]);
			targetList.add(target);
		}

		I18NFilesVO i18NFilesVO = new I18NFilesVO();
		i18NFilesVO.setSource(source);
		i18NFilesVO.setTargetedResources(targetList);

		i18NFilesTranslateService.translate(i18NFilesVO);
	}
}

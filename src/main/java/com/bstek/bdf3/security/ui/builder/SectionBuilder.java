package com.bstek.bdf3.security.ui.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.accordion.Section;

@Component("maintain.sectionForIntroBuilder")
public class SectionBuilder extends AbstractBuilder<Section> {
	
	@Override
	protected Collection<Control> getChildren(Section sect){
		if(sect.getControl()!=null){
			return Arrays.asList(sect.getControl());
		}
		return new ArrayList<>();
	}

	@Override
	protected String getId(Section sect){
		String caption = sect.getCaption();
		if (StringUtils.isEmpty(caption)) {
			caption = sect.getName();
		}
		return caption;
	}

}

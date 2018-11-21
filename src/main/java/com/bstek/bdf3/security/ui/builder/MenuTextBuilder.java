package com.bstek.bdf3.security.ui.builder;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.TextMenuItem;

@Component("maintain.menuTextBuilder")
public class MenuTextBuilder extends AbstractBuilder<TextMenuItem> {

	
	@Override
	protected String getId(TextMenuItem item){
		String id=item.getName();
		if(StringUtils.isEmpty(id)){
			id=item.getCaption();
		}
		return id;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected Collection<BaseMenuItem> getChildren(TextMenuItem item){
		return item.getItems();
	}
	
	
	@Override
	protected String getDesc(TextMenuItem item){
		String desc = super.getDesc(item);
		if (desc != null) {
			return desc;
		}
		if(StringUtils.isNotEmpty(item.getName())){
			return item.getCaption();
		}
		return null;
	}


}
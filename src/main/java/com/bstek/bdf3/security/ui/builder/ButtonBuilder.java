package com.bstek.bdf3.security.ui.builder;


import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.toolbar.MenuButton;


@Component("maintain.buttonBuilder")
public class ButtonBuilder extends AbstractBuilder<Button> {

	@Override
	protected Collection<BaseMenuItem> getChildren(Button btn){
		if (MenuButton.class.isAssignableFrom(btn.getClass())) {
			return ((MenuButton) btn).getItems();
		}
		return Collections.emptyList();
	}
	
	
	
	@Override
	protected String getIcon(Button btn) {
		if(StringUtils.isNotEmpty(btn.getIcon())){
			return btn.getIcon();
		}
		return super.getIcon(btn);
	}

	@Override
	protected String getId(Button btn){
		String id = btn.getId();
		if (StringUtils.isEmpty(id)) {
			id = btn.getCaption();
		}
		return id;
	}
	
	@Override
	protected String getDesc(Button btn){
		String desc = super.getDesc(btn);
		if (desc != null) {
			return desc;
		}
		if(btn.getId()!=null){
			return btn.getCaption();
		}
		return null;
	}
}
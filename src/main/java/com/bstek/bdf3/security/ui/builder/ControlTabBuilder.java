package com.bstek.bdf3.security.ui.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.base.tab.ControlTab;

@Component("maintain.controlTabBuilder")
public class ControlTabBuilder extends AbstractBuilder<ControlTab> {

	@Override
	protected Collection<Control> getChildren(ControlTab ctrlTab){
		if(null != ctrlTab.getControl()){
			return Arrays.asList(ctrlTab.getControl());
		}
		return new ArrayList<>();
	}
	@Override
	protected String getId(ControlTab ctrlTab){
		String id=ctrlTab.getName();
		if(StringUtils.isEmpty(id)){
			id=ctrlTab.getCaption();
		}
		return id;
	}
	@Override
	protected String getDesc(ControlTab controlTab){
		String desc = super.getDesc(controlTab);
		if (desc != null) {
			return desc;
		}
		if(StringUtils.isNotEmpty(controlTab.getName())){
			return controlTab.getCaption();		
		}
		return null;
	}




}


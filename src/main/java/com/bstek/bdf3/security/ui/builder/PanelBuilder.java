package com.bstek.bdf3.security.ui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.Panel;

@org.springframework.stereotype.Component("maintain.panelBuilder")
public class PanelBuilder extends AbstractBuilder<Panel> {
	

	@Override
	protected String getId(Panel pan){
		String id=pan.getId();
		if(StringUtils.isEmpty(id)){
			id=pan.getCaption();
		}
		return id;
	}
	
	
	@Override
	protected String getDesc(Panel pan){
		String desc = super.getDesc(pan);
		if (desc != null) {
			return desc;
		}
		if(StringUtils.isNotEmpty(pan.getId())){
			return pan.getCaption();		
		}
		return null;
	}
	
	@Override
	protected Collection<Component> getChildren(Panel pan){
		Collection<Component> children =new ArrayList<>();
		children.addAll((Collection<? extends Component>) pan.getTools());
		children.addAll((Collection<? extends Component>) pan.getChildren());
		children.addAll((Collection<? extends Component>) pan.getButtons());
		return children;
	}


}

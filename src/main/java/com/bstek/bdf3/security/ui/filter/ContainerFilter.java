package com.bstek.bdf3.security.ui.filter;

import java.util.Collection;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.base.Panel;

@org.springframework.stereotype.Component
public class ContainerFilter extends AbstractFilter<Container> {
	@Override	
	protected Collection<Component> getChildren(Container container){
		return container.getChildren();
	}
	@Override
	public boolean support(Object control) {
		return (control instanceof Container) && !(control instanceof Panel);
	}

}


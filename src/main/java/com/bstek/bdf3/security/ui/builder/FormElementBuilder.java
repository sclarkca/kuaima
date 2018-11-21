package com.bstek.bdf3.security.ui.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;

@Component("maintain.formElementBuilder")
public class FormElementBuilder extends AbstractBuilder<FormElement> {
	@Override
	protected Collection<Control> getChildren(FormElement elet){
		if(elet.getEditor()!=null){
			return Arrays.asList(elet.getEditor());
		}
		return new ArrayList<>();
	}
	@Override
	protected String getId(FormElement elet){
		String eleId = elet.getId();
		if (StringUtils.isEmpty(eleId)) {
			eleId = elet.getProperty();
		}
		if(StringUtils.isEmpty(eleId)){
			eleId = elet.getLabel();
		}
		if (StringUtils.isEmpty(eleId)) {
			ViewElement viewElement = elet.getParent();
			if (viewElement instanceof AutoForm) {
				EntityDataType entityDataType = ((AutoForm) viewElement).getDataType();
				Map<String, PropertyDef> dataTypePropertyDefs = null;
				if (entityDataType != null) {
					dataTypePropertyDefs = entityDataType.getPropertyDefs();
				}
				eleId = getFormElementLabel(elet, dataTypePropertyDefs);
			}
		}
		return eleId;
	}
	

	private String getFormElementLabel(FormElement elet, Map<String, PropertyDef> map) {
		String property = elet.getProperty();
		if (StringUtils.isNotEmpty(property) && map != null) {
			PropertyDef pd = map.get(property);
			if (pd != null && StringUtils.isNotEmpty(pd.getLabel())) {
				return pd.getLabel();
			}
		}
		return property;
	}

}

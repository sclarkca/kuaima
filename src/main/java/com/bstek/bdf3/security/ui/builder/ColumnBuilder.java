package com.bstek.bdf3.security.ui.builder;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;

@Component("intro.maintain.columnBuilder")
public class ColumnBuilder extends AbstractBuilder<Column> {
	@Override
	protected String getId(Column colm){
		String id = colm.getName();
		if (StringUtils.isEmpty(id)) {
			id = colm.getCaption();
		}
		return id;
	}
	@Override
	protected Collection<Column> getChildren(Column column){
		if(column instanceof ColumnGroup){
			return ((ColumnGroup)column).getColumns();
		}
		return null;
	}




}


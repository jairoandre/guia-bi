package br.com.vah.guiabi.controllers;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import br.com.vah.guiabi.entities.dbamv.GuiaMv;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.GuiaMvService;

public class GuiaMvCtrl extends AbstractCtrl<GuiaMv>{

	
	@Inject
	private transient Logger logger;
	
	@Inject
	private GuiaMvService service;
	
	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created");
		setItem(createNewItem());
		initLazyModel(service);
	}
	
	@Override
	public DataAccessService<GuiaMv> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public GuiaMv createNewItem() {
		return new GuiaMv();
	}

	@Override
	public String path() {
		return "guiaMv";
	}

	@Override
	public String getEntityName() {
		return "GuiaMv";
	}
}

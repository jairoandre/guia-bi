package br.com.vah.guiabi.service;

import javax.ejb.Stateless;

import br.com.vah.guiabi.entities.dbamv.GuiaMv;

@Stateless
public class GuiaMvService extends DataAccessService<GuiaMv>{
	
	public GuiaMvService(){
		super(GuiaMv.class);
	}
}

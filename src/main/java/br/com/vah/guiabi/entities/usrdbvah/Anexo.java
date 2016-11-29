package br.com.vah.guiabi.entities.usrdbvah;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.guiabi.entities.BaseEntity;

@Entity
@Table(name = "TB_GUIABI_ANEXO", schema = "USRDBVAH")
public class Anexo extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqAnexo", sequenceName = "SEQ_GUIABI_ANEXO", schema = "USRDBVAH", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAnexo")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_GUIA")
	private Guia guia;
	
	@Column( name="NM_ANEXO" )
	private String nmAnexo;
	
	@Lob
	@Column(name = "VL_ARQUIVO")
	private byte[] arquivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Guia getGuia() {
		return guia;
	}

	public void setGuia(Guia guia) {
		this.guia = guia;
	}

	public String getNmAnexo() {
		return nmAnexo;
	}

	public void setNmAnexo(String nmAnexo) {
		this.nmAnexo = nmAnexo;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
	public Integer getTamanhoArquivo() {
	    return arquivo.length;
	}
	
	@Override
	public String getLabelForSelectItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

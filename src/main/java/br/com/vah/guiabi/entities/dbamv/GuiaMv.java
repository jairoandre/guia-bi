package br.com.vah.guiabi.entities.dbamv;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.guiabi.entities.BaseEntity;

@Entity
@Table(name = "GUIA", schema = "DBAMV")
@NamedQueries({
	@NamedQuery(name="CdGuia.Max", query="SELECT MAX (g.id) FROM GuiaMv g")
	
})
public class GuiaMv extends BaseEntity {
	
	@Id
	@Column(name = "CD_GUIA")
	private Long id;
	
	@Column(name = "NR_GUIA")
	private String guia;
	
	@Column(name = "TP_GUIA")
	private String tipoGuia;
	
	@ManyToOne
	@JoinColumn(name = "CD_ATENDIMENTO")
	private Atendimento atendimento;
	
	@Column(name = "CD_TIP_ACOM")
	private Integer tipoAcomodacao;
	
	@ManyToOne
	@JoinColumn(name = "CD_PACIENTE")
	private Paciente paciente;
	
	@Column (name = "CD_CONVENIO")
	private Integer convenio;
	
	@Column(name = "DS_JUSTIFICATIVA")
	private String justificativa;
	
	@Column(name = "DT_SOLICITACAO")
	private Date dataSolicitacao;
	
	@Column(name = "DT_AUTORIZACAO")
	private Date dataAutorizacao;
	
	@Column(name = "DT_NEGACAO")
	private Date dataNegacao;
	
	@Column(name = "NR_DIAS_SOLICITADOS")
	private Integer diasSolicitados;
	
	@Column(name = "NR_DIAS_AUTORIZADOS")
	private Integer diasAutorizadas;
		
	@Column(name = "CD_SENHA")
	private String senha;
	
	@Column(name = "DS_OBSERVACAO")
	private String observacao;
	
	@Column(name = "DT_GERACAO")
	private Date criacao;
	
	@Column(name = "NM_AUTORIZADOR_CONV")
	private String nomeAutorizador;
	
	@Column(name = "TP_SITUACAO")
	private String tipoSituacao;
	
	@Column(name = "SN_CIRURGIA_EMERGENCIA")
	private String emergencia;
	
	@Column(name = "SN_SOLICITACAO_URGENTE")
	private String urgente;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGuia() {
		return guia;
	}

	public void setGuia(String guia) {
		this.guia = guia;
	}

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Integer getTipoAcomodacao() {
		return tipoAcomodacao;
	}

	public void setTipoAcomodacao(Integer tipoAcompanhante) {
		this.tipoAcomodacao = tipoAcompanhante;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Integer getConvenio() {
		return convenio;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public Date getDataNegacao() {
		return dataNegacao;
	}

	public void setDataNegacao(Date dataNegacao) {
		this.dataNegacao = dataNegacao;
	}

	public Integer getDiasSolicitados() {
		return diasSolicitados;
	}

	public void setDiasSolicitados(Integer diasSolicitados) {
		this.diasSolicitados = diasSolicitados;
	}

	public Integer getDiasAutorizadas() {
		return diasAutorizadas;
	}

	public void setDiasAutorizadas(Integer diasAutorizadas) {
		this.diasAutorizadas = diasAutorizadas;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getCriacao() {
		return criacao;
	}

	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}

	public String getNomeAutorizador() {
		return nomeAutorizador;
	}

	public void setNomeAutorizador(String nomeAutorizador) {
		this.nomeAutorizador = nomeAutorizador;
	}

	public String getTipoSituacao() {
		return tipoSituacao;
	}

	public void setTipoSituacao(String tipoSituacao) {
		this.tipoSituacao = tipoSituacao;
	}

	public String getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(String emergencia) {
		this.emergencia = emergencia;
	}

	public String getUrgente() {
		return urgente;
	}

	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}

	@Override
	public String getLabelForSelectItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}

-- DDL SISTEMA GUIA BI

-- GUIA

CREATE TABLE USRDBVAH.TB_GUIABI_GUIA
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  CD_SETOR NUMBER(*),
  CD_ATENDIMENTO NUMBER(*),
  CD_ESPECIALIDADE NUMBER(*),
  CD_CONVENIO NUMBER(*),
  DT_GUIA DATE,
  DT_RECEBIMENTO DATE,
  DT_AUDITORIA DATE,
  DT_REPOSTA_CONVENIO DATE,
  DT_SOLICITACAO_CONVENIO DATE,
  CD_TIPO VARCHAR2(25),
  CD_ESTADO VARCHAR2(25),
  CD_DESCRICAO VARCHAR2(255),
  CONSTRAINT FK_GUIABI_GUIA_SETOR FOREIGN KEY (CD_SETOR) REFERENCES DBAMV.TB_SETOR (CD_SETOR),
  CONSTRAINT FK_GUIABI_GUIA_ANTEDIME FOREIGN KEY (CD_ATENDIMENTO) REFERENCES DBAMV.TB_ATENDIME (CD_ATENDIMENTO),
  CONSTRAINT FK_GUIABI_GUIA_ESPECIALID FOREIGN KEY (CD_ESPECIALIDADE) REFERENCES DBAMV.ESPECIALID (CD_ESPECIALID)
);


CREATE SEQUENCE USRDBVAH.SEQ_GUIABI_GUIA;

-- USUÁRIO

CREATE TABLE USRDBVAH.TB_GUIABI_USUARIO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  DS_LOGIN VARCHAR2(255),
  NM_TITULO VARCHAR2(255),
  CD_ROLE VARCHAR2(50)
);

CREATE SEQUENCE USRDBVAH.SEQ_GUIABI_USUARIO;

-- HISTORICO GUIA

CREATE TABLE USRDBVAH.TB_GUIABI_HISTORICO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_USER NUMBER(*),
  ID_GUIA NUMBER(*),
  DT_ALTERACAO DATE,
  CD_ACAO VARCHAR2(50),
  CONSTRAINT FK_GUIABI_HISTORICO_USUARIO FOREIGN KEY (ID_USER) REFERENCES USRDBVAH.TB_GUIABI_USUARIO (ID),
  CONSTRAINT FK_GUIABI_HISTORICO_GUIA FOREIGN KEY (ID_GUIA) REFERENCES USRDBVAH.TB_GUIABI_GUIA (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_GUIABI_HISTORICO;

-- TABELA COMENTARIO

CREATE TABLE USRDBVAH.TB_GUIABI_COMENTARIO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_USER NUMBER(*),
  ID_GUIA NUMBER(*),
  DT_CRIACAO DATE,
  NM_COMENTARIO VARCHAR2(255),
  CONSTRAINT FK_GUIABI_COMENTARIO_USUARIO FOREIGN KEY (ID_USER) REFERENCES USRDBVAH.TB_GUIABI_USUARIO (ID),
  CONSTRAINT FK_GUIABI_COMENTARIO_GUIA FOREIGN KEY (ID_GUIA) REFERENCES USRDBVAH.TB_GUIABI_GUIA (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_GUIABI_COMENTARIO;

-- TABELA DE RELACIONAMENTO ENTRE GUIAS E PROCEDIMENTOS

CREATE TABLE USRDBVAH.TB_GUIABI_GUIA_PROFAT
(
  ID NUMBER(*),
  CD_PRO_FAT VARCHAR2(8),
  CONSTRAINT TB_GUIABI_GUIA_PROFAT_PK PRIMARY KEY (ID, CD_PRO_FAT),
  CONSTRAINT FK_GUIABI_GUIA_PROFAT_1 FOREIGN KEY (ID) REFERENCES USRDBVAH.TB_GUIABI_GUIA (ID),
  CONSTRAINT FK_GUIABI_GUIA_PROFAT_2 FOREIGN KEY (CD_PRO_FAT) REFERENCES DBAMV.PRO_FAT (CD_PRO_FAT)
);

CREATE TABLE USRDBVAH.TB_GUIABI_ANEXO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_GUIA NUMBER(*) NOT NULL,
  NM_ANEXO VARCHAR2(50) NOT NULL,
  VL_ARQUIVO BLOB NOT NULL,
  CONSTRAINT TB_GUIABI_ANEXO_FK1 FOREIGN KEY (ID_GUIA) REFERENCES USRDBVAH.TB_GUIABI_GUIA (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_GUIABI_ANEXO;

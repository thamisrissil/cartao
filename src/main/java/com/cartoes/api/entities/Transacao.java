package com.cartoes.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "transacao")
public class Transacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "data_Transacao", nullable = false)
	private Date dataTransacao;

	@Column(name = "cnpj", nullable = false, length = 14)
	private String cnpj;

	@Column(name = "valor", nullable = false, length = 11, unique = true)
	private String valor;

	@Column(name = "qdt_Parcelas", nullable = false)
	private int qdt_Parcelas;

	@Column(name = "cartao_id", nullable = false)
	private int cartao_id;

	@Column(name = "juros", nullable = false)
	private int juros;

	@JsonBackReference
	@ManyToOne (fetch = FetchType.LAZY)
	private  Cartao cartao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getQdt_Parcelas() {
		return qdt_Parcelas;
	}

	public void setQdt_Parcelas(int qdt_Parcelas) {
		this.qdt_Parcelas = qdt_Parcelas;
	}

	public int getCartao_id() {
		return cartao_id;
	}

	public void setCartao_id(int cartao_id) {
		this.cartao_id = cartao_id;
	}

	public int getJuros() {
		return juros;
	}

	public void setJuros(int juros) {
		this.juros = juros;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	@PreUpdate
	public void preUpdate() {
		dataTransacao = new Date();
	}

	@PrePersist
	public void prePersist() {
		dataTransacao = new Date();
	}
	@Override
	 public String toString() {
		return "Transacao[" + "id=" + id + ","
				 + "data_Transacao=" + dataTransacao + ","
				 + "cnpj=" + cnpj + ","
				 + "valor=" + valor + ","
				 + "juros=" + juros + ","
				 + "cartao_id=" + cartao_id + ","
				 + "qdt_Parcelas=" + qdt_Parcelas + "]";

	 }
}

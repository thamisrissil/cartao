package com.cartoes.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

public class TransacaoDto {
	
	private String id;
	
	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message = "CNPJ inválido.")
	private String cnpj;
	
	@NotEmpty(message = "Quantidade de parcelas não pode ser vazio.")
	@Length(min = 1, max = 2, message = "Quantidade de parcelas deve conter 1 ou 2 caracteres.")	
	private String qtdParcelas;
	
	@NotEmpty(message = "Juros não pode ser vazio.")
	@Length(min = 1, max = 4, message = "Juros deve conter entre 1 e 4 caracteres.")
	private String juros;
	
	@NotEmpty(message = "Valor não pode ser vazio.")
	@Length(min = 1, max = 10, message = "Valor deve conter entre 1 e 10 caracteres.")
	private String valor;
	
		
	@NotEmpty(message = "O número do cartão não pode ser vazio.")
	@Length(min = 16, max = 16, message = "Numero do cartão deve conter 16 caracteres.")
	private String cartaoNumero;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(String qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public String getJuros() {
		return juros;
	}

	public void setJuros(String juros) {
		this.juros = juros;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getCartaoNumero() {
		return cartaoNumero;
	}

	public void setCartaoNumero(String cartaoNumero) {
		this.cartaoNumero = cartaoNumero;
	}
	
	@Override
	public String toString() {
		return "Transacao [id=" + id + ","				
				+ " cnpj=" + cnpj + ", valor=" + valor + ","
				+ " qtdParcelas=" + qtdParcelas + ","
				+ " juros=" + juros + ","
				+ " cartaoNumero=" + cartaoNumero + "]";
	}
}

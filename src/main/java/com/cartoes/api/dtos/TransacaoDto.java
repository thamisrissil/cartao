package com.cartoes.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

public class TransacaoDto {
	
	private String id;
	@NotEmpty(message = "cnpj não pode ser vazio")
	@Length(min = 1, max = 14, message = "o cnpj deve conter 14 caracteres")
		private String cnpj;
	
	@NotEmpty(message = "valor não pode ser vazio")
	@Length(min = 1, max = 10, message = "o valor deve conter 10 caracteres")
		private String valor;

	@NotEmpty(message = "quantidade de parcelas não pode ser vazio")
	@Length(min = 1, max = 2, message = "quantidade de parcelas deve conter 2 caracteres")
		private String qtdParcelas;
	
	@NotEmpty(message = "juros não pode ser vazio")
	@Length(min = 1, max = 4, message = "quantidade de juros deve conter 4 caracteres")
		private String juros;
	
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
	
}

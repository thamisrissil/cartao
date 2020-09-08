package com.cartoes.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
public class TransacaoDto {
	
	@NotEmpty(message = "cnpj não pode ser vazio")
	@Length(min = 14, max = 14, message = "o cnpj deve conter 14 caracteres")
		private String cnpj;
	
	@NotEmpty(message = "valor não pode ser vazio")
	@Length(min = 10, max = 10, message = "o valor deve conter 10 caracteres")
		private String valor;

	@NotEmpty(message = "quantidade de parcelas não pode ser vazio")
	@Length(min = 2, max = 2, message = "quantidade de parcelas deve conter 2 caracteres")
		private String qtdParcelas;
	
	@NotEmpty(message = "juros não pode ser vazio")
	@Length(min = 4, max = 4, message = "quantidade de juros deve conter 4 caracteres")
		private String juros;
	
}

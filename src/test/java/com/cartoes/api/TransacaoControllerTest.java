package com.cartoes.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransacaoService transacaoService;

	private Transacao CriarTransacaoTestes() {

		Transacao transacao = new Transacao();

		transacao.setId(1);
		transacao.setCnpj("71031986000105");
		transacao.setJuros(1.5);
		transacao.setQtdParcelas(2);
		transacao.setValor(100.0);
		transacao.setCartao(new Cartao());
		transacao.getCartao().setNumero("1234567891234567");

		return transacao;

	}

	@Test
	@WithMockUser
	public void testBuscarPorCartaoNumeroSucesso() throws Exception {

		Transacao transacao = CriarTransacaoTestes();
		List<Transacao> lstTransacoes = new ArrayList<>();
		lstTransacoes.add(transacao);

		BDDMockito.given(transacaoService.buscarPorCartaoNumero(Mockito.anyString()))
				.willReturn(Optional.of(lstTransacoes));

		mvc.perform(
				MockMvcRequestBuilders.get("/api/transacao/cartao/0123456789123456").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.dados[0].id").value(transacao.getId()))
				.andExpect(jsonPath("$.dados[0].cnpj").value(transacao.getCnpj()))
				.andExpect(jsonPath("$.dados[0].juros").value(transacao.getJuros()))
				.andExpect(jsonPath("$.dados[0].qtdParcelas").value(transacao.getQtdParcelas()))
				.andExpect(jsonPath("$.dados[0].valor").value(transacao.getValor()))
				.andExpect(jsonPath("$.dados[0].cartaoNumero").value(transacao.getCartao().getNumero()))
				.andExpect(jsonPath("$.erros").isEmpty());

	}

	@Test
	@WithMockUser
	public void testBuscarPorCartaoNumeroInconsistencia() throws Exception {
		BDDMockito.given(transacaoService.buscarPorCartaoNumero(Mockito.anyString()))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(
				MockMvcRequestBuilders.get("/api/transacao/cartao/0123456789123456").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));
	}
	
	@Test
	@WithMockUser
	public void testSalvarSucesso() throws Exception {

		Transacao transacao = CriarTransacaoTestes();
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willReturn(transacao);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dados.id").value(transacao.getId()))
			.andExpect(jsonPath("$.dados.cnpj").value(transacao.getCnpj()))
			.andExpect(jsonPath("$.dados.juros").value(transacao.getJuros()))
			.andExpect(jsonPath("$.dados.qtdParcelas").value(transacao.getQtdParcelas()))
			.andExpect(jsonPath("$.dados.valor").value(transacao.getValor()))
			.andExpect(jsonPath("$.dados.cartaoNumero").value(transacao.getCartao().getNumero()))
			.andExpect(jsonPath("$.erros").isEmpty());
	}
	
	@Test
	@WithMockUser
	public void testSalvarInconsistencia() throws Exception {

		Transacao transacao = CriarTransacaoTestes();
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willThrow(new ConsistenciaException("Teste inconsistência."));
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())		
			.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjEmBranco() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setJuros("1.5");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("CNPJ não pode ser vazio."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjInvalido() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("123456789");
		objEntrada.setJuros("1.5");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("CNPJ inválido."));
	}
	

	@Test
	@WithMockUser
	public void testSalvarJurosEmBranco() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");		
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros não pode ser vazio."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setJuros("12345");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros deve conter entre 1 e 4 caracteres."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarQtdParcelasEmBranco() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setJuros("1.5");		
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Quantidade de parcelas não pode ser vazio."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarQtdParcelasExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("100");
		objEntrada.setJuros("1.5");		
		objEntrada.setValor("100.0");
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Quantidade de parcelas deve conter 1 ou 2 caracteres."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarValorEmBranco() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("1");
		objEntrada.setJuros("1.5");				
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Valor não pode ser vazio."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarValorExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("1");
		objEntrada.setValor("0123456789123");
		objEntrada.setJuros("1.5");				
		objEntrada.setCartaoNumero("1234567891234567");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Valor deve conter entre 1 e 10 caracteres."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoEmBranco() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("1");
		objEntrada.setValor("100");
		objEntrada.setJuros("1.5");						
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("O número do cartão não pode ser vazio."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoInsuficiente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("1");
		objEntrada.setValor("100");
		objEntrada.setJuros("1.5");			
		objEntrada.setCartaoNumero("123");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Numero do cartão deve conter 16 caracteres."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("71031986000105");
		objEntrada.setQtdParcelas("1");
		objEntrada.setValor("100");
		objEntrada.setJuros("1.5");			
		objEntrada.setCartaoNumero("1234654654654654654654");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Numero do cartão deve conter 16 caracteres."));
	}

}

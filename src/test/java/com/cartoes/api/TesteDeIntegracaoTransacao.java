package com.cartoes.api.integracao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.DataBinder;

import com.cartoes.api.controllers.TransacaoController;
import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.ClienteRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.response.Response;
import com.cartoes.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TesteDeIntegracaoTransacao {
	
	@Autowired
	private TransacaoController transacaoController;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
			
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Transacao transacaoTeste;
	private Cliente clienteTeste;
	private Cartao cartaoTeste;
	
	private void criarClienteTeste() throws ParseException{
		clienteTeste = new Cliente();
		
		clienteTeste.setCpf("03780843056");
		clienteTeste.setNome("teste");
		clienteTeste.setUf("TS");
	}
	
	private void criarCartaoTeste() throws ParseException{
		
		Calendar c = Calendar.getInstance();
		int dia = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, dia + 1);
		
		cartaoTeste = new Cartao();
		
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(clienteTeste);
		cartaoTeste.setDataValidade(c.getTime());
		cartaoTeste.setNumero("0123456789123456");
	}
	
	
	private void criarTransacaoTeste() throws ParseException{
		transacaoTeste = new Transacao();
		
		transacaoTeste.setCnpj("71031986000105");		
		transacaoTeste.setJuros(0.2);
		transacaoTeste.setCartao(cartaoTeste);
		transacaoTeste.setQtdParcelas(1);
		transacaoTeste.setValor(15.0);
		transacaoTeste.prePersist();
		
	}
	
	
	
	@Before
	public void setUp() throws Exception{
		criarClienteTeste();
		criarCartaoTeste();
		criarTransacaoTeste();
		
		clienteRepository.save(clienteTeste);
		cartaoRepository.save(cartaoTeste);
		transacaoRepository.save(transacaoTeste);
	}
	
	@After
	public void tearDown() throws Exception {
		
		transacaoRepository.deleteAll();
		cartaoRepository.deleteAll();
		clienteRepository.deleteAll();
		
	}
	
	@Test
	@WithMockUser
	public void buscarPorNumeroTest() throws Exception{
		
		ResponseEntity<Response<List<TransacaoDto>>> resultadoDto = transacaoController.buscarPorCartaoNumero("0123456789123456");
		
		TransacaoDto trasacaoTesteDto = ConversaoUtils.Converter(transacaoTeste);
		
		assertEquals(resultadoDto.getBody().getDados().get(0).toString(), trasacaoTesteDto.toString());
		
	}
	
	@Test
	@WithMockUser
	public void buscarPorNumeroInconsistenciaTest() throws Exception{
		
		ResponseEntity<Response<List<TransacaoDto>>> resultadoDto = transacaoController.buscarPorCartaoNumero("012345678912345");			
		
		assertEquals(resultadoDto.getStatusCodeValue(), 400);
		
	}
	
	@Test
	@WithMockUser
	public void salvarTransacaoTest() throws Exception{
		
		TransacaoDto transacaoDto = new TransacaoDto();
		transacaoDto.setCnpj("18808626000194");
		transacaoDto.setJuros("1");
		transacaoDto.setQtdParcelas("1");
		transacaoDto.setValor("100");
		transacaoDto.setCartaoNumero("0123456789123456");
		
		DataBinder teste = new DataBinder(transacaoDto);
	
		ResponseEntity<Response<TransacaoDto>> resultadoDto = transacaoController.salvar(transacaoDto, teste.getBindingResult());
		
		assertEquals(resultadoDto.getStatusCodeValue(), 200);
		
	}
	
	@Test
	@WithMockUser
	public void salvarTransacaoInconsistenciaTest() throws Exception{
		
		TransacaoDto transacaoDto = new TransacaoDto();
		transacaoDto.setCnpj("18808626000194");
		transacaoDto.setJuros("1");
		transacaoDto.setQtdParcelas("1");
		transacaoDto.setValor("100");
		transacaoDto.setCartaoNumero("012345678912345");
		
		DataBinder teste = new DataBinder(transacaoDto);
	
		ResponseEntity<Response<TransacaoDto>> resultadoDto = transacaoController.salvar(transacaoDto, teste.getBindingResult());
		
		assertEquals(resultadoDto.getStatusCodeValue(), 400);
		
	}
}

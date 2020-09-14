package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoServiceTest {

	@MockBean
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private TransacaoService transacaoService;
		
	@MockBean
	private CartaoRepository cartaoRepository;
	
	private Transacao transacaoTeste;
	private Cartao cartaoTeste;
	
	@Before
	public void setUp() throws Exception{
		Calendar c = Calendar.getInstance();
		int dia = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, dia + 1);
		
		transacaoTeste = new Transacao();
		transacaoTeste.setCartao(new Cartao());
		cartaoTeste= new Cartao();
		cartaoTeste.setDataValidade(c.getTime());
	}
	
	@Test
	public void testBuscaPorNumeroCartaoExistente() throws ConsistenciaException{
		
		
		List<Transacao> lstTransacoes = new ArrayList<>(); 
		lstTransacoes.add(new Transacao());
		
		BDDMockito.given(transacaoRepository.findByCartaoNumero(Mockito.anyString()))
		.willReturn(Optional.of(lstTransacoes));
		
		Optional<List<Transacao>> resultado = transacaoService.buscarPorCartaoNumero("0123456789123456");
		
		assertTrue(resultado.isPresent());	
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testBuscaPorNumeroCartaoNaoExistente() throws ConsistenciaException{
		
		List<Transacao> lstTransacoes = new ArrayList<>(); 
		
		BDDMockito.given(transacaoRepository.findByCartaoNumero(Mockito.anyString()))
		.willReturn(Optional.of(lstTransacoes));
		
		transacaoService.buscarPorCartaoNumero("0123456789123456");
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException, ParseException {				
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
			.willReturn(Optional.of(cartaoTeste));
		
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
			.willReturn(new Transacao());
		
		Transacao resultado = transacaoService.salvar(transacaoTeste);
		
		assertNotNull(resultado);
	}
	
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoNaoEncontrado() throws ConsistenciaException{
	  
	  BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
	  .willReturn(Optional.empty());
	  
	  transacaoService.salvar(transacaoTeste);
	  
	}
	  
	@Test(expected = ConsistenciaException.class)
	public void testSalvarTransacaoComId() throws ConsistenciaException{
	  
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(cartaoTeste));
	  
	  transacaoTeste.setId(1);;
	  
	  transacaoService.salvar(transacaoTeste);
	  
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoBloqueado() throws ConsistenciaException{
	  
		cartaoTeste.setBloqueado(true);
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(cartaoTeste));		
	  
	  transacaoService.salvar(transacaoTeste);
	  
	}
	
	
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoVencido() throws ConsistenciaException {

		Calendar c = Calendar.getInstance();
		int dia = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, dia - 1);
		
		cartaoTeste.setDataValidade(c.getTime());;

		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(cartaoTeste));

		transacaoService.salvar(transacaoTeste);

	}
	 
	 
	 
}
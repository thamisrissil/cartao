package com.cartoes.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.cartoes.api.entities.Transacao;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoRepositoryTest {

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

		clienteTeste.setCpf("123");
		clienteTeste.setNome("teste");
		clienteTeste.setUf("TS");
	}

	private void criarCartaoTeste() throws ParseException{
		cartaoTeste = new Cartao();

		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(clienteTeste);
		cartaoTeste.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
		cartaoTeste.setNumero("321");
	}


	private void criarTransacaoTeste() throws ParseException{
		transacaoTeste = new Transacao();

		transacaoTeste.setCnpj("01234567891234");		
		transacaoTeste.setJuros((int) 0.2);
		transacaoTeste.setCartao(cartaoTeste);
		transacaoTeste.setQdt_Parcelas(1);
		transacaoTeste.getValor();
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
	public void testFindById() {
		Transacao transacao = transacaoRepository.findById(transacaoTeste.getId()).get();
		assertEquals(transacaoTeste.getId(), transacao.getId());
	}

	@Test
	public void testFindByCartaoNumero() {
		List<Transacao> lstTransacao = transacaoRepository.findByCartaoNumero(transacaoTeste.getCartao().getNumero());
		if(lstTransacao.size() != 1) {
			fail();
		}

		Transacao transacao = lstTransacao.get(0);

		assertTrue(transacao.getCartao().getNumero().equals(transacaoTeste.getCartao().getNumero()));
	}

}

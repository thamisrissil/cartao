package com.cartoes.api.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@Service
public class TransacaoService {

	private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);
	@Autowired
	private TransacaoRepository transacaoRepository;
	private CartaoRepository cartaoRepository;
	public Optional<List<Transacao>> buscarPorNumero(String numero) throws ConsistenciaException {
		log.info("Service: buscando o numero do cartao : {}", numero);
		Optional<List<Transacao>> transacao = Optional.ofNullable(transacaoRepository.findByCartaoNumero(numero));
		if (!transacao.isPresent()){
			log.info("Service: Nenhum cartao com o numero");
			 throw new ConsistenciaException("Nenhum cartao com o numero: {} foi encontrado", numero);
		}
		 return transacao;
	}
	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
		log.info("Service: salvando o numero:{}", transacao);
		if (!cartaoRepository.findByNumero(transacao.getCartao().getNumero()).isPresent()) {
			log.info("Service: Nenhum cartao com o numero: {} foi encontrado", transacao.getCartao().getNumero());
			throw new ConsistenciaException("Nenhum cartao com o numero: {} foi encontrado", transacao.getCartao().getNumero());
		}
		if(cartaoRepository.findByNumero(transacao.getCartao().getNumero()).get().getBloqueado()) {
			log.info("Não é possível incluir transações para este cartão, pois o mesmo\r\n encontra-se bloqueado.");
			throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo\\r\\n encontra-se bloqueado");
		}
		if(cartaoRepository.findByNumero(transacao.getCartao().getNumero()).get().getDataValidade().before(new Date())) {
			log.info("Não é possível incluir transações para este cartão, pois o mesmo\r\n encontra-se vencido.”.\r\n");
			throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo\\r\\n encontra-se vencido");
		}
		if(transacao.getId() > 0) {
			log.info("Transações não\r\n podem ser alteradas, apenas incluídas");
			throw new ConsistenciaException("Transações não\r\n podem ser alteradas, apenas incluídas");
		}
		try {
			 return transacaoRepository.save(transacao);
			 } catch (DataIntegrityViolationException e) {

			 log.info("Service: Houve um erro");
			 throw new ConsistenciaException("Houve um erro");

			 }
	} 

} 

package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

  @Autowired 
  private TopicoRepository topicoRepository;

  @Autowired 
  private CursoRepository cursoRepository;

  @GetMapping
  public List<TopicoDto> lista(String nomeCurso) {
    // Topico topico = new Topico("Duvida", "Duvidas com React.Js", new Curso("Spring", "Programação"));

    if (nomeCurso == null) {
      List<Topico> topicos = topicoRepository.findAll();
      return TopicoDto.converteListaParaListaDto(topicos);

    } else {
      List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
      return TopicoDto.converteListaParaListaDto(topicos);
    }

  }
  
  @PostMapping
  @Transactional
  public ResponseEntity<TopicoDto> cadastrar(@RequestBody  @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
    Topico topico = form.converter(cursoRepository);
    topicoRepository.save(topico);

    URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
    return ResponseEntity.created(uri).body(new TopicoDto(topico));
  }

  @GetMapping("/{id}")
  @Transactional
  public TopicoDto detalhar(@PathVariable("id") Long id) {
    Topico topico = topicoRepository.getById(id);
    return new TopicoDto(topico);
  }

  @PutMapping("/{id}")
  @Transactional
  public ResponseEntity<TopicoDto> atualizar(@PathVariable("id") Long id,
      @RequestBody @Valid AtualizacaoTopicoForm form) {
    Topico topico = form.atualizar(id, topicoRepository);
    return ResponseEntity.ok(new TopicoDto(topico));
  }
  
  @DeleteMapping
  @Transactional
  public ResponseEntity<?> remover(@PathVariable Long id) {
    topicoRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }


}
